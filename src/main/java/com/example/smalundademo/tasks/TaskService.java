package com.example.smalundademo.tasks;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Responsible for the execution of tasks.
 * Delegates the actual task workflow to {@link TaskRunner}.
 */
@Component
public class TaskService {

    ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public void scheduleBatch() {
        _schedule();
    }

    private void _schedule() {

        List<Task> waitingTasks = repository.loadWaitingTaskByPriority(1);

        TaskResult result = null;
        for (Task task : waitingTasks) {
            try {
                task.lock();
                if (task.getState() == TaskState.INITIAL) {
                    task.setState(TaskState.IN_PROGRESS);
                }
                repository.update(task);
                TaskRunner taskRunner = new TaskRunner(task);
                result = taskRunner.call();
                if (result.newState() == TaskState.DONE) {

                    TaskFlow taskFlow = TaskConfiguration.taskFlows.get(task.getFlowId());
                    if (taskFlow != null) {
                        List<TaskBranch> taskBranches = taskFlow.findTaskBranches(task.getTaskDef());

                        for (TaskBranch tb : taskBranches) {
                            if (tb.predicate().test(task)) {
                                createTask(task.getFlowId(), tb.to());
                            }
                        }
                    }
                }

            } catch (Exception e) {
                result = new TaskResult(true, e, TaskState.RETRYING);
            } finally {
                task.unlock();
                task.setState(result.newState());
                repository.saveResult(task.getId(), result);
                repository.update(task);
                result = null;
            }
        }
    }


    public Task createTask(String flowId, TaskDef taskDef) {
        Task task = new Task();
        task.setFlowId(flowId);
        task.setTaskDef(taskDef);
        task.setState(TaskState.INITIAL);
        repository.save(task);
        return task;
    }
}
