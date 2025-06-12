// ModuleRepository.java
package com.hextech.learntrack.repository;

import com.hextech.learntrack.model.Course;
import com.hextech.learntrack.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByCourse(Course course);
    List<Module> findByCourseOrderByOrderInCourseAsc(Course course);
}