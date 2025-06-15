package com.hextech.learntrack.repository;

import com.hextech.learntrack.model.Assignment;
import com.hextech.learntrack.model.User;
import com.hextech.learntrack.model.AssignmentSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {
    List<AssignmentSubmission> findByUser(User user);
    List<AssignmentSubmission> findByAssignment(Assignment assignment);
    Optional<AssignmentSubmission> findByUserAndAssignment(User user, Assignment assignment);

    @Query("SELECT s FROM AssignmentSubmission s WHERE s.user = :user ORDER BY s.submissionDate DESC")
    List<AssignmentSubmission> findRecentByUser(@Param("user") User user, Pageable pageable);

    default List<AssignmentSubmission> getRecentSubmissionsByUser(User user, int limit) {
        return findRecentByUser(user, PageRequest.of(0, limit));
    }

    Page<AssignmentSubmission> findByAssignmentOrderBySubmissionDateDesc(Assignment assignment, Pageable pageable);
}