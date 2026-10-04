package com.college.studentmanagement.service;

import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.college.studentmanagement.entity.Student;
import com.college.studentmanagement.user.UserDto;

public interface StudentService extends UserDetailsService {
	Student findByUserName(String userName);   // ✔ updated
	Student findByStudentId(int id);
	void save(UserDto userDto);
	void save(Student student);
	List<Student> findAllStudents();
	void deleteById(int id);
}
