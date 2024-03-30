package com.example.smalundademo.tasks;

import com.example.smalundademo.tasks.TaskFlowReport;
import com.example.smalundademo.tasks.TaskRepository;
import com.example.smalundademo.tasks.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/taskflow")
public class TaskFlowController {
    private final TaskRepository repository;
    private final TaskService service;

    public TaskFlowController(TaskRepository repository, TaskService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/report/{id}")
    public TaskFlowReport getReport(@PathVariable("id") String taskFlowId) {
        TaskFlowReport taskFlowReport = repository.getTaskFlowReport(taskFlowId);
        return taskFlowReport;
    }

}
