package utils;

import java.util.Map;

import pojo.Record;

/**
 * @author Zhao Zhixu
 */
public interface GetContent {

	/**
	 * Get validation code image from target url.
	 * 
	 * @param url
	 *            Target URL
	 * @param path
	 *            Storage path for vc.jpg
	 * @return cookie
	 **/
	public String getVc(String url, String path);

	/**
	 * Get <TeacherID, TeacherName> map from target url.
	 * 
	 * @param url
	 *            Target URL
	 * @return <TeacherId, TeacherName> map
	 **/
	public Map<String, String> getTeacherMap(String url);

	public Map<String, String> getSemesterMap(String url);

	/**
	 * Get record from remote, including courses and schedules.
	 * 
	 * @param id
	 *            Teacher ID
	 * @param semester
	 *            Semester
	 * @param vc
	 *            Validation code
	 * @param url
	 *            Target URL
	 * @param cookie
	 *            Cookie
	 * @return A Record, including a Course list and a Schedule list
	 **/
	public Record getRecord(String id, String semester, String vc, String url, String cookie);

	/**
	 * Get record from remote, including courses and schedules. Semester is set
	 * to 20161 by default.
	 * 
	 * @param id
	 *            Teacher ID
	 * @param vc
	 *            Validation code
	 * @param url
	 *            Target URL
	 * @param cookie
	 *            Cookie
	 * @return A Record, including a Course list and a Schedule list
	 **/
	public Record getRecord(String id, String vc, String url, String cookie);
}
