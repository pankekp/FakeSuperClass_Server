package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pojo.Course;
import pojo.Schedule;
import pojo.Semester;
import pojo.Teacher;
import pojo.TimeStamp;
import utils.DButil;

public class ManagerDaoImpl {

	
	public int addTeacher(Teacher teacher) {
		int rs = 0;
		Connection con = DButil.getConn();
		String sql = "insert into Teachers values(?,?,?)";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, teacher.getTeacherId());
			pst.setString(2, teacher.getTeacherName());
			pst.setBoolean(3, false);
			rs = pst.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} finally {
			DButil.close(con, pst, null);
		}
		return rs;
	}

	
	public int addSchedule(Schedule schedule) {
		int rs = 0;
		Connection con = DButil.getConn();
		PreparedStatement pst = null;
		String sql = "insert into Schedules(Semester,ClassNo,Classes,PeopleNum,Time,Location,TeacherId,CourseId) values(?,?,?,?,?,?,?,?)";
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, schedule.getSemesterId());
			pst.setString(2, schedule.getClassNo());
			pst.setString(3, schedule.getClasses());
			pst.setString(4, schedule.getPeopleNum());
			pst.setString(5, schedule.getTime());
			pst.setString(6, schedule.getLocation());
			pst.setString(7, schedule.getTeacherId());
			pst.setString(8, schedule.getCourseId());
			rs = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			DButil.close(con, pst, null);
		}
		return rs;
	}

	
	public int addCourse(Course course) {
		int rs = 0;
		Connection con = DButil.getConn();
		String sql = "insert into Courses(CourseId,CourseName,Credit,Method,Type) values(?,?,?,?,?)";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, course.getCourseId());
			pst.setString(2, course.getCourseName());
			pst.setString(3, course.getCredit());
			pst.setString(4, course.getMethod());
			pst.setString(5, course.getType());
			rs = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			DButil.close(con, pst, null);
		}
		return rs;
	}

	/**
	 * @param name
	 *            a teacher's name
	 * @return a list of teachers' ids
	 */
	
	public List<String> findTeacherId(String name) {
		List<String> TeacherIds = new ArrayList<>();
		Connection con = DButil.getConn();
		String sql = "select TeacherId from Teachers where Name=?";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, name);
			rs = pst.executeQuery();
			while (rs.next()) {
				String id = rs.getString("TeacherId");
				TeacherIds.add(id);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			DButil.close(con, pst, rs);
		}
		return TeacherIds;
	}

	/**
	 * @param TeacherId
	 *            a teacher's id
	 * @return whether there is a schedule in the table Schedules
	 *//*
		 *  public boolean hasSchedules(String TeacherId) { boolean
		 * has=false; Connection con=DButil.getConn(); String
		 * sql="select ScheduleId from Schedules where TeacherId=?"; try {
		 * PreparedStatement pst=con.prepareStatement(sql); pst.setString(1,
		 * TeacherId); ResultSet rs=pst.executeQuery(); while(rs.next()){
		 * has=true; } DButil.close(con, pst, rs); } catch (SQLException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); } return has; }
		 */

	/**
	 * @param TeacherId
	 *            a teacher's id
	 * @return null when list is empty
	 * @return list when find schedules
	 */
	
	public List<Schedule> findSchedules(String TeacherId, String Semester) {
		ArrayList<Schedule> Schedules=new ArrayList<Schedule>();
		Connection con = DButil.getConn();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select ScheduleId,Semester,ClassNo,Classes,PeopleNum,Time,Location,CourseId from schedules where TeacherId=? and Semester=?";
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, TeacherId);
			pst.setString(2, Semester);
			rs = pst.executeQuery();
			//System.out.println("findCOurse" + TeacherId + "....." + Semester);
			while (rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setTeacherId(TeacherId);
				schedule.setScheduleId(rs.getInt("ScheduleId"));
				schedule.setSemesterId(rs.getString("Semester"));
				schedule.setClassNo(rs.getString("ClassNo"));
				schedule.setClasses(rs.getString("Classes"));
				schedule.setPeopleNum(rs.getString("PeopleNum"));
				schedule.setTime(rs.getString("Time"));
				schedule.setLocation(rs.getString("Location"));
				schedule.setCourseId(rs.getString("CourseId"));
				Schedules.add(schedule);
				//System.out.println(schedule.getTeacherId() + "......");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DButil.close(con, pst, rs);
		}
		if (Schedules.size()==0) {
			return null;
		} else {
			return Schedules;
		}
	}

	
	public Course findCourses(String CourseId) {
		Course course = new Course();
		Connection con = DButil.getConn();
		String sql = "select CourseName,Credit,Method,Type from Courses where CourseId=?";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, CourseId);
			rs = pst.executeQuery();
			while (rs.next()) {
				course.setCourseId(CourseId);
				course.setCourseName(rs.getString("CourseName"));
				course.setCredit(rs.getString("Method"));
				course.setMethod(rs.getString("Method"));
				course.setType(rs.getString("Type"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DButil.close(con, pst, rs);
		}
		return course;
	}

	
	public List<Teacher> findAllTeachers() {
		ArrayList<Teacher> resTeachers = new ArrayList<Teacher>();
		Connection con = DButil.getConn();
		String sql = "select * from Teachers";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				Teacher teacher = new Teacher();
				teacher.setTeacherId(rs.getString("TeacherId"));
				teacher.setTeacherName(rs.getString("Name"));
				teacher.setCached(rs.getBoolean("Cached"));
				resTeachers.add(teacher);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DButil.close(con, pst, rs);
		}
		return resTeachers;
	}

	
	public List<Schedule> findAllSchedules() {
		List<Schedule> resSchedules = new ArrayList<Schedule>();
		Connection con = DButil.getConn();
		String sql = "select * from schedules";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setClasses(rs.getString("Classes"));
				schedule.setClassNo(rs.getString("ClassNo"));
				schedule.setCourseId(rs.getString("CourseId"));
				schedule.setLocation(rs.getString("Location"));
				schedule.setPeopleNum(rs.getString("PeopleNum"));
				schedule.setScheduleId(rs.getInt("ScheduleId"));
				schedule.setSemesterId(rs.getString("Semester"));
				schedule.setTeacherId(rs.getString("TeacherId"));
				schedule.setTime(rs.getString("Time"));
				resSchedules.add(schedule);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DButil.close(con, pst, rs);
		}
		return resSchedules;
	}

	
	public List<Course> findAllCourses() {
		List<Course> resCourses = new ArrayList<Course>(); 
		Connection con = DButil.getConn();
		String sql = "select * from Courses";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				Course course = new Course();
				course.setCourseId(rs.getString("CourseId"));
				course.setCourseName("CourseName");
				course.setCredit("Credit");
				course.setMethod("Method");
				course.setType("Type");
				resCourses.add(course);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DButil.close(con, pst, rs);
		}
		return resCourses;
	}

	
	public int addSemester(Semester semester) {
		int rs = 0;
		Connection con = DButil.getConn();
		String sql = "insert into Semesters(SemesterId,SemesterName) values(?,?)";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, semester.getSemesterId());
			pst.setString(2, semester.getSemesterName());
			rs = pst.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}finally{
			DButil.close(con, pst, null);
		}
		return rs;
	}

	
	public List<Semester> findAllSemesters() {
		List<Semester> resSemesters = new ArrayList<Semester>();
		Connection con = DButil.getConn();
		String sql = "select SemesterId,SemesterName from Semesters";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				Semester semester = new Semester();
				semester.setSemesterId(rs.getString("SemesterId"));
				semester.setSemesterName(rs.getString("SemesterName"));
				resSemesters.add(semester);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DButil.close(con, pst, rs);
		}
		return resSemesters;
	}

	
	public int addTimeStamp(TimeStamp timeStamp) {
		int rs = 0;
		Connection con = DButil.getConn();
		String sql = "insert into TimeStamp(TimeStampId,timeStamp) values(?,?)";
		PreparedStatement pst =null;
		try {
			pst = con.prepareStatement(sql);
			pst.setInt(1, timeStamp.getTimeStampId());
			pst.setLong(2, timeStamp.getTimeStampSecond());
			rs = pst.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DButil.close(con, pst, null);
		}
		return rs;
	}

	
	public TimeStamp findNewTimeStamp() {
		TimeStamp timeStamp = new TimeStamp();
		String sql = "select TimeStampId,TimeStamp from TimeStamp";

		Connection con = DButil.getConn();
		PreparedStatement pst =null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				if (timeStamp.getTimeStampId() < rs.getInt("TimeStampId")) {
					timeStamp.setTimeStampId(rs.getInt("TimeStampId"));
					timeStamp.setTimeStampSecond(rs.getLong("TimeStamp"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DButil.close(con, pst, rs);
		}
		return timeStamp;
	}

}
