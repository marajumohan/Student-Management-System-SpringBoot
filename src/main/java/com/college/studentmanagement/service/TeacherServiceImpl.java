package com.college.studentmanagement.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.college.studentmanagement.dao.RoleDao;
import com.college.studentmanagement.dao.TeacherDao;
import com.college.studentmanagement.entity.Role;
import com.college.studentmanagement.entity.Teacher;
import com.college.studentmanagement.user.UserDto;

@Service
public class TeacherServiceImpl implements TeacherService {

	@Autowired
	private TeacherDao teacherDao;

	@Autowired
	private RoleDao roleDao;

	@Override
	@Transactional
	public Teacher findByTeacherName(String teacherName) {
		return teacherDao.findByTeacherName(teacherName);
	}

	@Override
	@Transactional
	public Teacher findByTeacherId(int id) {
		return teacherDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void save(UserDto userDto) {
		Teacher teacher = new Teacher();
		teacher.setUserName(userDto.getUserName());
		teacher.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
		teacher.setFirstName(userDto.getFirstName());
		teacher.setLastName(userDto.getLastName());
		teacher.setEmail(userDto.getEmail());

		// Fetch role safely from DB
		Role role = roleDao.findByName(userDto.getRole());
		if (role == null) {
			role = roleDao.findByName("ROLE_TEACHER"); // fallback default
		}
		teacher.setRole(role);

		teacherDao.save(teacher);
	}

	@Override
	@Transactional
	public void save(Teacher teacher) {
		if (teacher.getRole() == null) {
			Role defaultRole = roleDao.findByName("ROLE_TEACHER");
			teacher.setRole(defaultRole);
		}
		teacherDao.save(teacher);
	}

	@Override
	@Transactional
	public List<Teacher> findAllTeachers() {
		return teacherDao.findAll();
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Teacher teacher = teacherDao.findByTeacherName(username);
		if (teacher == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}

		Role role = teacher.getRole();
		if (role == null) {
			role = roleDao.findByName("ROLE_TEACHER");
			teacher.setRole(role);
		}

		Collection<Role> roles = new ArrayList<>();
		roles.add(role);

		return org.springframework.security.core.userdetails.User.builder()
				.username(teacher.getUserName())
				.password(teacher.getPassword())
				.authorities(mapRolesToAuthorities(roles))
				.build();
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream()
				.filter(Objects::nonNull)
				.map(role -> new SimpleGrantedAuthority(
						role.getName() != null ? role.getName() : "ROLE_TEACHER"
				))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteTeacherById(int id) {
		teacherDao.deleteById(id);
	}
}
