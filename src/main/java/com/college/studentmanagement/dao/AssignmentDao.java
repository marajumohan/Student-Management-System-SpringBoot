package com.college.studentmanagement.dao;

import com.college.studentmanagement.entity.Assignment;

public interface AssignmentDao {
	
	public void save(Assignment assignment);
	
	public void deleteAssignmentById(int id);
}
