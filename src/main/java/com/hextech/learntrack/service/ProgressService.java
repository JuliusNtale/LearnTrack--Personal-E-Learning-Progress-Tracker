package com.hextech.learntrack.service;

import com.hextech.learntrack.exception.ProgressTrackingException;
import com.hextech.learntrack.model.*;
import com.hextech.learntrack.repository.LessonProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class ProgressService {
    private final LessonProgressRepository lessonProgressRepository;
    private final EnrollmentService enrollmentService;
    private final LessonService lessonService;

    @Autowired
    public ProgressService(LessonProgressRepository lessonProgressRepository,
                           EnrollmentService enrollmentService,
                           LessonService lessonService) {
        this.lessonProgressRepository = lessonProgressRepository;
        this.enrollmentService = enrollmentService;
        this.lessonService = lessonService;
    }

    public LessonProgress markLessonAsCompleted(User user, Long lessonId, int timeSpentMinutes) {
        if (user == null || lessonId == null) {
            throw new IllegalArgumentException("User and lesson ID cannot be null");
        }
        if (timeSpentMinutes < 0) {
            throw new IllegalArgumentException("Time spent cannot be negative");
        }

        Lesson lesson = lessonService.getLessonById(lessonId)
                .orElseThrow(() -> new ProgressTrackingException("Lesson not found with ID: " + lessonId));

        Course course = lesson.getModule().getCourse();
        Enrollment enrollment = enrollmentService.getEnrollmentByUserAndCourse(user, course)
                .orElseThrow(() -> new ProgressTrackingException(
                        "User is not enrolled in course: " + course.getTitle()));

        return lessonProgressRepository.findByEnrollmentAndLesson(enrollment, lesson)
                .map(existingProgress -> updateExistingProgress(existingProgress, timeSpentMinutes))
                .orElseGet(() -> createNewProgress(enrollment, lesson, timeSpentMinutes));
    }

    private LessonProgress updateExistingProgress(LessonProgress progress, int timeSpentMinutes) {
        progress.setCompleted(true);
        progress.setCompletionDate(LocalDateTime.now());
        progress.setTimeSpentMinutes(timeSpentMinutes);
        return lessonProgressRepository.save(progress);
    }

    private LessonProgress createNewProgress(Enrollment enrollment, Lesson lesson, int timeSpentMinutes) {
        LessonProgress progress = new LessonProgress();
        progress.setEnrollment(enrollment);
        progress.setLesson(lesson);
        progress.setCompleted(true);
        progress.setCompletionDate(LocalDateTime.now());
        progress.setTimeSpentMinutes(timeSpentMinutes);
        return lessonProgressRepository.save(progress);
    }

    @Transactional(readOnly = true)
    public double calculateCourseProgress(Enrollment enrollment) {
        if (enrollment == null) {
            throw new IllegalArgumentException("Enrollment cannot be null");
        }

        Course course = enrollment.getCourse();
        long totalLessons = course.getModules().stream()
                .flatMap(module -> module.getLessons().stream())
                .count();

        if (totalLessons == 0) {
            return 0.0;
        }

        long completedLessons = lessonProgressRepository.countByEnrollmentAndCompleted(enrollment, true);
        return (double) completedLessons / totalLessons * 100;
    }

    @Transactional(readOnly = true)
    public Optional<LessonProgress> getLessonProgress(User user, Long lessonId) {
        if (user == null || lessonId == null) {
            throw new IllegalArgumentException("User and lesson ID cannot be null");
        }

        Lesson lesson = lessonService.getLessonById(lessonId)
                .orElseThrow(() -> new ProgressTrackingException("Lesson not found"));

        return enrollmentService.getEnrollmentByUserAndCourse(user, lesson.getModule().getCourse())
                .flatMap(enrollment -> lessonProgressRepository.findByEnrollmentAndLesson(enrollment, lesson));
    }
}