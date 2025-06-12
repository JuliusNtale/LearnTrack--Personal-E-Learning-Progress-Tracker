
---

# 📚 LearnTrack – Personal E-Learning Progress Tracker

*A Student-Centric System for Tracking Self-Learning Progress*

---

## 🚀 Project Overview

**LearnTrack** is a minimalist, web-based application designed specifically for students to track their personal learning progress. Unlike traditional Learning Management Systems (LMS), LearnTrack empowers students to self-organize and monitor their learning goals, courses, and materials without any complexity or dependency on instructors.

---

## 🎯 Project Objectives

1. Develop a student-only web platform for tracking progress in online/self-paced learning.
2. Provide tools for self-monitoring like time tracking, progress visualization, and completion rates.
3. Create a personalized hub to organize custom courses, study materials, and goals.
4. Simplify tracking with a clean UI and effective features.
5. Ensure accessibility across all devices (desktop, tablet, mobile).

---

## 🧩 Functional Requirements

### 1. 👤 User Management (Students & Admin)
- **Student Registration & Login**
  - Email verification
  - Secure password storage (hashed)
  - Editable student profile: Name, Picture, Bio

- **Admin Dashboard**
  - View, ban, or manage student accounts
  - Monitor system performance
  - Backup/restore data (no course interference)

---

### 2. 📚 Course & Learning Management
- **Personal Course Creation**
  - Add, edit, or delete custom self-learning courses
  - Categorize courses (e.g., Programming, Math, Languages)
  - Define custom completion criteria

- **Learning Materials Upload**
  - Attach PDFs, embed video links (e.g., YouTube)
  - Create searchable tags for easy retrieval

---

### 3. 📈 Progress Tracking & Self-Monitoring
- **Manual Tracking**
  - Mark lessons as completed ✅
  - Add notes or thoughts per module

- **Automated Tracking**
  - Session timer for time logging
  - Visual progress bars showing % completion

- **Achievements & Certificates**
  - Auto-generated certificates upon completion
  - Earn badges for key milestones (e.g., "10 hours studied!")

---

### 4. 📊 Analytics & Reports
- **Student Dashboard**
  - Weekly/monthly study time graphs
  - Completion rate summaries

- **Downloadable Reports**
  - Export personal progress as PDF or CSV

---

### 5. 🔔 Notifications & Reminders
- **Email Alerts**
  - "You haven’t studied [Course] in 3 days!"
  - "Congrats! You completed [Course]!"

- **In-App Reminders**
  - Set custom deadlines and reminders

---

## 🛠️ Tech Stack
| **Area**       | **Technology**              |
|----------------|-----------------------------|
| Frontend       | Themelead & HTML            |
| Backend        | Java Spring Boot            |
| Database       | Mysql Server                |
| Authentication | JWT + Spring Security       |
| Deployment     | Heroku (Backend) + Vercel (Frontend) |


---

## ✅ Why This Project Works

- **Simplified Scope** – No instructors; focused only on the student experience.
- **Low Complexity** – Manual tracking, basic analytics — no AI or ML needed.
- **Highly Personal** – Built for individual learners, not institutions.
- **Scalable** – Future integration of AI (recommendations, smart alerts) is possible.

---

## 📂 Folder Structure Suggestion (Coming Soon in Repo)

