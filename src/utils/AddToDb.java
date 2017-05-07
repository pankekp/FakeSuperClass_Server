package utils;

import java.util.List;

import dao.impl.ManagerDaoImpl;
import pojo.Course;
import pojo.Schedule;
import pojo.Semester;
import pojo.Teacher;
import pojo.TimeStamp;

public class AddToDb {
	ManagerDaoImpl md = new ManagerDaoImpl();

	public int AddTeacherCache(List<Teacher> teachers) {
		int i = 0;
		for (Teacher teacher : teachers) {
			md.addTeacher(teacher);
			i++;
		}

		return i;
	}

	public int AddCourseCache(List<Course> courses) {
		int i = 0;
		for (Course course : courses) {
			md.addCourse(course);
			i++;
		}

		return i;
	}

	public int AddScheduleCache(List<Schedule> schedules) {
		int i = 0;
		for (Schedule schedule : schedules) {
			md.addSchedule(schedule);
			i++;
		}

		return i;
	}

	public int AddSemesterCache(List<Semester> semesters) {
		int i = 0;
		for (Semester semester : semesters) {
			md.addSemester(semester);
			i++;
		}

		return i;
	}

	public int AddTimeStampCache(List<TimeStamp> timeStamps) {
		int i = 0;
		for (TimeStamp timeStamp : timeStamps) {
			md.addTimeStamp(timeStamp);
			i++;
		}

		return i;
	}
}
