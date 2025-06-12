// AssignmentSubmission.java
package com.hextech.learntrack.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "assignment_submissions")
public class AssignmentSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date submissionDate = new Date();

    @Column
    private String filePath;

    @Column(length = 2000)
    private String comments;

    @Column
    private Integer grade;

    @Column(length = 1000)
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;
}