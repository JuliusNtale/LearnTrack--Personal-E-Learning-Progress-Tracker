package com.hextech.learntrack.service;

import com.hextech.learntrack.exception.ProgressTrackingException;
import com.hextech.learntrack.model.*;
import com.hextech.learntrack.repository.LessonProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProgressService {
    private final LessonProgressRepository lessonProgressRepository;
    private final EnrollmentService enrollmentService;
    private final LessonService lessonService;
    private final CourseService courseService;

    @Autowired
    public ProgressService(LessonProgressRepository lessonProgressRepository,
                           EnrollmentService enrollmentService,
                           LessonService lessonService,
                           CourseService courseService) {
        this.lessonProgressRepository = lessonProgressRepository;
        this.enrollmentService = enrollmentService;
        this.lessonService = lessonService;
        this.courseService = courseService;
    }

    public LessonProgress markLessonAsCompleted(User user, Long lessonId, int timeSpentMinutes) {
        validateProgressInput(user, lessonId, timeSpentMinutes);

        Lesson lesson = lessonService.getLessonById(lessonId)
                .orElseThrow(() -> new ProgressTrackingException("Lesson not found with ID: " + lessonId));

        Course course = lesson.getModule().getCourse();
        Enrollment enrollment = getEnrollment(user, course);

        return updateOrCreateProgress(enrollment, lesson, timeSpentMinutes);
    }

    public Map<String, Object> getUserProgressStats(User user) {
        Map<String, Object> stats = new HashMap<>();

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUser(user);
        long totalCompleted = lessonProgressRepository.countByEnrollmentsAndCompleted(enrollments, true);

        double avgCompletion = enrollments.stream()
                .mapToDouble(Enrollment::getProgress)
                .average()
                .orElse(0.0);

        long coursesInProgress = enrollments.stream()
                .filter(e -> e.getProgress() > 0 && e.getProgress() < 100)
                .count();

        stats.put("totalCompleted", totalCompleted);
        stats.put("avgCompletion", Math.round(avgCompletion));
        stats.put("coursesInProgress", coursesInProgress);
        stats.put("completedPercentage", calculateOverallCompletionPercentage(user));

        return stats;
    }

    public Map<String, Object> getWeeklyTimeSpent(User user) {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(6);

        List<Object[]> dailyMinutes = lessonProgressRepository.findWeeklyTimeSpent(user, weekStart, today);

        Map<LocalDate, Integer> timeMap = dailyMinutes.stream()
                .collect(Collectors.toMap(
                        d -> (LocalDate) d[0],  // First element is now properly cast to LocalDate
                        d -> ((Number) d[1]).intValue()
                ));

        List<String> days = new ArrayList<>();
        List<Integer> minutes = new ArrayList<>();
        int totalMinutes = 0;

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dayName = date.getDayOfWeek().toString().substring(0, 3);
            days.add(dayName);

            int dayMinutes = timeMap.getOrDefault(date, 0);
            minutes.add(dayMinutes);
            totalMinutes += dayMinutes;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("days", days);
        result.put("minutes", minutes);
        result.put("totalHours", totalMinutes / 60);

        return result;
    }

    public Map<String, Object> getTimeDistributionByCourse(User user) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUser(user);

        Map<String, Integer> timeByCourse = new HashMap<>();
        for (Enrollment enrollment : enrollments) {
            Integer timeSpent = lessonProgressRepository.sumTimeSpentByEnrollment(enrollment);
            if (timeSpent != null) {
                timeByCourse.put(enrollment.getCourse().getTitle(), timeSpent);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("labels", new ArrayList<>(timeByCourse.keySet()));
        result.put("data", new ArrayList<>(timeByCourse.values()));

        return result;
    }

    public List<Map<String, Object>> getRecentLearningActivities(User user, int limit) {
        // Use org.springframework.data.domain.Pageable
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> activities = lessonProgressRepository.findRecentActivitiesByUser(user, pageable);

        return activities.stream()
                .map(activity -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("courseTitle", activity[0]);
                    map.put("lessonTitle", activity[1]);
                    map.put("date", activity[2]);
                    map.put("timeSpent", activity[3]);
                    map.put("description", "Completed: " + activity[1]);
                    return map;
                })
                .collect(Collectors.toList());
    }

    private double calculateOverallCompletionPercentage(User user) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUser(user);
        if (enrollments.isEmpty()) return 0.0;

        double totalLessons = enrollments.stream()
                .mapToDouble(e -> e.getCourse().getModules().stream()
                        .mapToLong(m -> m.getLessons().size())
                        .sum())
                .sum();

        if (totalLessons == 0) return 0.0;

        long completedLessons = lessonProgressRepository.countByEnrollmentsAndCompleted(enrollments, true);
        return Math.round((completedLessons / totalLessons) * 100);
    }

    private Enrollment getEnrollment(User user, Course course) {
        return enrollmentService.getEnrollmentByUserAndCourse(user, course)
                .orElseThrow(() -> new ProgressTrackingException(
                        "User is not enrolled in course: " + course.getTitle()));
    }

    private LessonProgress updateOrCreateProgress(Enrollment enrollment, Lesson lesson, int timeSpentMinutes) {
        return lessonProgressRepository.findByEnrollmentAndLesson(enrollment, lesson)
                .map(existing -> updateProgress(existing, timeSpentMinutes))
                .orElseGet(() -> createNewProgress(enrollment, lesson, timeSpentMinutes));
    }

    private LessonProgress updateProgress(LessonProgress progress, int timeSpentMinutes) {
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

    private void validateProgressInput(User user, Long lessonId, int timeSpentMinutes) {
        if (user == null || lessonId == null) {
            throw new IllegalArgumentException("User and lesson ID cannot be null");
        }
        if (timeSpentMinutes < 0) {
            throw new IllegalArgumentException("Time spent cannot be negative");
        }
    }

    public Map<Long, Double> getLessonProgress(User user, Course course) {
        List<LessonProgress> progresses = lessonProgressRepository.findByUserAndCourse(user, course);

        return progresses.stream()
                .collect(Collectors.toMap(
                        progress -> progress.getLesson().getId(),
                        progress -> {
                            // Calculate completion percentage
                            if (progress.isCompleted()) {
                                return 100.0;
                            }
                            // Optional: Add logic for partial completion if needed
                            return 0.0;
                        }
                ));
    }
}