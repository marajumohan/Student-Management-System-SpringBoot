package com.college.studentmanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.college.studentmanagement.entity.Role;

public interface RoleDao extends JpaRepository<Role, Integer> {
	Role findByName(String name); // auto-implemented by Spring Data JPA
}
