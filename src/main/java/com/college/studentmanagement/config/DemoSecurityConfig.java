package com.college.studentmanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.college.studentmanagement.service.StudentService;
import com.college.studentmanagement.service.TeacherService;

@Configuration
@EnableWebSecurity
public class DemoSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private StudentService studentService;

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Student and teacher credentials from DB
		auth.userDetailsService(studentService).passwordEncoder(passwordEncoder());
		auth.userDetailsService(teacherService).passwordEncoder(passwordEncoder());

		// Admin credentials in-memory
		auth.inMemoryAuthentication()
				.withUser("admin")
				.password(passwordEncoder().encode("1"))
				.roles("ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				// Public pages
				.antMatchers("/showLoginPage", "/register", "/css/**", "/js/**").permitAll()

				// Role-based restrictions
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/student/**").hasRole("STUDENT")
				.antMatchers("/teacher/**").hasRole("TEACHER")

				// Any other request must be authenticated
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/showLoginPage")
				.loginProcessingUrl("/authenticateTheUser")
				.successHandler(customAuthenticationSuccessHandler)
				.permitAll()
				.and()
				.logout().permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/access-denied");
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
