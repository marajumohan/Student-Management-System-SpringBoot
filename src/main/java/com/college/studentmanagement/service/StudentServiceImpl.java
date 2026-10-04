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
import com.college.studentmanagement.dao.StudentDao;
import com.college.studentmanagement.entity.Role;
import com.college.studentmanagement.entity.Student;
import com.college.studentmanagement.user.UserDto;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentDao studentDao;

	@Autowired
	private RoleDao roleDao;

	@Override
	@Transactional
	public Student findByUserName(String userName) {   // ✔ updated method name
		return studentDao.findByUserName(userName);
	}

	@Override
	@Transactional
	public Student findByStudentId(int id) {
		return studentDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void save(UserDto userDto) {
		Student student = new Student();
		student.setUserName(userDto.getUserName());
		student.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
		student.setFirstName(userDto.getFirstName());
		student.setLastName(userDto.getLastName());
		student.setEmail(userDto.getEmail());

		Role role = roleDao.findByName(userDto.getRole());
		if (role == null) {
			role = roleDao.findByName("ROLE_STUDENT"); // fallback default
		}
		student.setRole(role);

		studentDao.save(student);
	}

	@Override
	@Transactional
	public void save(Student student) {
		if (student.getRole() == null) {
			Role defaultRole = roleDao.findByName("ROLE_STUDENT");
			student.setRole(defaultRole);
		}
		studentDao.save(student);
	}

	@Override
	@Transactional
	public List<Student> findAllStudents() {
		return studentDao.findAll();
	}

	@Override
	@Transactional
	public void deleteById(int id) {
		studentDao.deleteById(id);
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Student student = studentDao.findByUserName(username); // ✔ updated
		if (student == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}

		Role role = student.getRole();
		if (role == null) {
			role = roleDao.findByName("ROLE_STUDENT");
			student.setRole(role);
		}

		Collection<Role> roles = new ArrayList<>();
		roles.add(role);

		return org.springframework.security.core.userdetails.User.builder()
				.username(student.getUserName())
				.password(student.getPassword())
				.authorities(mapRolesToAuthorities(roles))
				.build();
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream()
				.filter(Objects::nonNull)
				.map(role -> new SimpleGrantedAuthority(
						role.getName() != null ? role.getName() : "ROLE_STUDENT"
				))
				.collect(Collectors.toList());
	}
}
