@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ProgressTrackerRepository progressTrackerRepository;

    @Override
    public CourseDTO createCourse(CreateCourseDTO createCourseDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Course course = new Course();
        course.setTitle(createCourseDTO.getTitle());
        course.setDescription(createCourseDTO.getDescription());
        course.setCategory(createCourseDTO.getCategory());
        course.setUser(user);
        
        if (createCourseDTO.getDeadline() != null) {
            course.setDeadline(createCourseDTO.getDeadline());
        }
        
        Course savedCourse = courseRepository.save(course);
        
        // Create a progress tracker for this course
        ProgressTracker progressTracker = new ProgressTracker();
        progressTracker.setUser(user);
        progressTracker.setCourse(savedCourse);
        progressTrackerRepository.save(progressTracker);
        
        return mapToDTO(savedCourse);
    }

    @Override
    public List<CourseDTO> getAllCoursesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return courseRepository.findByUser(user).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDTO updateCourse(Long courseId, UpdateCourseDTO updateCourseDTO, Long userId) {
        Course course = courseRepository.findByIdAndUserId(courseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        
        if (updateCourseDTO.getTitle() != null) {
            course.setTitle(updateCourseDTO.getTitle());
        }
        
        if (updateCourseDTO.getDescription() != null) {
            course.setDescription(updateCourseDTO.getDescription());
        }
        
        if (updateCourseDTO.getCategory() != null) {
            course.setCategory(updateCourseDTO.getCategory());
        }
        
        if (updateCourseDTO.getDeadline() != null) {
            course.setDeadline(updateCourseDTO.getDeadline());
        }
        
        Course updatedCourse = courseRepository.save(course);
        return mapToDTO(updatedCourse);
    }

    @Override
    public void deleteCourse(Long courseId, Long userId) {
        Course course = courseRepository.findByIdAndUserId(courseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        
        courseRepository.delete(course);
    }

    private CourseDTO mapToDTO(Course course) {
        return CourseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(course.getCategory())
                .completed(course.isCompleted())
                .deadline(course.getDeadline())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}