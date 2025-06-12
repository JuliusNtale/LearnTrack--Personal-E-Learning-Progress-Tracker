// AssignmentRepository.java
package com.hextech.learntrack.repository;

import com.hextech.learntrack.model.Course;
import com.hextech.learntrack.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByCourse(Course course);
}