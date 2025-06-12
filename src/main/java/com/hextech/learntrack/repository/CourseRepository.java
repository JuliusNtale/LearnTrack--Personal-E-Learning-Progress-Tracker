// CourseRepository.java
package com.hextech.learntrack.repository;

import com.hextech.learntrack.model.Course;
import com.hextech.learntrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByUser(User user);
    List<Course> findByCategory(String category);
    List<Course> findByLevel(String level);
}