package com.example.task.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "author")
    @JsonManagedReference
    private List<Task> tasks;

    @OneToMany(mappedBy = "assignee")
    @JsonManagedReference
    private List<Task> assignedTasks;

    @OneToMany(mappedBy = "author")
    @JsonManagedReference
    private List<Comment> comments;
}