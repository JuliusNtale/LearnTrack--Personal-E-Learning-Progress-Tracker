package com.hextech.learntrack.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "lesson_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    @ToString.Exclude
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    @ToString.Exclude
    private Lesson lesson;

    @Column(nullable = false)
    private boolean completed = false;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Column(name = "time_spent_minutes", nullable = false)
    private int timeSpentMinutes = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    public User getUser() {
        return this.enrollment != null ? this.enrollment.getUser() : null;
    }

    @Transient
    public Course getCourse() {
        return this.lesson != null ? this.lesson.getModule().getCourse() : null;
    }

    @Version
    private int version;


    // Add this method to calculate completion percentage
    public double getCompletionPercentage() {
        // For simple binary completion (0% or 100%)
        return this.completed ? 100.0 : 0.0;
    }
}