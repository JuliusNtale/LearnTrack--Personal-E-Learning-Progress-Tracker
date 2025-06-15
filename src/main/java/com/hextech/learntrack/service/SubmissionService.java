package com.hextech.learntrack.service;

import com.hextech.learntrack.model.*;
import com.hextech.learntrack.repository.AssignmentSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubmissionService {
    private final AssignmentSubmissionRepository submissionRepository;

    @Autowired
    public SubmissionService(AssignmentSubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public AssignmentSubmission submitAssignment(User user, Assignment assignment, String filePath, String comments) {
        validateSubmissionInput(user, assignment);

        return submissionRepository.findByUserAndAssignment(user, assignment)
                .map(existing -> updateSubmission(existing, filePath, comments))
                .orElseGet(() -> createNewSubmission(user, assignment, filePath, comments));
    }

    public List<AssignmentSubmission> getRecentSubmissionsByUser(User user, int limit) {
        return submissionRepository.getRecentSubmissionsByUser(user, limit);
    }

    public AssignmentSubmission gradeSubmission(Long submissionId, Integer grade, String feedback) {
        if (submissionId == null) {
            throw new IllegalArgumentException("Submission ID cannot be null");
        }

        AssignmentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + submissionId));

        if (grade != null && (grade < 0 || grade > submission.getAssignment().getMaxPoints())) {
            throw new IllegalArgumentException("Invalid grade value");
        }

        submission.setGrade(grade);
        submission.setFeedback(feedback);
        return submissionRepository.save(submission);
    }

    public Page<AssignmentSubmission> getSubmissionsByAssignment(Assignment assignment, Pageable pageable) {
        return submissionRepository.findByAssignmentOrderBySubmissionDateDesc(assignment, pageable);
    }

    public Optional<AssignmentSubmission> getSubmissionById(Long id) {
        return submissionRepository.findById(id);
    }

    public List<AssignmentSubmission> getSubmissionsByUser(User user) {
        return submissionRepository.findByUser(user);
    }

    public Optional<AssignmentSubmission> getSubmissionByUserAndAssignment(User user, Assignment assignment) {
        return submissionRepository.findByUserAndAssignment(user, assignment);
    }

    public void deleteSubmission(Long submissionId) {
        submissionRepository.deleteById(submissionId);
    }

    private AssignmentSubmission createNewSubmission(User user, Assignment assignment, String filePath, String comments) {
        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setUser(user);
        submission.setAssignment(assignment);
        submission.setFilePath(filePath);
        submission.setComments(comments);
        submission.setSubmissionDate(new Date());
        return submissionRepository.save(submission);
    }

    private AssignmentSubmission updateSubmission(AssignmentSubmission submission, String filePath, String comments) {
        submission.setFilePath(filePath);
        submission.setComments(comments);
        submission.setSubmissionDate(new Date());
        return submissionRepository.save(submission);
    }

    private void validateSubmissionInput(User user, Assignment assignment) {
        if (user == null || assignment == null) {
            throw new IllegalArgumentException("User and Assignment cannot be null");
        }
    }
}