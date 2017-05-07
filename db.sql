create table TimeStamp(TimeStampId int AUTO_INCREMENT primary key not null,
						timeStamp bigint);
insert into TimeStamp(timeStamp) values(1490767891969);
select * from TimeStamp;

create table Semesters(SemesterId varchar(20) primary key,
						SemesterName varchar(20));
insert into Semesters values("2016-1","20161");
select * from Semesters;

create table Teachers(TeacherId varchar(20) primary key,
						Name varchar(20),Cached boolean);
insert into Teachers values("0000138","zhangsan",false);
insert into Teachers values("0000168","李金霞",false);
insert into Teachers values("0000155","caoying",false);

select * from Teachers;
select * from Teachers where TeacherId='501';

create table Courses(CourseId varchar(20) primary key,
						CourseName varchar(20),
						Credit varchar(20),
						Method varchar(20),
						Type varchar(20));
insert into Courses(CourseId,CourseName,Credit,Method,Type) values("370004","医学试验","1.5","讲授","公共课");

select * from Courses;

create table Schedules(ScheduleId int AUTO_INCREMENT primary key not null,
						Semester varchar(20),
						ClassNo varchar(20),
						Classes varchar(20),
						PeopleNum varchar(20),
						Time varchar(20),
						Location varchar(20),
						TeacherId varchar(20),
						CourseId varchar(20));
						
insert into Schedules(Semester,ClassNo,Classes,PeopleNum,Time,Location,TeacherId,CourseId) values("2016-2017学年第二学期","002","某班级","45","5-12周 四[9-11节]","教学楼205","0000138","370004");
select * from Schedules;

drop table Teachers;
drop table Courses;
drop table Schedules;
drop table Semesters;
drop table TimeStamp;

select * from schedules where TeacherId='0000168';