// LearnTrack Custom JavaScript

// Study Timer functionality
class StudyTimer {
    constructor() {
        this.startTime = null;
        this.elapsedTime = 0;
        this.isRunning = false;
        this.timerInterval = null;
    }

    start() {
        if (!this.isRunning) {
            this.startTime = Date.now() - this.elapsedTime;
            this.isRunning = true;
            this.timerInterval = setInterval(() => {
                this.updateDisplay();
            }, 1000);
        }
    }

    stop() {
        if (this.isRunning) {
            clearInterval(this.timerInterval);
            this.isRunning = false;
        }
    }

    reset() {
        this.stop();
        this.elapsedTime = 0;
        this.updateDisplay();
    }

    getElapsedMinutes() {
        return Math.floor(this.elapsedTime / 60000);
    }

    updateDisplay() {
        if (this.isRunning) {
            this.elapsedTime = Date.now() - this.startTime;
        }
        
        const totalSeconds = Math.floor(this.elapsedTime / 1000);
        const hours = Math.floor(totalSeconds / 3600);
        const minutes = Math.floor((totalSeconds % 3600) / 60);
        const seconds = totalSeconds % 60;

        const display = document.getElementById('timer-display');
        if (display) {
            display.textContent = 
                `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
        }
    }
}

// Global timer instance
const studyTimer = new StudyTimer();

// Progress visualization
function createProgressChart(canvasId, data, labels) {
    const ctx = document.getElementById(canvasId);
    if (ctx) {
        return new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: labels,
                datasets: [{
                    data: data,
                    backgroundColor: [
                        '#0d6efd',
                        '#198754',
                        '#ffc107',
                        '#dc3545',
                        '#6f42c1',
                        '#fd7e14'
                    ],
                    borderWidth: 2,
                    borderColor: '#fff'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });
    }
}

// Time spent chart
function createTimeChart(canvasId, timeData) {
    const ctx = document.getElementById(canvasId);
    if (ctx) {
        return new Chart(ctx, {
            type: 'line',
            data: {
                labels: timeData.labels,
                datasets: [{
                    label: 'Hours Studied',
                    data: timeData.data,
                    borderColor: '#0d6efd',
                    backgroundColor: 'rgba(13, 110, 253, 0.1)',
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Hours'
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false
                    }
                }
            }
        });
    }
}

// Notification system
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} alert-dismissible fade show notification-toast`;
    notification.style.position = 'fixed';
    notification.style.top = '20px';
    notification.style.right = '20px';
    notification.style.zIndex = '9999';
    notification.style.minWidth = '300px';
    
    notification.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    document.body.appendChild(notification);
    
    // Auto-remove after 5 seconds
    setTimeout(() => {
        if (notification.parentNode) {
            notification.remove();
        }
    }, 5000);
}

// Progress circle animation
function animateProgressCircle(circleId, percentage) {
    const circle = document.getElementById(circleId);
    if (circle) {
        const circumference = 2 * Math.PI * 35; // radius = 35
        const offset = circumference - (percentage / 100) * circumference;
        
        circle.style.strokeDasharray = circumference;
        circle.style.strokeDashoffset = offset;
    }
}

// Auto-save form data
function setupAutoSave(formId) {
    const form = document.getElementById(formId);
    if (form) {
        const inputs = form.querySelectorAll('input, textarea, select');
        inputs.forEach(input => {
            input.addEventListener('input', function() {
                const formData = new FormData(form);
                const data = Object.fromEntries(formData);
                localStorage.setItem(`autosave_${formId}`, JSON.stringify(data));
            });
        });
        
        // Restore data on page load
        const savedData = localStorage.getItem(`autosave_${formId}`);
        if (savedData) {
            const data = JSON.parse(savedData);
            Object.keys(data).forEach(key => {
                const input = form.querySelector(`[name="${key}"]`);
                if (input) {
                    input.value = data[key];
                }
            });
        }
    }
}

// Course progress tracking
function updateCourseProgress(courseId, progress) {
    fetch(`/api/courses/${courseId}/progress`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ progress: progress })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showNotification('Progress updated successfully!', 'success');
            // Update progress bars
            updateProgressDisplays(courseId, progress);
        }
    })
    .catch(error => {
        console.error('Error updating progress:', error);
        showNotification('Failed to update progress', 'danger');
    });
}

function updateProgressDisplays(courseId, progress) {
    // Update progress bars
    const progressBars = document.querySelectorAll(`[data-course-id="${courseId}"] .progress-bar`);
    progressBars.forEach(bar => {
        bar.style.width = `${progress}%`;
        bar.setAttribute('aria-valuenow', progress);
    });
    
    // Update progress text
    const progressTexts = document.querySelectorAll(`[data-course-id="${courseId}"] .progress-text`);
    progressTexts.forEach(text => {
        text.textContent = `${progress}%`;
    });
}

// Initialize on DOM content loaded
document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Initialize progress circles
    const progressCircles = document.querySelectorAll('.progress-circle');
    progressCircles.forEach(circle => {
        const percentage = circle.getAttribute('data-percentage');
        const circleElement = circle.querySelector('circle');
        if (circleElement) {
            animateProgressCircle(circleElement.id, percentage);
        }
    });
    
    // Setup timer controls
    const startTimerBtn = document.getElementById('start-timer');
    const stopTimerBtn = document.getElementById('stop-timer');
    const resetTimerBtn = document.getElementById('reset-timer');
    
    if (startTimerBtn) {
        startTimerBtn.addEventListener('click', () => {
            studyTimer.start();
            startTimerBtn.disabled = true;
            stopTimerBtn.disabled = false;
        });
    }
    
    if (stopTimerBtn) {
        stopTimerBtn.addEventListener('click', () => {
            studyTimer.stop();
            startTimerBtn.disabled = false;
            stopTimerBtn.disabled = true;
        });
    }
    
    if (resetTimerBtn) {
        resetTimerBtn.addEventListener('click', () => {
            studyTimer.reset();
            startTimerBtn.disabled = false;
            stopTimerBtn.disabled = true;
        });
    }
    
    // Setup auto-save for forms
    const forms = document.querySelectorAll('form[data-autosave]');
    forms.forEach(form => {
        setupAutoSave(form.id);
    });
});

// Export functions for global use
window.LearnTrack = {
    studyTimer,
    showNotification,
    createProgressChart,
    createTimeChart,
    updateCourseProgress,
    animateProgressCircle
};
