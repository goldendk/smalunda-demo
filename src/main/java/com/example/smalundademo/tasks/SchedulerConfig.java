package com.example.smalundademo.tasks;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SchedulerConfig {

    private final TaskRepository taskRepository;
    private final TaskService taskService;

    public SchedulerConfig(TaskRepository taskRepository,
                           TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

    @Scheduled(fixedRate = 1000)
    public void createTasks(){
        taskService.createTask(TaskConstants.EXAMPLE_TASK_FLOW_ID,
                taskRepository.loadTaskDef("A"));
        taskService.scheduleBatch();
    }
}
