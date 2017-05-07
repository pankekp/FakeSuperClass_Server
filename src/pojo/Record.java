package pojo;

import java.util.List;

/**
 * @author Zhao Zhixu
 */
public class Record {
	List<Course> CourseList;
	List<Schedule> ScheduleList;
	public List<Course> getCourseList() {
		return CourseList;
	}
	public void setCourseList(List<Course> courseList) {
		CourseList = courseList;
	}
	public List<Schedule> getScheduleList() {
		return ScheduleList;
	}
	public void setScheduleList(List<Schedule> scheduleRList) {
		ScheduleList = scheduleRList;
	}	
}
