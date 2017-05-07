package utils;

import java.util.ArrayList;
import java.util.List;

import pojo.*;

public class CacheMethods {
	private List<Schedule> scheduleCache;
	private List<Course> courseCache;
	private List<Teacher> teacherCache;
	private List<TimeStamp> timeStampCache;
	private List<Semester> semesterCache;

	public List<Schedule> findSchedules(String teacherId, String semesterId) {
		List<Schedule> resSchedules = new ArrayList<Schedule>();
		for (Schedule schedule : scheduleCache) {
			System.out.println(schedule.getTime());
			if (schedule.getTeacherId().equals(teacherId) && schedule.getSemesterId().equals(semesterId)) {
				System.out.println("findSchedule:" + schedule.getTeacherId() + "..." + schedule.getSemesterId());
				resSchedules.add(schedule);
			}
		}

		return resSchedules;
	}

	public Course findCourse(String courseId) {
		for (Course course : courseCache) {
			if (course.getCourseId().equals(courseId))
				return course;
		}

		return null;
	}

	public List<Semester> findSemesters(String semesterId) {
		List<Semester> resSemesters = new ArrayList<Semester>();
		for (Semester semester : semesterCache) {
			if (semester.getSemesterId().equals(semesterId)) {
				resSemesters.add(semester);
			}
		}

		return resSemesters;
	}

	public Teacher findTeacher(String teacherId) {
		for (Teacher teacher : teacherCache) {
			if (teacher.getTeacherId().equals(teacherId)) {
				return teacher;
			}
		}

		return null;
	}

	public TimeStamp findTimeStamp(int timeStampId) {
		for (TimeStamp timeStamp : timeStampCache) {
			if (timeStamp.getTimeStampId() == timeStampId) {
				return timeStamp;
			}
		}

		return null;
	}

	public int saveSchedule(List<Schedule> schedules) {
		int i = 0;
		for (Schedule schedule : schedules) {
			scheduleCache.add(schedule);
			i++;
		}

		return i;
	}

	public int saveCourses(List<Course> courses) {
		int i = 0;
		for (Course course : courses) {
			courseCache.add(course);
			i++;
		}

		return i;
	}

	public int saveTeachers(List<Teacher> teachers) {
		int i = 0;
		for (Teacher teacher : teachers) {
			teacherCache.add(teacher);
			i++;
		}

		return i;
	}

	public int saveSemesters(List<Semester> semesters) {
		int i = 0;
		for (Semester semester : semesters) {
			semesterCache.add(semester);
			i++;
		}

		return i;
	}

	public int saveTimeStamps(List<TimeStamp> timeStamps) {
		int i = 0;
		for (TimeStamp timeStamp : timeStamps) {
			timeStampCache.add(timeStamp);
			i++;
		}

		return i;
	}

	public List<Schedule> getScheduleCache() {
		return scheduleCache;
	}

	public void setScheduleCache(List<Schedule> scheduleCache) {
		this.scheduleCache = scheduleCache;
	}

	public List<Course> getCourseCache() {
		return courseCache;
	}

	public void setCourseCache(List<Course> courseCache) {
		this.courseCache = courseCache;
	}

	public List<Teacher> getTeacherCache() {
		return teacherCache;
	}

	public void setTeacherCache(List<Teacher> teacherCache) {
		this.teacherCache = teacherCache;
	}

	public List<TimeStamp> getTimeStampCache() {
		return timeStampCache;
	}

	public void setTimeStampCache(List<TimeStamp> timeStampCache) {
		this.timeStampCache = timeStampCache;
	}

	public List<Semester> getSemesterCache() {
		return semesterCache;
	}

	public void setSemesterCache(List<Semester> semesterCache) {
		this.semesterCache = semesterCache;
	}
}
