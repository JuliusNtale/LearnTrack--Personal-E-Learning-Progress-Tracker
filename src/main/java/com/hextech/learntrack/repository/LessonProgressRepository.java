package com.hextech.learntrack.repository;

import com.hextech.learntrack.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    // Fixed: Added proper relationship path through enrollment
    Optional<LessonProgress> findByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);

    long countByEnrollmentAndCompleted(Enrollment enrollment, boolean completed);

    @Query("SELECT COUNT(lp) FROM LessonProgress lp WHERE lp.enrollment IN :enrollments AND lp.completed = :completed")
    long countByEnrollmentsAndCompleted(@Param("enrollments") List<Enrollment> enrollments,
                                        @Param("completed") boolean completed);

    // Fixed: Added proper joins for navigation
    @Query("SELECT lp.lesson.module.course.title, lp.lesson.title, lp.completionDate, lp.timeSpentMinutes " +
            "FROM LessonProgress lp " +
            "JOIN lp.enrollment e " +
            "WHERE e.user = :user AND lp.completed = true " +
            "ORDER BY lp.completionDate DESC")
    List<Object[]> findRecentActivitiesByUser(@Param("user") User user, Pageable pageable);

    // Fixed: Added join for user relationship
    @Query("SELECT CAST(lp.completionDate AS date), SUM(lp.timeSpentMinutes) " +
            "FROM LessonProgress lp " +
            "JOIN lp.enrollment e " +
            "WHERE e.user = :user AND CAST(lp.completionDate AS date) BETWEEN :start AND :end " +
            "GROUP BY CAST(lp.completionDate AS date)")
    List<Object[]> findWeeklyTimeSpent(@Param("user") User user,
                                       @Param("start") LocalDate start,
                                       @Param("end") LocalDate end);

    @Query("SELECT SUM(lp.timeSpentMinutes) FROM LessonProgress lp WHERE lp.enrollment = :enrollment")
    Integer sumTimeSpentByEnrollment(@Param("enrollment") Enrollment enrollment);

    @Modifying
    @Query("UPDATE LessonProgress lp SET lp.timeSpentMinutes = :minutes WHERE lp.id = :id")
    int updateTimeSpent(@Param("id") Long id, @Param("minutes") int minutes);

    @Query("SELECT COALESCE(AVG(lp.timeSpentMinutes), 0) FROM LessonProgress lp WHERE lp.lesson.id = :lessonId")
    double findAverageTimeSpentForLesson(@Param("lessonId") Long lessonId);

    // Fixed: Changed to use enrollment.user instead of direct user
    @Query("SELECT lp FROM LessonProgress lp JOIN lp.enrollment e WHERE lp.completed = false AND e.user.id = :userId")
    List<LessonProgress> findIncompleteLessons(@Param("userId") Long userId);

    // In LessonProgressRepository.java
    @Query("SELECT lp FROM LessonProgress lp JOIN FETCH lp.lesson JOIN FETCH lp.enrollment e " +
            "WHERE e.user = :user AND lp.lesson.module.course = :course")
    List<LessonProgress> findByUserAndCourse(@Param("user") User user, @Param("course") Course course);}