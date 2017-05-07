package utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import net.iharder.Base64;
import pojo.InitInfo;
import pojo.Record;
import pojo.Semester;
import pojo.Teacher;

/**
 * Build Record, teacher list, course list, or semester list from JSON string.
 * 
 * @author zhixu
 *
 */
public class JsonBuilder {

	/**
	 * Get a JSON string from a Record object
	 * 
	 * @param record
	 * @return the output JSON string
	 */
	public String buildRecord(Record record) {
		JSONObject resJsonObj = new JSONObject();
		resJsonObj.put("type", "record");
		JSONObject recordObj = new JSONObject();
		recordObj.put("courseList", record.getCourseList());
		recordObj.put("scheduleList", record.getScheduleList());
		resJsonObj.put("record", recordObj);

		return resJsonObj.toString();
	}

	/**
	 * Get a JSON string from an InitInfo object
	 * 
	 * @param initInfo
	 * @return the output JOSN string
	 */
	public String buildInitInfo(InitInfo initInfo) {
		JSONObject resJsonObj = new JSONObject();
		resJsonObj.put("type", "initInfo");
		resJsonObj.put("timeStamp", initInfo.getTimeStamp());

		// TODO: use a List, not a Map
		Map<String, String> teacherMap = initInfo.getTeacherMap();
		List<Teacher> teachers = new ArrayList<>();
		for (String key : teacherMap.keySet()) {
			Teacher teacher = new Teacher();
			teacher.setTeacherId(key);
			teacher.setTeacherName(teacherMap.get(key));
			teachers.add(teacher);
		}
		resJsonObj.put("teacherMap", teachers);

		// TODO: use a List, not a Map
		Map<String, String> semesterMap = initInfo.getSemesterMap();
		List<Semester> semesters = new ArrayList<>();
		for (String key : semesterMap.keySet()) {
			Semester semester = new Semester();
			semester.setSemesterId(key);
			semester.setSemesterName(semesterMap.get(key));
			semesters.add(semester);
		}
		resJsonObj.put("semesterMap", semesters);

		return resJsonObj.toString();
	}

	/**
	 * Get a JSON string from a TeacherMap <teacherId, teacherNam>
	 * 
	 * @param teacherMap
	 * @return the output JSON string
	 */
	public String buildTeacherMap(Map<String, String> teacherMap) {
		JSONObject resJsonObj = new JSONObject();
		resJsonObj.put("type", "teacherMap");

		// TODO: use a List, not a Map
		List<Teacher> teachers = new ArrayList<>();
		for (String key : teacherMap.keySet()) {
			Teacher teacher = new Teacher();
			teacher.setTeacherId(key);
			teacher.setTeacherName(teacherMap.get(key));
			teachers.add(teacher);
		}
		resJsonObj.put("teacherMap", teachers);

		return resJsonObj.toString();
	}

	/**
	 * Get a JSON string from a SemesterMap<String, String>
	 * 
	 * @param semesterMap
	 * @return the output JSON String
	 */
	public String buildSemesterMap(Map<String, String> semesterMap) {
		JSONObject resJsonObj = new JSONObject();
		resJsonObj.put("type", "semesterMap");

		// TODO: use a List, not a Map
		List<Semester> semesters = new ArrayList<>();
		for (String key : semesterMap.keySet()) {
			Semester semester = new Semester();
			semester.setSemesterId(key);
			semester.setSemesterName(semesterMap.get(key));
			semesters.add(semester);
		}
		resJsonObj.put("semesterMap", semesters);

		return resJsonObj.toString();
	}

	/**
	 * Get a JSON string from an image
	 * 
	 * @param vcpath
	 *            the path where the image to be encoded is stored
	 * @return the output JSON string
	 * @throws IOException
	 */
	public String buildVcImg(String vcpath, String cookie) throws IOException {
		JSONObject resJsonObj = new JSONObject();
		resJsonObj.put("type", "vcImage");

		File file = new File(vcpath);
		FileInputStream fin = new FileInputStream(file);
		ByteArrayOutputStream baos = new ByteArrayOutputStream((int) file.length());

		byte[] buffer = new byte[4096];
		int len = -1;
		while ((len = fin.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}

		byte[] data = baos.toByteArray();
		fin.close();
		baos.close();

		String vcString = new String(Base64.encodeBytes(data));
		resJsonObj.put("vcImage", vcString);
		resJsonObj.put("cookie", cookie);

		return resJsonObj.toString();
	}
}
