package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import dao.impl.ManagerDaoImpl;
import pojo.Semester;
import pojo.Teacher;
import pojo.TimeStamp;
import service.impl.CrawlerManagerImpl;
import utils.AddToDb;
import utils.CacheMethods;
import utils.GetContent;
import utils.GetContentImpl;

@Controller
public class DataTransfer {

	private CrawlerManagerImpl cm;
	private GetContent gc = new GetContentImpl();
	int i = 0;
	private ManagerDaoImpl md = new ManagerDaoImpl();
	private CacheMethods Cache;

	@ResponseBody
	@RequestMapping(value = "/rec/sId/{sId}/tId/{tId}/code/{code}/cookie/{cookie}", method = {
			RequestMethod.GET }, produces = "application/json;charset=UTF-8")
	public String rec(@PathVariable String sId, @PathVariable String tId, @PathVariable String code,
			@PathVariable String cookie) {

		//测试使用
		String test = sId + "..." + tId + "..." + code + "..." + cookie;
		System.out.println(test);

		// 本地库查找
		String json1 = cm.findRecord(tId, sId);
		System.out.println(json1);
		if (!"notCrawl".equals(json1)) {
			// 找到表示爬取过，将json1传到客户端
			return json1;
		} else {
			// 找不到则表示为未爬取过，进行爬取
			String json2 = cm.createRecord(sId, tId, code, "ASP." + cookie);
			System.out.println(json2);
			if ("fail".equals(json2)) {
				// 爬取失败则爬取验证码,将验证码传到客户端
				String imgJson = cm.findCheckCode("c:/img/" + i + ".jpg");
				i++;
				return imgJson;
			} else {
				// 爬取成功，直接返回
				return json2;
			}
		}
	}

	@ResponseBody
	@RequestMapping(value = "/list/timeStamp/{timeStamp}", method = {
			RequestMethod.GET }, produces = "application/json;charset=UTF-8")
	public String list(@PathVariable String timeStamp) {

		System.out.println(timeStamp);
		String temp = cm.updateTable(timeStamp);
		return temp;

	}

	@PostConstruct
	public void test1() {
		Cache = new CacheMethods();
		cm = new CrawlerManagerImpl();
		//每次启动服务器时爬取教师列表及学期列表
		Map<String,String> semesterMap = gc.getSemesterMap("http://1.85.55.152/ZNPK/TeacherKBFB.aspx");
		Map<String,String> teacherMap = gc.getTeacherMap("http://1.85.55.152/ZNPK/Private/List_JS.aspx?xnxq=20161&t=282");
		
		//将爬到的列表存入服务器的数据库
		for(String key:teacherMap.keySet()){
			Teacher teacher=new Teacher();
			teacher.setTeacherId(key);
			teacher.setTeacherName(teacherMap.get(key));
			md.addTeacher(teacher);
		}
		for(String key:semesterMap.keySet()){
			Semester semester=new Semester();
			semester.setSemesterId(key);
			semester.setSemesterName(semesterMap.get(key));
			md.addSemester(semester);
		}
		//更新时间戳
		TimeStamp timeStamp=new TimeStamp();
		timeStamp.setTimeStampSecond(System.currentTimeMillis());
		md.addTimeStamp(timeStamp);

		//将服务器数据库中的内容加载到缓存中
		Cache.setTeacherCache(md.findAllTeachers());
		Cache.setSemesterCache(md.findAllSemesters());
		Cache.setCourseCache(md.findAllCourses());
		Cache.setScheduleCache(md.findAllSchedules());
		List<TimeStamp> ts = new ArrayList<TimeStamp>();
		ts.add(md.findNewTimeStamp());
		Cache.setTimeStampCache(ts);
		cm.setCache(Cache);
		cm.send(Cache);

		//开启子线程，每隔一段时间将缓存中的内容更新至数据库中
		new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(3000000);
						// update db by cache
						AddToDb addToDb = new AddToDb();
						addToDb.AddTeacherCache(Cache.getTeacherCache());
						addToDb.AddCourseCache(Cache.getCourseCache());
						addToDb.AddScheduleCache(Cache.getScheduleCache());
						addToDb.AddSemesterCache(Cache.getSemesterCache());
						addToDb.AddTimeStampCache(Cache.getTimeStampCache());
					} catch (InterruptedException e) {
						//e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@PreDestroy
	public void test2() {
		//关闭服务器之前将缓存中的内容更新至数据库中
		AddToDb addToDb = new AddToDb();
		addToDb.AddTeacherCache(Cache.getTeacherCache());
		addToDb.AddCourseCache(Cache.getCourseCache());
		addToDb.AddScheduleCache(Cache.getScheduleCache());
		addToDb.AddSemesterCache(Cache.getSemesterCache());
		addToDb.AddTimeStampCache(Cache.getTimeStampCache());
	}
}