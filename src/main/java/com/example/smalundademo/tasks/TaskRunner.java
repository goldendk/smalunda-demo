package com.example.smalundademo.tasks;

import java.util.concurrent.Callable;

public class TaskRunner implements Callable<TaskResult> {
    private final Task task;

    public TaskRunner(Task waiting) {
        this.task = waiting;
    }

    @Override
    public TaskResult call() throws Exception {
        System.out.println("Running task: " + task.getId());

        Thread.sleep(2_000);
        TaskResultBuilder resultBuilder = TaskResultBuilder.builder();

        int v = (int) Math.random() * 10;

        resultBuilder
                .error(false)
                .errorCause(null)
                .newState(TaskState.DONE);

        return resultBuilder.build();
    }
}
