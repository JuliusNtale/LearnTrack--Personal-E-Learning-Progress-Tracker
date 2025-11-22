package com.hextech.learntrack.service;

import com.hextech.learntrack.model.User;
import com.hextech.learntrack.model.Course;
import com.hextech.learntrack.model.Enrollment;
import com.hextech.learntrack.repository.UserRepository;
import com.hextech.learntrack.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProgressService progressService;

    /**
     * Check for users who haven't studied in 3+ days and send reminder emails
     * Runs daily at 9 AM
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendStudyReminders() {
        List<User> activeUsers = userRepository.findAll().stream()
                .filter(user -> "STUDENT".equals(user.getRole()) && user.isEnabled())
                .toList();

        for (User user : activeUsers) {
            checkAndSendStudyReminders(user);
        }
    }

    private void checkAndSendStudyReminders(User user) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUser(user);
        
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getProgress() < 100) { // Course not completed
                // Check last activity date
                LocalDateTime lastActivity = progressService.getLastActivityDate(user, enrollment.getCourse());
                
                if (lastActivity != null) {
                    long daysSinceLastActivity = ChronoUnit.DAYS.between(lastActivity, LocalDateTime.now());
                    
                    if (daysSinceLastActivity >= 3) {
                        emailService.sendStudyReminderEmail(
                            user.getEmail(),
                            enrollment.getCourse().getTitle(),
                            (int) daysSinceLastActivity
                        );
                    }
                }
            }
        }
    }

    /**
     * Send course completion notification
     */
    public void sendCourseCompletionNotification(User user, Course course) {
        emailService.sendCourseCompletionEmail(user.getEmail(), course.getTitle());
    }

    /**
     * Send milestone achievement notification
     */
    public void sendMilestoneNotification(User user, String milestone) {
        emailService.sendMilestoneAchievementEmail(user.getEmail(), milestone);
    }

    /**
     * Check and send milestone notifications based on study time or course completions
     */
    public void checkAndSendMilestoneNotifications(User user) {
        // Check total study time milestones
        int totalMinutesStudied = progressService.getTotalStudyTime(user);
        int hoursStudied = totalMinutesStudied / 60;

        // Send milestone notifications for study time
        if (hoursStudied == 10 && !hasSentMilestone(user, "10_HOURS")) {
            sendMilestoneNotification(user, "10 Hours of Study Time!");
            markMilestoneSent(user, "10_HOURS");
        } else if (hoursStudied == 50 && !hasSentMilestone(user, "50_HOURS")) {
            sendMilestoneNotification(user, "50 Hours of Study Time!");
            markMilestoneSent(user, "50_HOURS");
        } else if (hoursStudied == 100 && !hasSentMilestone(user, "100_HOURS")) {
            sendMilestoneNotification(user, "100 Hours of Study Time!");
            markMilestoneSent(user, "100_HOURS");
        }

        // Check course completion milestones
        int completedCourses = progressService.getCompletedCoursesCount(user);
        if (completedCourses == 1 && !hasSentMilestone(user, "FIRST_COURSE")) {
            sendMilestoneNotification(user, "First Course Completed!");
            markMilestoneSent(user, "FIRST_COURSE");
        } else if (completedCourses == 5 && !hasSentMilestone(user, "FIVE_COURSES")) {
            sendMilestoneNotification(user, "5 Courses Completed!");
            markMilestoneSent(user, "FIVE_COURSES");
        } else if (completedCourses == 10 && !hasSentMilestone(user, "TEN_COURSES")) {
            sendMilestoneNotification(user, "10 Courses Completed!");
            markMilestoneSent(user, "TEN_COURSES");
        }
    }

    private boolean hasSentMilestone(User user, String milestone) {
        // This would check a milestone tracking table
        // For now, return false to always send (you can implement proper tracking)
        return false;
    }

    private void markMilestoneSent(User user, String milestone) {
        // This would mark the milestone as sent in a tracking table
        // Implementation depends on whether you want to create a separate entity
    }

    @Autowired
    private EnrollmentService enrollmentService;
}
