package com.college.studentmanagement.service;

import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.college.studentmanagement.entity.Teacher;
import com.college.studentmanagement.user.UserDto;

public interface TeacherService extends UserDetailsService {
	Teacher findByUserName(String userName);   // ✔ updated
	Teacher findByTeacherId(int id);
	void save(UserDto userDto);
	void save(Teacher teacher);
	List<Teacher> findAllTeachers();
	void deleteTeacherById(int id);
}
