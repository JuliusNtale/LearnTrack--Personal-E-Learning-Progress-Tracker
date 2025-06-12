// LessonRepository.java
package com.hextech.learntrack.repository;

import com.hextech.learntrack.model.Lesson;
import com.hextech.learntrack.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByModule(Module module);
    List<Lesson> findByModuleOrderByOrderInModuleAsc(Module module);
}