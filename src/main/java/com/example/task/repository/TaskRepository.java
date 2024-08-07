package com.example.task.repository;

import com.example.task.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Репозиторий для работы с задачами.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {


    Page<Task> findByAuthorId(Long authorId, Pageable pageable);

    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);


    Page<Task> findByAuthorIdAndStatus(Long authorId, Task.Status status, Pageable pageable);

    Page<Task> findByAuthorIdAndPriority(Long authorId, Task.Priority priority, Pageable pageable);

    Page<Task> findByAuthorIdAndStatusAndPriority(Long authorId, Task.Status status, Task.Priority priority, Pageable pageable);

    Page<Task> findByAssigneeIdAndStatus(Long assigneeId, Task.Status status, Pageable pageable);

    Page<Task> findByAssigneeIdAndPriority(Long assigneeId, Task.Priority priority, Pageable pageable);

    Page<Task> findByAssigneeIdAndStatusAndPriority(Long assigneeId, Task.Status status, Task.Priority priority, Pageable pageable);
}