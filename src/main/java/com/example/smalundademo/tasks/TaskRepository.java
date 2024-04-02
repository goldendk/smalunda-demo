package com.example.smalundademo.tasks;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TaskRepository {
    AtomicInteger idSequence = new AtomicInteger(0);
    private List<Task> tasks = new ArrayList<>();

    private List<TaskDef> taskDefinitions = new ArrayList<>();
    private Map<Integer, List<TaskResult>> attempts = new HashMap<>();

    public List<Task> loadWaitingTaskByPriority(int count) {
        return tasks.stream().filter((t) -> t.getState() == TaskState.INITIAL)
                .limit(count).collect(Collectors.toList());

    }

    public void save(TaskDef taskDef) {
        Optional<TaskDef> taskDef1 = _findTaskDef(taskDef.id());
        if (taskDef1.isPresent()) {
            throw new IllegalStateException("Only unique TaskDef IDs allowed: " + taskDef.id());
        }
        taskDefinitions.add(taskDef);
    }

    public TaskDef loadTaskDef(String id) {
        Optional<TaskDef> taskDef = _findTaskDef(id);
        return taskDef.get();
    }

    private Optional<TaskDef> _findTaskDef(String id) {
        Optional<TaskDef> first = taskDefinitions.stream().filter(td -> td.id().equals(id)).findFirst();
        return first;
    }

    public void update(Task waiting) {
        System.out.println("Updating " + waiting.getId());
    }

    public void save(Task task) {
        task.setId(idSequence.incrementAndGet());
        System.out.println("Saving " + task.getId());
        tasks.add(task);
    }

    public void saveResult(int id, TaskResult result) {
        List<TaskResult> list = attempts.computeIfAbsent(id, (v) -> new ArrayList<>());
        list.add(result);
    }

    /**
     * Return the current state of a task flow.
     *
     * @return
     */
    public TaskFlowReport getTaskFlowReport(String taskFlowId) {

        Stream<Task> targetFlowStream = tasks.stream().filter(t -> t.getFlowId().equals(taskFlowId));

        Map<TaskDef, TaskReportBuilder> taskReportBuilders = new HashMap<>();

        targetFlowStream.forEach(t -> {
            taskReportBuilders.computeIfAbsent(t.getTaskDef(), (key) ->
                    TaskReportBuilder.builder()
                            .taskId(t.getTaskDef().id())
                            .initial(new ArrayList<>())
                            .retrying(new ArrayList<>())
                            .running(new ArrayList<>()));
            if (t.getState() == TaskState.INITIAL) {
                taskReportBuilders.get(t.getTaskDef()).initial().add(t.getId());
            } else if (t.getState() == TaskState.IN_PROGRESS) {
                taskReportBuilders.get(t.getTaskDef()).running().add(t.getId());
            } else if (t.getState() == TaskState.RETRYING) {
                taskReportBuilders.get(t.getTaskDef()).retrying().add(t.getId());
            }

        });


        List<TaskReport> taskReports = taskReportBuilders.values().stream().map(b -> b.build()).collect(Collectors.toList());

        return new TaskFlowReport(taskFlowId, taskReports);

    }
}
