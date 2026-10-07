package com.college.studentmanagement.dao;

import com.college.studentmanagement.entity.Role;

public interface RoleDao {
	
	public Role findRoleByName(String theRoleName);
	
	public Role save(Role theRole);
	
	//assigns the given role to the teacher/student rows that have no role yet
	public int assignRoleWhereMissing(String entityName, Role theRole);
}
