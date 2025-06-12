// ModuleService.java
package com.hextech.learntrack.service;

import com.hextech.learntrack.model.Course;
import com.hextech.learntrack.model.Module;
import com.hextech.learntrack.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepository;

    public Module createModule(Module module) {
        return moduleRepository.save(module);
    }

    public List<Module> getModulesByCourse(Course course) {
        return moduleRepository.findByCourseOrderByOrderInCourseAsc(course);
    }

    public Optional<Module> getModuleById(Long id) {
        return moduleRepository.findById(id);
    }

    public Module updateModule(Module module) {
        return moduleRepository.save(module);
    }

    public void deleteModule(Long id) {
        moduleRepository.deleteById(id);
    }
}