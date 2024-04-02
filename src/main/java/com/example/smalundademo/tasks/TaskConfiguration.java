package com.example.smalundademo.tasks;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TaskConfiguration {

    public static Map<String, TaskFlow> taskFlows = new HashMap<>();
    private final TaskRepository taskRepository;

    public TaskConfiguration(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {

        TaskDef taskA = new TaskDef("A");
        TaskDef taskB = new TaskDef("B");
        TaskDef taskC = new TaskDef("C");
        TaskDef taskD = new TaskDef("D");
        TaskDef taskE = new TaskDef("E");

        taskRepository.save(taskA);
        taskRepository.save(taskB);
        taskRepository.save(taskC);


        TaskFlow exampleTaskFlow = TaskFlow.newFlow().flowId(TaskConstants.EXAMPLE_TASK_FLOW_ID)
                .rootTask(taskA)
                .addTaskBranch(new TaskBranch(taskA, (t) -> true, taskB))
                .addTaskBranch(new TaskBranch(taskB, (t) -> true, taskC))
                .addTaskBranch(new TaskBranch(taskB, (t) -> true, taskD))
                .addTaskBranch(new TaskBranch(taskD, (t) -> true, taskE))
                .build();
        taskFlows.put(TaskConstants.EXAMPLE_TASK_FLOW_ID, exampleTaskFlow);

    }
}