import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { getProgress, updateModuleStatus, recordStudySession } from '../../api/progress';
import { ProgressTracker as ProgressTrackerType, Module } from '../../types';
import { ProgressBar, Card, Container, Row, Col, Button, Form, Spinner, Alert } from 'react-bootstrap';
import { useAuth } from '../../contexts/AuthContext';
import StudySessionModal from './StudySessionModal';

const ProgressTracker = () => {
  const { courseId } = useParams<{ courseId: string }>();
  const [progress, setProgress] = useState<ProgressTrackerType | null>(null);
  const [modules, setModules] = useState<Module[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showSessionModal, setShowSessionModal] = useState(false);
  const { user } = useAuth();

  useEffect(() => {
    const fetchProgress = async () => {
      try {
        if (user?.id && courseId) {
          const [progressData, moduleData] = await Promise.all([
            getProgress(parseInt(courseId), user.id),
            getModules(parseInt(courseId))
          ]);
          setProgress(progressData);
          setModules(moduleData);
        }
      } catch (err) {
        setError('Failed to fetch progress data');
      } finally {
        setLoading(false);
      }
    };

    fetchProgress();
  }, [courseId, user]);

  const handleModuleToggle = async (moduleId: number, completed: boolean) => {
    try {
      await updateModuleStatus(moduleId, completed);
      setModules(modules.map(module => 
        module.id === moduleId ? { ...module, completed } : module
      ));
      
      // Recalculate progress
      if (progress) {
        const completedCount = modules.filter(m => m.id === moduleId ? completed : m.completed).length;
        const newPercentage = Math.round((completedCount / modules.length) * 100);
        setProgress({ ...progress, completionPercentage: newPercentage });
      }
    } catch (err) {
      setError('Failed to update module status');
    }
  };

  const handleRecordSession = async (startTime: Date, endTime: Date) => {
    try {
      if (progress) {
        const session = await recordStudySession({
          courseId: progress.course.id,
          startTime,
          endTime
        }, user?.id || 0);
        
        setProgress({
          ...progress,
          totalTimeSpent: progress.totalTimeSpent + session.duration
        });
        setShowSessionModal(false);
      }
    } catch (err) {
      setError('Failed to record study session');
    }
  };

  if (loading) return <Spinner animation="border" />;
  if (error) return <Alert variant="danger">{error}</Alert>;
  if (!progress) return <Alert variant="warning">No progress data found</Alert>;

  return (
    <Container>
      <Row className="mb-4">
        <Col>
          <h2>{progress.course.title} Progress</h2>
          <ProgressBar 
            now={progress.completionPercentage} 
            label={`${progress.completionPercentage}%`} 
            className="mb-3"
          />
          <div className="mb-3">
            <strong>Total Time Spent:</strong> {Math.floor(progress.totalTimeSpent / 60)} hours {progress.totalTimeSpent % 60} minutes
          </div>
          <Button onClick={() => setShowSessionModal(true)}>Record Study Session</Button>
        </Col>
      </Row>

      <Row>
        <Col>
          <h3>Modules</h3>
          {modules.map(module => (
            <Card key={module.id} className="mb-3">
              <Card.Body>
                <Form.Check
                  type="checkbox"
                  id={`module-${module.id}`}
                  label={module.title}
                  checked={module.completed}
                  onChange={(e) => handleModuleToggle(module.id, e.target.checked)}
                />
                <Card.Text className="mt-2">{module.description}</Card.Text>
              </Card.Body>
            </Card>
          ))}
        </Col>
      </Row>

      <StudySessionModal
        show={showSessionModal}
        onHide={() => setShowSessionModal(false)}
        onSave={handleRecordSession}
      />
    </Container>
  );
};

export default ProgressTracker;