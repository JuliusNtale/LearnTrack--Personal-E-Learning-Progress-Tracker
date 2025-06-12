// EnrollmentRepository.java
package com.hextech.learntrack.repository;

import com.hextech.learntrack.model.Course;
import com.hextech.learntrack.model.Enrollment;
import com.hextech.learntrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByUser(User user);
    List<Enrollment> findByCourse(Course course);
    Optional<Enrollment> findByUserAndCourse(User user, Course course);
    boolean existsByUserAndCourse(User user, Course course);
}