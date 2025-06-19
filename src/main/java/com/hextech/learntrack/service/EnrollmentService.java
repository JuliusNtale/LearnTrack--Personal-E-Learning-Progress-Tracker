// EnrollmentService.java
package com.hextech.learntrack.service;

import com.hextech.learntrack.model.Enrollment;
import com.hextech.learntrack.model.User;
import com.hextech.learntrack.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hextech.learntrack.model.Course;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public Enrollment enrollUser(User user, Course course) {
        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getEnrollmentsByUser(User user) {
        return enrollmentRepository.findByUser(user);
    }

    public List<Enrollment> getEnrollmentsByCourse(Course course) {
        return enrollmentRepository.findByCourse(course);
    }

    public Optional<Enrollment> getEnrollmentByUserAndCourse(User user, Course course) {
        return enrollmentRepository.findByUserAndCourse(user, course);
    }

    public boolean isUserEnrolled(User user, Course course) {
        return enrollmentRepository.existsByUserAndCourse(user, course);
    }

    public Enrollment updateEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    public void unenrollUser(Long enrollmentId) {
        enrollmentRepository.deleteById(enrollmentId);
    }

    // Add this method to your EnrollmentService class
    public List<Course> getEnrolledCourses(User user) {
        return enrollmentRepository.findByUser(user)
                .stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }
}
