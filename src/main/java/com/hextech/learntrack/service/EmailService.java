package com.hextech.learntrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n\n"
                + "http://localhost:8080/reset-password?token=" + token + "\n\n"
                + "If you didn't request a password reset, please ignore this email.");

        mailSender.send(message);
    }

    public void sendVerificationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify Your Email - LearnTrack");
        message.setText("Welcome to LearnTrack!\n\n"
                + "To verify your email address, click the link below:\n\n"
                + "http://localhost:8080/verify-email?token=" + token + "\n\n"
                + "If you didn't create an account, please ignore this email.");

        mailSender.send(message);
    }

    public void sendStudyReminderEmail(String to, String courseName, int daysSinceLastStudy) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Study Reminder - " + courseName);
        message.setText("Hi there!\n\n"
                + "You haven't studied \"" + courseName + "\" in " + daysSinceLastStudy + " days.\n\n"
                + "Keep up with your learning goals! Log in to continue your progress:\n"
                + "http://localhost:8080/dashboard\n\n"
                + "Happy learning!\n"
                + "The LearnTrack Team");

        mailSender.send(message);
    }

    public void sendCourseCompletionEmail(String to, String courseName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Congratulations! Course Completed - " + courseName);
        message.setText("🎉 Congratulations!\n\n"
                + "You have successfully completed the course: \"" + courseName + "\"\n\n"
                + "Your dedication to learning is inspiring! Check your dashboard to see your progress and download your certificate:\n"
                + "http://localhost:8080/dashboard\n\n"
                + "Keep learning and growing!\n"
                + "The LearnTrack Team");

        mailSender.send(message);
    }

    public void sendMilestoneAchievementEmail(String to, String milestone) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Achievement Unlocked - " + milestone);
        message.setText("🏆 Achievement Unlocked!\n\n"
                + "You've reached a new milestone: " + milestone + "\n\n"
                + "Your learning journey is progressing wonderfully! View your achievements:\n"
                + "http://localhost:8080/dashboard\n\n"
                + "Keep up the excellent work!\n"
                + "The LearnTrack Team");

        mailSender.send(message);
    }
}