```bash
learntrack/
E:.
¦   .gitattributes
¦   .gitignore
¦   folder-structure.txt
¦   LICENSE
¦   pom.xml
¦   README.md
¦   
+---.idea
¦       .gitignore
¦       compiler.xml
¦       encodings.xml
¦       jarRepositories.xml
¦       misc.xml
¦       vcs.xml
¦       workspace.xml
¦       
+---.mvn
+---docs
¦       Architecture.png
¦       ER-Diagram.png
¦       Proposal.pdf
¦       
+---src
¦   +---main
¦   ¦   +---java
¦   ¦   ¦   +---com
¦   ¦   ¦       +---hextech
¦   ¦   ¦           +---learntrack
¦   ¦   ¦               ¦   LearnTrackApplication.java
¦   ¦   ¦               ¦   
¦   ¦   ¦               +---controller
¦   ¦   ¦               ¦       AdminController.java
¦   ¦   ¦               ¦       AuthController.java
¦   ¦   ¦               ¦       CourseController.java
¦   ¦   ¦               ¦       DashboardController.java
¦   ¦   ¦               ¦       
¦   ¦   ¦               +---exception
¦   ¦   ¦               ¦       ProgressTrackingException.java
¦   ¦   ¦               ¦       
¦   ¦   ¦               +---model
¦   ¦   ¦               ¦       Assignment.java
¦   ¦   ¦               ¦       AssignmentSubmission.java
¦   ¦   ¦               ¦       Course.java
¦   ¦   ¦               ¦       Enrollment.java
¦   ¦   ¦               ¦       Lesson.java
¦   ¦   ¦               ¦       LessonProgress.java
¦   ¦   ¦               ¦       Module.java
¦   ¦   ¦               ¦       User.java
¦   ¦   ¦               ¦       
¦   ¦   ¦               +---repository
¦   ¦   ¦               ¦       AssignmentRepository.java
¦   ¦   ¦               ¦       AssignmentSubmissionRepository.java
¦   ¦   ¦               ¦       CourseRepository.java
¦   ¦   ¦               ¦       EnrollmentRepository.java
¦   ¦   ¦               ¦       LessonProgressRepository.java
¦   ¦   ¦               ¦       LessonRepository.java
¦   ¦   ¦               ¦       ModuleRepository.java
¦   ¦   ¦               ¦       UserRepository.java
¦   ¦   ¦               ¦       
¦   ¦   ¦               +---security
¦   ¦   ¦               ¦       CustomUserDetails.java
¦   ¦   ¦               ¦       SecurityConfig.java
¦   ¦   ¦               ¦       
¦   ¦   ¦               +---service
¦   ¦   ¦                       AssignmentService.java
¦   ¦   ¦                       CourseService.java
¦   ¦   ¦                       EmailService.java
¦   ¦   ¦                       EnrollmentService.java
¦   ¦   ¦                       LessonService.java
¦   ¦   ¦                       ModuleService.java
¦   ¦   ¦                       ProgressService.java
¦   ¦   ¦                       SubmissionService.java
¦   ¦   ¦                       UserService.java
¦   ¦   ¦                       
¦   ¦   +---resources
¦   ¦       ¦   application.properties
¦   ¦       ¦   message.properties
¦   ¦       ¦   
¦   ¦       +---static
¦   ¦       +---templates
¦   ¦           ¦   dashboard.html
¦   ¦           ¦   
¦   ¦           +---admin
¦   ¦           ¦       users.html
¦   ¦           ¦       
¦   ¦           +---assignments
¦   ¦           ¦       create.html
¦   ¦           ¦       view.html
¦   ¦           ¦       
¦   ¦           +---auth
¦   ¦           ¦       login.html
¦   ¦           ¦       register.html
¦   ¦           ¦       
¦   ¦           +---courses
¦   ¦           ¦       create.html
¦   ¦           ¦       view.html
¦   ¦           ¦       
¦   ¦           +---fragments
¦   ¦           ¦       navbar.html
¦   ¦           ¦       
¦   ¦           +---lessons
¦   ¦           ¦       create.html
¦   ¦           ¦       
¦   ¦           +---modules
¦   ¦                   create.html
¦   ¦                   view.html
¦   ¦                   
¦   +---test
¦       +---java
+---target
    +---classes
    ¦   ¦   application.properties
    ¦   ¦   message.properties
    ¦   ¦   
    ¦   +---com
    ¦   ¦   +---hextech
    ¦   ¦       +---learntrack
    ¦   ¦           ¦   LearnTrackApplication.class
    ¦   ¦           ¦   
    ¦   ¦           +---controller
    ¦   ¦           ¦       AdminController.class
    ¦   ¦           ¦       AuthController.class
    ¦   ¦           ¦       CourseController.class
    ¦   ¦           ¦       DashboardController.class
    ¦   ¦           ¦       
    ¦   ¦           +---exception
    ¦   ¦           ¦       ProgressTrackingException.class
    ¦   ¦           ¦       
    ¦   ¦           +---model
    ¦   ¦           ¦       Assignment.class
    ¦   ¦           ¦       AssignmentSubmission.class
    ¦   ¦           ¦       Course.class
    ¦   ¦           ¦       Enrollment.class
    ¦   ¦           ¦       Lesson.class
    ¦   ¦           ¦       LessonProgress$LessonProgressBuilder.class
    ¦   ¦           ¦       LessonProgress.class
    ¦   ¦           ¦       Module.class
    ¦   ¦           ¦       User.class
    ¦   ¦           ¦       
    ¦   ¦           +---repository
    ¦   ¦           ¦       AssignmentRepository.class
    ¦   ¦           ¦       AssignmentSubmissionRepository.class
    ¦   ¦           ¦       CourseRepository.class
    ¦   ¦           ¦       EnrollmentRepository.class
    ¦   ¦           ¦       LessonProgressRepository.class
    ¦   ¦           ¦       LessonRepository.class
    ¦   ¦           ¦       ModuleRepository.class
    ¦   ¦           ¦       UserRepository.class
    ¦   ¦           ¦       
    ¦   ¦           +---security
    ¦   ¦           ¦       CustomUserDetails.class
    ¦   ¦           ¦       SecurityConfig.class
    ¦   ¦           ¦       
    ¦   ¦           +---service
    ¦   ¦                   AssignmentService.class
    ¦   ¦                   CourseService.class
    ¦   ¦                   EmailService.class
    ¦   ¦                   EnrollmentService.class
    ¦   ¦                   LessonService.class
    ¦   ¦                   ModuleService.class
    ¦   ¦                   ProgressService.class
    ¦   ¦                   SubmissionService.class
    ¦   ¦                   UserService.class
    ¦   ¦                   
    ¦   +---templates
    ¦       ¦   dashboard.html
    ¦       ¦   
    ¦       +---admin
    ¦       ¦       users.html
    ¦       ¦       
    ¦       +---assignments
    ¦       ¦       create.html
    ¦       ¦       view.html
    ¦       ¦       
    ¦       +---auth
    ¦       ¦       login.html
    ¦       ¦       register.html
    ¦       ¦       
    ¦       +---courses
    ¦       ¦       create.html
    ¦       ¦       view.html
    ¦       ¦       
    ¦       +---fragments
    ¦       ¦       navbar.html
    ¦       ¦       
    ¦       +---lessons
    ¦       ¦       create.html
    ¦       ¦       
    ¦       +---modules
    ¦               create.html
    ¦               view.html
    ¦               
    +---generated-sources
    ¦   +---annotations
    +---generated-test-sources
    ¦   +---test-annotations
    +---test-classes


```
---

## 🚀 Quick Start
### Prerequisites
- Java 17, Maven, Node.js 16+, PostgreSQL

### Run Backend
```bash
cd backend
mvn spring-boot:run
```
---


## 📬 Contributions & Feedback

Want to contribute or suggest features? Open an issue or submit a pull request. Feedback is always welcome!

---

## 🧠 License

This project is for academic purposes under MIT License unless otherwise specified.

---



