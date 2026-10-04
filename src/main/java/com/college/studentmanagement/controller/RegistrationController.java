package com.college.studentmanagement.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.college.studentmanagement.dao.RoleDao;
import com.college.studentmanagement.entity.Role;
import com.college.studentmanagement.service.StudentService;
import com.college.studentmanagement.service.TeacherService;
import com.college.studentmanagement.user.UserDto;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private RoleDao roleDao;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/showRegistrationForm")
	public String showRegistrationForm(Model theModel) {
		theModel.addAttribute("userDto", new UserDto());
		return "registration/registration-form";
	}

	@PostMapping("/processRegistrationForm")
	public String processRegistrationForm(@Valid @ModelAttribute("userDto") UserDto user,
										  BindingResult theBindingResult,
										  @RequestParam(value="role") String roleName,
										  Model theModel) {
		if (theBindingResult.hasErrors()) {
			return "registration/registration-form";
		}

		String userName = user.getUserName();

		if (roleName.equals("ROLE_STUDENT")) {
			// if username already exists in db
			if (studentService.findByUserName(userName) != null) {   // ✔ updated
				theModel.addAttribute("userDto", new UserDto());
				theModel.addAttribute("registrationError", "User name already exists!");
				return "registration/registration-form";
			}

			Role role = roleDao.findByName(roleName);
			user.setRole(role.getName()); // assign role name string
			studentService.save(user);

		} else { // teacher role
			// if username already exists in db
			if (teacherService.findByUserName(userName) != null) {   // ✔ updated
				theModel.addAttribute("userDto", new UserDto());
				theModel.addAttribute("registrationError", "User name already exists!");
				return "registration/registration-form";
			}

			Role role = roleDao.findByName(roleName);
			user.setRole(role.getName()); // assign role name string
			teacherService.save(user);
		}

		return "registration/registration-confirmation";
	}
}
