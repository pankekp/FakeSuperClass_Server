package pojo;

/**
 * @author Zhao Zhixu
 */
public class Teacher {
	String teacherId;
	String teacherName;
	boolean Cached;

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public boolean isCached() {
		return Cached;
	}

	public void setCached(boolean cached) {
		Cached = cached;
	}

	

}
