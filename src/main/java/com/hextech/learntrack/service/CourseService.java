package com.hextech.learntrack.service;

import com.hextech.learntrack.model.Course;
import com.hextech.learntrack.model.User;
import com.hextech.learntrack.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getCoursesByUser(User user) {
        return courseRepository.findByUser(user);
    }

    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category);
    }

    public List<Course> getCoursesByLevel(String level) {
        return courseRepository.findByLevel(level);
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public long countAllCourses() {
        return courseRepository.count();
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> searchCourses(String query) {
        return courseRepository.searchByTitleOrDescription(query);
    }
}