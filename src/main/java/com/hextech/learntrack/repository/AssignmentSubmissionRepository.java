// AssignmentSubmissionRepository.java
package com.hextech.learntrack.repository;

import com.hextech.learntrack.model.Assignment;
import com.hextech.learntrack.model.User;
import com.hextech.learntrack.model.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {
    List<AssignmentSubmission> findByUser(User user);
    List<AssignmentSubmission> findByAssignment(Assignment assignment);
    Optional<AssignmentSubmission> findByUserAndAssignment(User user, Assignment assignment);
}