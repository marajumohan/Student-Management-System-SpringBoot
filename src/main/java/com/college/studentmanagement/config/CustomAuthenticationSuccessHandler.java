package com.college.studentmanagement.config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.college.studentmanagement.entity.Student;
import com.college.studentmanagement.entity.Teacher;
import com.college.studentmanagement.service.StudentService;
import com.college.studentmanagement.service.TeacherService;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private StudentService studentService;

	@Autowired
	private TeacherService teacherService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
										HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {

		String redirectUrl = request.getContextPath() + "/";

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority authority : authorities) {
			String role = authority.getAuthority();

			if ("ROLE_STUDENT".equals(role)) {
				String userName = authentication.getName();
				Student student = studentService.findByStudentName(userName);

				if (student != null) {
					HttpSession session = request.getSession();
					session.setAttribute("user", student);
					redirectUrl = request.getContextPath() + "/student/" + student.getId() + "/courses";
				} else {
					redirectUrl = request.getContextPath() + "/student/home";
				}
				break;

			} else if ("ROLE_TEACHER".equals(role)) {
				String userName = authentication.getName();
				Teacher teacher = teacherService.findByTeacherName(userName);

				if (teacher != null) {
					HttpSession session = request.getSession();
					session.setAttribute("user", teacher);
					redirectUrl = request.getContextPath() + "/teacher/" + teacher.getId() + "/courses";
				} else {
					redirectUrl = request.getContextPath() + "/teacher/home";
				}
				break;

			} else if ("ROLE_ADMIN".equals(role)) {
				redirectUrl = request.getContextPath() + "/admin/adminPanel";
				break;
			}
		}

		response.sendRedirect(redirectUrl);
	}
}
