package com.hextech.learntrack.controller;

import com.hextech.learntrack.model.User;
import com.hextech.learntrack.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private ProgressService progressService;

    @Autowired
    private SubmissionService submissionService;

    @GetMapping("/dashboard")
    public String showDashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);

        // For students: show enrolled courses
        if (user.getRole().equals("STUDENT")) {
            model.addAttribute("enrollments", enrollmentService.getEnrollmentsByUser(user));
            model.addAttribute("submissions", submissionService.getSubmissionsByUser(user));
        }

        // For instructors: show created courses
        if (user.getRole().equals("ADMIN")) {
            model.addAttribute("templates/courses", courseService.getCoursesByUser(user));
        }

        return "dashboard";
    }
}
