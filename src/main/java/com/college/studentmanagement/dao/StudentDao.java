package com.college.studentmanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.college.studentmanagement.entity.Student;

public interface StudentDao extends JpaRepository<Student, Integer> {
	Student findByStudentName(String studentName); // auto query
}
