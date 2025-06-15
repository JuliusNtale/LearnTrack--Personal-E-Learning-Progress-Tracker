package com.hextech.learntrack.repository;

import com.hextech.learntrack.model.Course;
import com.hextech.learntrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByUser(User user);
    List<Course> findByCategory(String category);
    List<Course> findByLevel(String level);

    @Query("SELECT c FROM Course c WHERE " +
            "LOWER(c.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Course> searchByTitleOrDescription(@Param("query") String query);

    @Query("SELECT COUNT(c) FROM Course c")
    long countAllCourses();
}