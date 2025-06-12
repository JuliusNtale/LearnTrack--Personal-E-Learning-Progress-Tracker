package com.hextech.learntrack.repository;

import com.hextech.learntrack.model.Enrollment;
import com.hextech.learntrack.model.Lesson;
import com.hextech.learntrack.model.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {
    Optional<LessonProgress> findByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);

    long countByEnrollmentAndCompleted(Enrollment enrollment, boolean completed);

    @Modifying
    @Query("UPDATE LessonProgress lp SET lp.timeSpentMinutes = :minutes WHERE lp.id = :id")
    int updateTimeSpent(@Param("id") Long id, @Param("minutes") int minutes);

    @Query("SELECT COALESCE(AVG(lp.timeSpentMinutes), 0) FROM LessonProgress lp WHERE lp.lesson.id = :lessonId")
    double findAverageTimeSpentForLesson(@Param("lessonId") Long lessonId);
}