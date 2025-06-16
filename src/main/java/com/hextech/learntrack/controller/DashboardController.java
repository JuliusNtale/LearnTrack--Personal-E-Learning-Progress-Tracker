package com.hextech.learntrack.controller;

import com.hextech.learntrack.model.User;
import com.hextech.learntrack.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DashboardController {
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final ProgressService progressService;
    private final SubmissionService submissionService;
    private final UserService userService;

    @Autowired
    public DashboardController(CourseService courseService,
                               EnrollmentService enrollmentService,
                               ProgressService progressService,
                               SubmissionService submissionService,
                               UserService userService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.progressService = progressService;
        this.submissionService = submissionService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(@AuthenticationPrincipal User user, Model model) {
        try {
            // Validate user
            if (user == null) {
                logger.warn("Unauthenticated access attempt to dashboard");
                return "redirect:/login";
            }

            // Add common attributes
            model.addAttribute("user", user);
            logger.info("Rendering dashboard for {}: {}", user.getRole(), user.getEmail());

            // Role-specific data loading
            if ("STUDENT".equals(user.getRole())) {
                loadStudentDashboardData(user, model);
            } else if ("ADMIN".equals(user.getRole())) {
                loadAdminDashboardData(user, model);

                // Add dummy/empty data for admin to avoid template errors
                model.addAttribute("timeSpent", Map.of(
                        "days", new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"},
                        "minutes", new int[]{0, 0, 0, 0, 0, 0, 0},
                        "totalHours", 0
                ));
                model.addAttribute("timeDistribution", Map.of(
                        "labels", new String[]{},
                        "data", new int[]{}
                ));
            } else {
                logger.warn("Unauthorized role access attempt: {}", user.getRole());
                return "redirect:/access-denied";
            }

            return "dashboard";

        } catch (Exception e) {
            logger.error("Error rendering dashboard for user {}", user != null ? user.getEmail() : "unknown", e);
            model.addAttribute("error", "Unable to load dashboard data");
            return "dashboard"; // Fallback to dashboard with error message
        }
    }

    private void loadStudentDashboardData(User user, Model model) {
        model.addAttribute("enrollments", enrollmentService.getEnrollmentsByUser(user));
        model.addAttribute("submissions", submissionService.getRecentSubmissionsByUser(user, 5));
        model.addAttribute("progressStats", progressService.getUserProgressStats(user));
        model.addAttribute("timeSpent", progressService.getWeeklyTimeSpent(user));
        model.addAttribute("timeDistribution", progressService.getTimeDistributionByCourse(user));
        model.addAttribute("recentActivities", progressService.getRecentLearningActivities(user, 5));
    }

    private void loadAdminDashboardData(User user, Model model) {
        model.addAttribute("courses", courseService.getCoursesByUser(user));
        model.addAttribute("studentCount", userService.countByRole("STUDENT"));
        model.addAttribute("courseStats", getCourseStatistics());

        // Add empty student-specific attributes to prevent template errors
        model.addAttribute("enrollments", null);
        model.addAttribute("submissions", null);
        model.addAttribute("progressStats", null);
        model.addAttribute("timeSpent", null);
        model.addAttribute("timeDistribution", null);
        model.addAttribute("recentActivities", null);
    }

    private Map<String, Object> getCourseStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("activeCourses", courseService.countAllCourses());
        stats.put("avgCompletion", calculateAverageCourseCompletion());
        return stats;
    }

    private int calculateAverageCourseCompletion() {
        // Default implementation - replace with your actual calculation
        return 65;
    }

}