import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getCourses, deleteCourse } from '../../api/courses';
import { Course } from '../../types';
import { Button, Card, Container, Row, Col, Spinner, Alert } from 'react-bootstrap';
import { useAuth } from '../../contexts/AuthContext';
import CourseFormModal from './CourseFormModal';

const CourseList = () => {
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const { user } = useAuth();

  useEffect(() => {
    const fetchCourses = async () => {
      try {
        if (user?.id) {
          const data = await getCourses(user.id);
          setCourses(data);
        }
      } catch (err) {
        setError('Failed to fetch courses');
      } finally {
        setLoading(false);
      }
    };

    fetchCourses();
  }, [user]);

  const handleDelete = async (id: number) => {
    try {
      await deleteCourse(id);
      setCourses(courses.filter(course => course.id !== id));
    } catch (err) {
      setError('Failed to delete course');
    }
  };

  const handleAddCourse = (newCourse: Course) => {
    setCourses([...courses, newCourse]);
    setShowModal(false);
  };

  if (loading) return <Spinner animation="border" />;
  if (error) return <Alert variant="danger">{error}</Alert>;

  return (
    <Container>
      <Row className="mb-4">
        <Col>
          <h2>My Courses</h2>
          <Button onClick={() => setShowModal(true)}>Add New Course</Button>
        </Col>
      </Row>

      <Row>
        {courses.map(course => (
          <Col key={course.id} md={4} className="mb-4">
            <Card>
              <Card.Body>
                <Card.Title>{course.title}</Card.Title>
                <Card.Subtitle className="mb-2 text-muted">
                  {course.category}
                </Card.Subtitle>
                <Card.Text>{course.description}</Card.Text>
                <div className="d-flex justify-content-between">
                  <Link to={`/courses/${course.id}`} className="btn btn-primary">
                    View Details
                  </Link>
                  <Button
                    variant="danger"
                    onClick={() => handleDelete(course.id)}
                  >
                    Delete
                  </Button>
                </div>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      <CourseFormModal
        show={showModal}
        onHide={() => setShowModal(false)}
        onSave={handleAddCourse}
      />
    </Container>
  );
};

export default CourseList;