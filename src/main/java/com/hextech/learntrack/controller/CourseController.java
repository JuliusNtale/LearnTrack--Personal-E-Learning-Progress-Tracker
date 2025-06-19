package com.hextech.learntrack.controller;

import com.hextech.learntrack.model.*;
import com.hextech.learntrack.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/courses")  // Changed from "/templates/courses" for consistency
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private ProgressService progressService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private SubmissionService submissionService;


    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);


    @GetMapping
    public String listCourses(@AuthenticationPrincipal User user, Model model) {
        try {
            model.addAttribute("user", user); // Add this line
            if ("ADMIN".equals(user.getRole())) {
                model.addAttribute("courses", courseService.getCoursesByUser(user));
            } else {
                model.addAttribute("courses", enrollmentService.getEnrolledCourses(user));
            }
            return "courses/list";
        } catch (Exception e) {
            logger.error("Error loading courses", e);
            model.addAttribute("error", "Failed to load courses");
            return "error";
        }
    }

    // Course-related methods
    @GetMapping("/create")
    public String showCreateCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "courses/create";  // Removed "templates/" prefix
    }

    @PostMapping("/create")
    public String createCourse(@AuthenticationPrincipal User user,
                               @ModelAttribute Course course) {
        course.setUser(user);
        courseService.createCourse(course);
        return "redirect:/dashboard";
    }

    @GetMapping("/{id}")
    public String viewCourse(@AuthenticationPrincipal User user,
                             @PathVariable Long id,
                             Model model) {
        Course course = courseService.getCourseById(id).orElseThrow();
        model.addAttribute("course", course);

        if (user.getRole().equals("STUDENT")) {
            Enrollment enrollment = enrollmentService.getEnrollmentByUserAndCourse(user, course)
                    .orElse(null);
            model.addAttribute("enrollment", enrollment);
            model.addAttribute("isEnrolled", enrollment != null);
        }

        model.addAttribute("modules", moduleService.getModulesByCourse(course));
        return "courses/view";
    }

    @GetMapping("/{id}/enroll")
    public String enrollInCourse(@AuthenticationPrincipal User user,
                                 @PathVariable Long id) {
        Course course = courseService.getCourseById(id).orElseThrow();
        if (!enrollmentService.isUserEnrolled(user, course)) {
            enrollmentService.enrollUser(user, course);
        }
        return "redirect:/courses/" + id;
    }

    // Module-related methods
    @GetMapping("/{courseId}/modules/create")
    public String showCreateModuleForm(@PathVariable Long courseId,
                                       Model model) {
        model.addAttribute("module", new com.hextech.learntrack.model.Module());  // Fully qualified
        model.addAttribute("courseId", courseId);
        return "modules/create";
    }

    @PostMapping("/{courseId}/modules/create")
    public String createModule(@PathVariable Long courseId,
                               @ModelAttribute com.hextech.learntrack.model.Module module,
                               @AuthenticationPrincipal User user) {
        Course course = courseService.getCourseById(courseId).orElseThrow();
        module.setCourse(course);
        moduleService.createModule(module);
        return "redirect:/courses/" + courseId;
    }

    @GetMapping("/{courseId}/modules/{moduleId}")
    public String viewModule(@PathVariable Long courseId,
                             @PathVariable Long moduleId,
                             @AuthenticationPrincipal User user,
                             Model model) {
        com.hextech.learntrack.model.Module module = moduleService.getModuleById(moduleId).orElseThrow();
        model.addAttribute("module", module);
        model.addAttribute("lessons", lessonService.getLessonsByModule(module));

        if (user.getRole().equals("STUDENT")) {
            Enrollment enrollment = enrollmentService.getEnrollmentByUserAndCourse(user, module.getCourse())
                    .orElseThrow();
            model.addAttribute("enrollment", enrollment);
        }

        return "modules/view";
    }

    // Lesson-related methods (single createLesson method combining both functionalities)
    @GetMapping("/{courseId}/modules/{moduleId}/lessons/create")
    public String showCreateLessonForm(@PathVariable Long courseId,
                                       @PathVariable Long moduleId,
                                       Model model) {
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("courseId", courseId);
        model.addAttribute("moduleId", moduleId);
        model.addAttribute("lessonTypes", List.of("VIDEO", "READING", "QUIZ", "ASSIGNMENT"));
        return "lessons/create";
    }

    @PostMapping("/{courseId}/modules/{moduleId}/lessons/create")
    public String createLesson(@PathVariable Long courseId,
                               @PathVariable Long moduleId,
                               @ModelAttribute Lesson lesson,
                               @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        com.hextech.learntrack.model.Module module = moduleService.getModuleById(moduleId).orElseThrow();
        lesson.setModule(module);

        if (file != null && !file.isEmpty()) {
            String uploadDir = "uploads";
            Files.createDirectories(Paths.get(uploadDir));
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            file.transferTo(new File(uploadDir + "/" + fileName));
            lesson.setFilePath(fileName);
        }

        lessonService.createLesson(lesson);
        return "redirect:/courses/" + courseId + "/modules/" + moduleId;
    }

    @PostMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}/complete")
    public String markLessonComplete(@AuthenticationPrincipal User user,
                                     @PathVariable Long courseId,
                                     @PathVariable Long moduleId,
                                     @PathVariable Long lessonId,
                                     @RequestParam int timeSpentMinutes) {
        progressService.markLessonAsCompleted(user, lessonId, timeSpentMinutes);
        return "redirect:/courses/" + courseId + "/modules/" + moduleId;
    }

    // Assignment-related methods
    @GetMapping("/{courseId}/assignments/create")
    public String showCreateAssignmentForm(@PathVariable Long courseId,
                                           Model model) {
        model.addAttribute("assignment", new Assignment());
        model.addAttribute("courseId", courseId);
        return "assignments/create";
    }

    @PostMapping("/{courseId}/assignments/create")
    public String createAssignment(@PathVariable Long courseId,
                                   @ModelAttribute Assignment assignment) {
        Course course = courseService.getCourseById(courseId).orElseThrow();
        assignment.setCourse(course);
        assignmentService.createAssignment(assignment);
        return "redirect:/courses/" + courseId;
    }

    @GetMapping("/{courseId}/assignments/{assignmentId}")
    public String viewAssignment(@PathVariable Long courseId,
                                 @PathVariable Long assignmentId,
                                 @AuthenticationPrincipal User user,
                                 Model model) {
        Assignment assignment = assignmentService.getAssignmentById(assignmentId).orElseThrow();
        model.addAttribute("assignment", assignment);

        if (user.getRole().equals("STUDENT")) {
            Optional<AssignmentSubmission> submission = submissionService.getSubmissionByUserAndAssignment(user, assignment);
            model.addAttribute("submission", submission.orElse(null));
        }

        return "assignments/view";
    }

    // File download method
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String file) throws IOException {
        Path filePath = Paths.get("uploads").resolve(file).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            throw new RuntimeException("Could not read file: " + file);
        }
    }

    @PostMapping("/{courseId}/assignments/{assignmentId}/submit")
    public String submitAssignment(@AuthenticationPrincipal User user,
                                   @PathVariable Long courseId,
                                   @PathVariable Long assignmentId,
                                   @RequestParam String comments,
                                   @RequestParam("file") MultipartFile file) throws IOException {
        Assignment assignment = assignmentService.getAssignmentById(assignmentId).orElseThrow();

        String filePath = null;
        if (file != null && !file.isEmpty()) {
            String uploadDir = "uploads/assignments";
            Files.createDirectories(Paths.get(uploadDir));
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            file.transferTo(new File(uploadDir + "/" + fileName));
            filePath = "/assignments/" + fileName;
        }

        submissionService.submitAssignment(user, assignment, filePath, comments);
        return "redirect:/courses/" + courseId + "/assignments/" + assignmentId;
    }
}