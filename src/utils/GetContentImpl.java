package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pojo.Course;
import pojo.Header;
import pojo.Record;
import pojo.Schedule;

/**
 * @author Zhao Zhixu
 */
public class GetContentImpl implements GetContent {
	/* get validation code and return cookie */
	@Override
	public String getVc(String url, String path) {
		String[] sessionid = null;
		try {
			URL u = new URL(url);
			HttpURLConnection con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "image/webp,image/*,*/*;q=0.8");
			con.setRequestProperty("Connection", "keep-alive");
			con.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
			con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			String host = url.split("/")[2];
			con.setRequestProperty("Host", host);
			String referer = "http://" + host + "/ZNPK/TeacherKBFB.aspx";
			con.setRequestProperty("Referer", referer);
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36");
			con.setDoInput(true);
			con.setDoOutput(true);

			String cookie = con.getHeaderField("Set-Cookie");
			sessionid = cookie.split(";");

			BufferedInputStream in = new BufferedInputStream(con.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
			byte[] buffer = new byte[128];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}

			in.close();
			out.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sessionid[0];
	}

	@Override
	public Map<String, String> getTeacherMap(String url) {
		String resStr = "";
		Header header = new Header();
		header.setAccept("text/html");
		header.setCookie("");
		try {
			resStr = Methods.get(header, url);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Document doc = Jsoup.parse(resStr);
		Element script = doc.getElementsByTag("script").first();
		Pattern p = Pattern.compile("<select\\b([^>]+)>(.*?)</select>");
		Matcher m = p.matcher(script.data());
		String innerHtml = "";
		if (m.find())
			innerHtml = m.group();
		Document innerdoc = Jsoup.parseBodyFragment(innerHtml);
		Element body = innerdoc.body();
		Elements options = body.getElementsByTag("option");

		Map<String, String> resMap = new TreeMap<String, String>();
		for (Element option : options) {
			String teacherId = option.attr("value");
			String teacherName = option.text();
			if (teacherId != null && !"".equals(teacherId)) {
				resMap.put(teacherId, teacherName);
			}
		}
		return resMap;
	}

	@Override
	public Map<String, String> getSemesterMap(String url) {
		String resStr = "";
		Header header = new Header();
		header.setAccept("text/html");
		header.setCookie("");
		try {
			resStr = Methods.get(header, url);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, String> semesterMap = new TreeMap<String, String>();
		Document doc = Jsoup.parse(resStr);
		Element td = doc.select("td:contains(学年学期)").first();
		Elements options = td.select("select[name=Sel_XNXQ]").first().children();
		for (Element option = options.first(); option != null; option = option.nextElementSibling()) {
			String value = option.attr("value");
			String text = option.text();
			semesterMap.put(value, text);
		}

		return semesterMap;
	}

	@Override
	public Record getRecord(String id, String semester, String vc, String url, String cookie) {
		if (cookie == null)
			cookie = "";
		Header header = new Header();
		header.setAccept("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		header.setCookie(cookie);
		header.setAccept_Encoding("gzip, deflate");
		header.setAccept_Language("zh-CN,zh;q=0.8");
		header.setCache_Control("max-age=0");
		header.setConnection("keep-alive");
		header.setContent_Length("49");
		header.setContent_Type("application/x-www-form-urlencoded");
		String host = url.split("/")[2];
		header.setHost(host);
		String origin = url.substring(0, url.indexOf("/ZNPK"));
		header.setOrigin(origin);
		String referer = "http://" + host + "/ZNPK/TeacherKBFB.aspx";
		header.setReferer(referer);
		header.setUser_Agent(
				"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36");

		String params = "Sel_XNXQ=" + semester + "&Sel_JS=" + id + "&type=2&txt_yzm=" + vc;
		String resStr = "";
		try {
			resStr = Methods.post(header, url, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(resStr);
		Document doc = Jsoup.parse(resStr);
		Element script = doc.select("script").first();
		if (script != null) {
			Pattern p = Pattern.compile("验证码错误");
			Matcher m = p.matcher(script.data());
			if (m.find()) {
				// TODO: log output: Wrong validation code
				return null;
			}
		}

		Record record = new Record();
		record.setCourseList(new ArrayList<Course>());
		record.setScheduleList(new ArrayList<Schedule>());

		Element tableRpt = doc.select("table#pageRpt").first();
		Element tableContent = tableRpt.select("table[border=1]").first();
		if (tableContent == null) {
			// No course for this teacher
			return record;
		}

		int courseIndex = 1;
		int creditIndex = 2;
		int methodIndex = 3;
		int typeIndex = 4;
		int classNoIndex = 5;
		int classesIndex = 6;
		int peopleNumIndex = 7;
		int timeIndex = 8;
		int locationIndex = 9;
		Elements tdheaders = tableContent.select("tr").first().children();
		int i = 0;
		for (Element td = tdheaders.first(); td != null; td = td.nextElementSibling(), i++) {
			String text = td.text();
			switch (text) {
			case "课程":
				courseIndex = i;
				break;
			case "学分":
				creditIndex = i;
				break;
			case "授课方式":
				methodIndex = i;
				break;
			case "课程类别":
				typeIndex = i;
				break;
			case "上课班号":
				classNoIndex = i;
				break;
			case "上课班级":
				classesIndex = i;
				break;
			case "上课人数":
				peopleNumIndex = i;
				break;
			case "时间":
				timeIndex = i;
				break;
			case "地点":
				locationIndex = i;
				break;
			}
		}

		Elements trs = tableContent.select("tr:nth-of-type(n+2)");
		String oldCourse = null;
		String oldCourseId = null;
		String oldTime = null;
		String oldLocation = "";
		String oldClasses = "";
		String oldClassNo = "";
		String oldPeopleNum = "";
		Pattern pc = Pattern.compile("\\[([\\d\\w]+)\\]");
		Pattern ps = Pattern.compile("周.");
		for (Element tr = trs.first(); tr != null; tr = tr.nextElementSibling()) {
			String course = tr.child(courseIndex).text();
			if (!"".equals(course) && !course.equals(oldCourse)) {
				Course newCourse = new Course();
				String courseName = course.substring(course.indexOf("]") + 1, course.length());
				Matcher m = pc.matcher(course);
				String courseId = null;
				if (m.find())
					courseId = m.group(1);
				newCourse.setCourseId(courseId);
				newCourse.setCourseName(courseName);
				newCourse.setCredit(tr.child(creditIndex).text());
				newCourse.setMethod(tr.child(methodIndex).text());
				newCourse.setType(tr.child(typeIndex).text());
				record.getCourseList().add(newCourse);
				oldCourse = courseName;
				oldCourseId = courseId;
			}
			String time = tr.child(timeIndex).text();
			if (!"".equals(time) && !time.equals(oldTime)) {
				Schedule newSchedule = new Schedule();
				String Classes = tr.child(classesIndex).text();
				String ClassNo = tr.child(classNoIndex).text();
				String Location = tr.child(locationIndex).text();
				String PeopleNum = tr.child(peopleNumIndex).text();
				if ("".equals(Classes) || Classes == null)
					newSchedule.setClasses(oldClasses);
				else {
					newSchedule.setClasses(Classes);
					oldClasses = Classes;
				}
				if ("".equals(ClassNo) || Classes == null)
					newSchedule.setClassNo(oldClassNo);
				else {
					newSchedule.setClassNo(ClassNo);
					oldClassNo = ClassNo;
				}
				if ("".equals(Location) || Location == null)
					newSchedule.setLocation(oldLocation);
				else {
					newSchedule.setLocation(Location);
					oldLocation = Location;
				}
				if ("".equals(PeopleNum) || PeopleNum == null)
					newSchedule.setPeopleNum(oldPeopleNum);
				else {
					newSchedule.setPeopleNum(PeopleNum);
					oldPeopleNum = PeopleNum;
				}
				newSchedule.setTeacherId(id);
				newSchedule.setCourseId(oldCourseId);
				Matcher m = ps.matcher(time);
				String timechomp = time;
				if (m.find())
					timechomp = m.replaceAll("周 ");
				newSchedule.setTime(timechomp);
				record.getScheduleList().add(newSchedule);
				oldTime = time;
			}
		}
		return record;
	}

	@Override
	public Record getRecord(String id, String vc, String url, String cookie) {
		Record record = this.getRecord(id, "20161", vc, url, cookie);
		return record;
	}
}
