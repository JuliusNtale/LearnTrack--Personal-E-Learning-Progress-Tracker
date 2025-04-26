import { useState, useEffect } from 'react';
import { getWeeklyProgress, getCourses } from '../../api/progress';
import { ProgressChartData, Course } from '../../types';
import { Bar } from 'react-chartjs-2';
import { Card, Container, Row, Col, Spinner, Alert } from 'react-bootstrap';
import { useAuth } from '../../contexts/AuthContext';

const Dashboard = () => {
  const [weeklyData, setWeeklyData] = useState<ProgressChartData[]>([]);
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { user } = useAuth();

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (user?.id) {
          const [progressData, courseData] = await Promise.all([
            getWeeklyProgress(user.id),
            getCourses(user.id)
          ]);
          setWeeklyData(progressData);
          setCourses(courseData);
        }
      } catch (err) {
        setError('Failed to fetch dashboard data');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [user]);

  const chartData = {
    labels: weeklyData.map(data => data.date),
    datasets: [
      {
        label: 'Study Time (hours)',
        data: weeklyData.map(data => data.hours),
        backgroundColor: 'rgba(54, 162, 235, 0.5)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 1,
      },
    ],
  };

  const chartOptions = {
    scales: {
      y: {
        beginAtZero: true,
        title: {
          display: true,
          text: 'Hours',
        },
      },
      x: {
        title: {
          display: true,
          text: 'Date',
        },
      },
    },
  };

  if (loading) return <Spinner animation="border" />;
  if (error) return <Alert variant="danger">{error}</Alert>;

  return (
    <Container>
      <Row className="mb-4">
        <Col>
          <h2>Dashboard</h2>
        </Col>
      </Row>

      <Row className="mb-4">
        <Col md={8}>
          <Card>
            <Card.Body>
              <Card.Title>Weekly Study Progress</Card.Title>
              <Bar data={chartData} options={chartOptions} />
            </Card.Body>
          </Card>
        </Col>
        <Col md={4}>
          <Card>
            <Card.Body>
              <Card.Title>Quick Stats</Card.Title>
              <Card.Text>
                <strong>Total Courses:</strong> {courses.length}
              </Card.Text>
              <Card.Text>
                <strong>Completed Courses:</strong> {courses.filter(c => c.completed).length}
              </Card.Text>
              <Card.Text>
                <strong>Active Courses:</strong> {courses.filter(c => !c.completed).length}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row>
        <Col>
          <Card>
            <Card.Body>
              <Card.Title>Recent Courses</Card.Title>
              {courses.slice(0, 3).map(course => (
                <div key={course.id} className="mb-3">
                  <h5>{course.title}</h5>
                  <p>{course.description}</p>
                </div>
              ))}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Dashboard;