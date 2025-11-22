package com.hextech.learntrack.service;

import com.hextech.learntrack.model.User;
import com.hextech.learntrack.model.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private ProgressService progressService;

    @Autowired
    private EnrollmentService enrollmentService;

    /**
     * Generate CSV report of user's progress
     */
    public String generateCSVReport(User user) {
        StringBuilder csv = new StringBuilder();
        
        // CSV Header
        csv.append("Course Name,Progress %,Total Time Spent (hours),Completion Status,Enrollment Date\n");
        
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUser(user);
        
        for (Enrollment enrollment : enrollments) {
            csv.append('"').append(enrollment.getCourse().getTitle()).append('"').append(',');
            csv.append(enrollment.getProgress()).append(',');
            
            // Calculate total time spent in hours
            int totalMinutes = progressService.getTotalTimeSpentForEnrollment(enrollment);
            double totalHours = totalMinutes / 60.0;
            csv.append(String.format("%.2f", totalHours)).append(',');
            
            // Completion status
            String status = enrollment.getProgress() >= 100 ? "Completed" : "In Progress";
            csv.append('"').append(status).append('"').append(',');
            
            // Enrollment date
            csv.append('"').append(enrollment.getEnrollmentDate().format(DateTimeFormatter.ISO_LOCAL_DATE)).append('"');
            csv.append('\n');
        }
        
        return csv.toString();
    }

    /**
     * Generate detailed CSV report including lesson-level progress
     */
    public String generateDetailedCSVReport(User user) {
        StringBuilder csv = new StringBuilder();
        
        // CSV Header
        csv.append("Course,Module,Lesson,Completed,Completion Date,Time Spent (minutes)\n");
        
        List<Object[]> activities = progressService.getDetailedProgressReport(user);
        
        for (Object[] activity : activities) {
            // Assuming the query returns: course_title, module_title, lesson_title, completed, completion_date, time_spent
            for (int i = 0; i < activity.length; i++) {
                if (activity[i] != null) {
                    csv.append('"').append(activity[i].toString()).append('"');
                } else {
                    csv.append("\"\"");
                }
                if (i < activity.length - 1) {
                    csv.append(',');
                }
            }
            csv.append('\n');
        }
        
        return csv.toString();
    }

    /**
     * Generate simple PDF report content (HTML that can be converted to PDF)
     * Note: For full PDF generation, you'd need a library like iText or Flying Saucer
     */
    public String generatePDFContent(User user) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<title>Learning Progress Report - ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 40px; }");
        html.append("h1 { color: #0d6efd; border-bottom: 2px solid #0d6efd; padding-bottom: 10px; }");
        html.append("h2 { color: #6c757d; margin-top: 30px; }");
        html.append("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        html.append("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
        html.append("th { background-color: #f8f9fa; font-weight: bold; }");
        html.append("tr:nth-child(even) { background-color: #f8f9fa; }");
        html.append(".summary { background-color: #e7f3ff; padding: 20px; border-radius: 5px; margin: 20px 0; }");
        html.append(".completed { color: #198754; font-weight: bold; }");
        html.append(".in-progress { color: #fd7e14; font-weight: bold; }");
        html.append("</style>");
        html.append("</head><body>");
        
        // Header
        html.append("<h1>Learning Progress Report</h1>");
        html.append("<p><strong>Student:</strong> ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("</p>");
        html.append("<p><strong>Email:</strong> ").append(user.getEmail()).append("</p>");
        html.append("<p><strong>Report Generated:</strong> ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("</p>");
        
        // Summary Section
        Map<String, Object> stats = progressService.getUserProgressStats(user);
        html.append("<div class='summary'>");
        html.append("<h2>Summary</h2>");
        html.append("<p><strong>Total Lessons Completed:</strong> ").append(stats.get("totalCompleted")).append("</p>");
        html.append("<p><strong>Average Course Completion:</strong> ").append(stats.get("avgCompletion")).append("%</p>");
        html.append("<p><strong>Courses In Progress:</strong> ").append(stats.get("coursesInProgress")).append("</p>");
        html.append("<p><strong>Total Study Time:</strong> ").append(progressService.getTotalStudyTime(user) / 60.0).append(" hours</p>");
        html.append("</div>");
        
        // Course Progress Table
        html.append("<h2>Course Progress</h2>");
        html.append("<table>");
        html.append("<tr><th>Course Name</th><th>Progress</th><th>Status</th><th>Time Spent</th><th>Enrollment Date</th></tr>");
        
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUser(user);
        for (Enrollment enrollment : enrollments) {
            html.append("<tr>");
            html.append("<td>").append(enrollment.getCourse().getTitle()).append("</td>");
            html.append("<td>").append(enrollment.getProgress()).append("%</td>");
            
            String status = enrollment.getProgress() >= 100 ? "Completed" : "In Progress";
            String statusClass = enrollment.getProgress() >= 100 ? "completed" : "in-progress";
            html.append("<td class='").append(statusClass).append("'>").append(status).append("</td>");
            
            int totalMinutes = progressService.getTotalTimeSpentForEnrollment(enrollment);
            html.append("<td>").append(String.format("%.1f hours", totalMinutes / 60.0)).append("</td>");
            html.append("<td>").append(enrollment.getEnrollmentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("</td>");
            html.append("</tr>");
        }
        
        html.append("</table>");
        html.append("</body></html>");
        
        return html.toString();
    }

    /**
     * Generate study time summary for charts
     */
    public Map<String, Object> generateStudyTimeSummary(User user) {
        return progressService.getTimeDistributionByCourse(user);
    }
}
