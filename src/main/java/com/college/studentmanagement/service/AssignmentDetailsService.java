package com.college.studentmanagement.service;

import com.college.studentmanagement.entity.AssignmentDetails;

public interface AssignmentDetailsService {
	
	public AssignmentDetails findByAssignmentAndStudentCourseDetailsId(int assignmentId, int studentCourseDetailsId);
	
	public void save(AssignmentDetails studentCourseAssignmentDetails);
}
