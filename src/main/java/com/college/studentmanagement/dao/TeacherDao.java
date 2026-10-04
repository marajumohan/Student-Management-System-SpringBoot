package com.college.studentmanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.college.studentmanagement.entity.Teacher;

public interface TeacherDao extends JpaRepository<Teacher, Integer> {
	Teacher findByTeacherName(String teacherName); // auto query
}
