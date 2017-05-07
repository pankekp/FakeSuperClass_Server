package service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pojo.Course;
import pojo.InitInfo;
import pojo.Record;
import pojo.Schedule;
import pojo.Semester;
import pojo.Teacher;
import service.CrawlerManager;
import utils.CacheMethods;
import utils.GetContent;
import utils.GetContentImpl;
import utils.JsonBuilder;

public class CrawlerManagerImpl implements CrawlerManager {

	private String recordUrl = "http://1.85.55.152/ZNPK/TeacherKBFB_rpt.aspx";
	private String imgUrl = "http://1.85.55.152/sys/ValidateCode.aspx";
	private GetContent gc = new GetContentImpl();
	private JsonBuilder jb = new JsonBuilder();
	private CacheMethods Cache;

	public CacheMethods getCache() {
		return Cache;
	}

	public void setCache(CacheMethods cache) {
		Cache = cache;
	}

	public CrawlerManagerImpl(CacheMethods cache) {
		super();
		Cache = cache;
	}

	public CrawlerManagerImpl() {
		super();
	}

	//在服务器数据库中根据教师id及学期id进行查询，查到结果则返回，否则返回“未爬取过”
	@Override
	public String findRecord(String teacherId, String semesterId) {
		List<Schedule> schedules = Cache.findSchedules(teacherId, semesterId);
		// //测试使用
		// for (Schedule s:Cache.getScheduleCache()){
		// System.out.println(s.getTeacherId()+"..."+s.getSemesterId());
		// }
		// System.out.println("findRecord:"+teacherId+".."+semesterId);
		if (schedules.isEmpty())
			return "notCrawl";

		Record record = new Record();
		record.setScheduleList(schedules);
		List<Course> courses = new ArrayList<Course>();
		for (Schedule schedule : schedules) {
			Course course = null;
			if ((course = Cache.findCourse(schedule.getCourseId())) != null)
				courses.add(course);
		}
		record.setCourseList(courses);
		String json = jb.buildRecord(record);
		return json;
	}

	
	
	/**
	 * controller层收到“未爬取过”的返回结果时则会调用此函数进行爬取
	 * 1.爬取失败则会返回“fail”
	 * 2.爬取成功并且有课：将结果存入缓存同时解析爬取结果并返回controller层
	 * 3.爬取成功却没有课：同上
	 */
	@Override
	public String createRecord(String sId, String tId, String code, String cookie) {
		Record record = gc.getRecord(tId, sId, code, recordUrl, cookie);
		if (record != null) {
			List<Schedule> schedules = record.getScheduleList();
			for (Schedule schedule : schedules) {
				schedule.setSemesterId(sId);
			}
			List<Course> courses = record.getCourseList();
			String json = jb.buildRecord(record);
			if (schedules.size() != 0 || courses.size() != 0) {
				Cache.saveCourses(courses);
				Cache.saveSchedule(schedules);
			}
			return json;
		} else {
			return "fail";
		}
	}

	
	//若爬取失败controller层会调用此方法进行验证码的爬取，同时获得本次爬取会话的cookie，将cookie及解码后的验证码图片解析为json格式一并返回客户端
	@Override
	public String findCheckCode(String path) {
		String imgJson = null;
		try {
			String cookie = gc.getVc(imgUrl, path);
			imgJson = jb.buildVcImg(path, cookie);
			File f = new File(path);
			f.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imgJson;
	}

	/**
	 * 客户端每次启动时会访问controller层，controller层会调用此方法进行列表的更新
	 * 1.若客户端发送至服务器的时间戳小于服务器端保留的，则服务器端将当前缓存中的教师列表及学期列表以及时间戳返回给客户端
	 * 2.若时间戳等于服务器端的时间戳，则返回“ok”，客户端接收到相应信息后便从其本地数据库中提取列表信息
	 */
	@Override
	public String updateTable(String timeStamp) {
		long millisClient = Long.parseLong(timeStamp);
		long millisServer = 0;
		if (Cache.findTimeStamp(0) == null) {
			millisServer = 1;
		} else {
			millisServer = Cache.findTimeStamp(0).getTimeStampSecond();
		}
		System.out.println(millisClient + "..." + millisServer);
		if (millisClient < millisServer) {
			// 返回最新数据
			List<Teacher> teachers = Cache.getTeacherCache();
			List<Semester> semesters = Cache.getSemesterCache();
			Map<String, String> teacherMap = new TreeMap<String, String>();
			Map<String, String> semesterMap = new TreeMap<String, String>();
			InitInfo initInfo = new InitInfo();
			int i = 0;
			for (Teacher teacher = teachers.get(i); i < teachers.size(); i++) {
				teacherMap.put(teacher.getTeacherId(), teacher.getTeacherName());
			}
			i = 0;
			for (Semester semester = semesters.get(i); i < semesters.size(); i++) {
				semesterMap.put(semester.getSemesterId(), semester.getSemesterName());
			}
			initInfo.setSemesterMap(semesterMap);
			initInfo.setTeacherMap(teacherMap);
			initInfo.setTimeStamp(millisServer);
			System.out.println("timestamp:" + millisServer);
			return jb.buildInitInfo(initInfo);
		} else {
			return "ok";
		}
	}

	public void send(CacheMethods cache) {
		this.Cache = cache;
	}
}
