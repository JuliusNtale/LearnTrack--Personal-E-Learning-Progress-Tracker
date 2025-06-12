package com.hextech.learntrack.service;

import com.hextech.learntrack.model.*;
import com.hextech.learntrack.repository.AssignmentSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (user == null || assignment == null) {
            throw new IllegalArgumentException("User and Assignment cannot be null");
        }

        return submissionRepository.findByUserAndAssignment(user, assignment)
                .map(existingSubmission -> {
                    existingSubmission.setFilePath(filePath);
                    existingSubmission.setComments(comments);
                    existingSubmission.setSubmissionDate(new Date());
                    return submissionRepository.save(existingSubmission);
                })
                .orElseGet(() -> {
                    AssignmentSubmission newSubmission = new AssignmentSubmission();
                    newSubmission.setUser(user);
                    newSubmission.setAssignment(assignment);
                    newSubmission.setFilePath(filePath);
                    newSubmission.setComments(comments);
                    newSubmission.setSubmissionDate(new Date());
                    return submissionRepository.save(newSubmission);
                });
    }

    public AssignmentSubmission gradeSubmission(Long submissionId, Integer grade, String feedback) {
        if (submissionId == null) {
            throw new IllegalArgumentException("Submission ID cannot be null");
        }

        AssignmentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + submissionId));

        if (grade != null && grade < 0) {
            throw new IllegalArgumentException("Grade cannot be negative");
        }

        submission.setGrade(grade);
        submission.setFeedback(feedback);
        return submissionRepository.save(submission);
    }

    @Transactional(readOnly = true)
    public List<AssignmentSubmission> getSubmissionsByUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return submissionRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<AssignmentSubmission> getSubmissionsByAssignment(Assignment assignment) {
        if (assignment == null) {
            throw new IllegalArgumentException("Assignment cannot be null");
        }
        return submissionRepository.findByAssignment(assignment);
    }

    @Transactional(readOnly = true)
    public Optional<AssignmentSubmission> getSubmissionByUserAndAssignment(User user, Assignment assignment) {
        if (user == null || assignment == null) {
            throw new IllegalArgumentException("User and Assignment cannot be null");
        }
        return submissionRepository.findByUserAndAssignment(user, assignment);
    }

    @Transactional(readOnly = true)
    public Optional<AssignmentSubmission> getSubmissionById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return submissionRepository.findById(id);
    }

    public void deleteSubmission(Long submissionId) {
        if (submissionId == null) {
            throw new IllegalArgumentException("Submission ID cannot be null");
        }
        submissionRepository.deleteById(submissionId);
    }
}