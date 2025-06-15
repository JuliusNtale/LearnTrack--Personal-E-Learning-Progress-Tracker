package com.hextech.learntrack.controller;

import com.hextech.learntrack.model.User;
import com.hextech.learntrack.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DashboardController {
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
        model.addAttribute("user", user);

        if (user.getRole().equals("STUDENT")) {
            model.addAttribute("enrollments", enrollmentService.getEnrollmentsByUser(user));
            model.addAttribute("submissions", submissionService.getRecentSubmissionsByUser(user, 5));
            model.addAttribute("progressStats", progressService.getUserProgressStats(user));
            model.addAttribute("timeSpent", progressService.getWeeklyTimeSpent(user));
            model.addAttribute("timeDistribution", progressService.getTimeDistributionByCourse(user));
            model.addAttribute("recentActivities", progressService.getRecentLearningActivities(user, 5));
        }
        else if (user.getRole().equals("ADMIN")) {
            model.addAttribute("courses", courseService.getCoursesByUser(user));
            model.addAttribute("studentCount", userService.countByRole("STUDENT"));
            model.addAttribute("courseStats", getCourseStatistics());
        }

        return "dashboard";
    }

    private Map<String, Object> getCourseStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("activeCourses", courseService.countAllCourses());
        stats.put("avgCompletion", calculateAverageCourseCompletion());
        return stats;
    }

    private int calculateAverageCourseCompletion() {
        // Implementation depends on your business logic
        return 65; // Example value
    }
}