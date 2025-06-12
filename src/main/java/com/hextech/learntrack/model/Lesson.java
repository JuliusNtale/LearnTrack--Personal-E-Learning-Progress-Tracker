package com.hextech.learntrack.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private int orderInModule;

    @Column(nullable = false)
    private String lessonType; // VIDEO, READING, QUIZ, ASSIGNMENT

    @Column
    private String videoUrl;

    @Column
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;
}