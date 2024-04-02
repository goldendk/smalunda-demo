package com.example.smalundademo.tasks;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface representing a flow of tasks. A flow of tasks is another work for 'work-flow'.
 * A flow always starts with a single task.
 * After any task is completed it may require another task or several tasks to be created. They each in
 * turn will case the same effect when they finish. Eventually the 'flow' of tasks will come to and end
 * (ending the workflow).
 *
 * <p>
 * The path from one node to another is called a 'TaskBranch' in this setup. A TaskFlow will
 * consist of one or more TaskBranches.
 *
 * <p>
 *     Rules: <br/>
 *     <ul>
 *         <li>First taskBranch added is always the starting point of the flow.</li>
 *         <li>All task flows must have a single task to start with. It cannot start with parallel split from start node.</li>
 *         <li>The flow is defined as left-to-right. So taskBranches should be added in this way.</li>
 *         <li>No 'looping' is allowed.</li>
 *     </ul>
 *
 * <p>
 * This example is one TaskFlow that contains 4 Task branches: A-B,  B-C, B-D, D-E.
 * The start node and end nodes do not 'count'
 *
 *
 * <pre>
 *         C--&gt;o
 *         |
 *         |
 * o--&gt;A--&gt;B
 *         |
 *         |
 *         D--&gt;E--&gt;o
 * </pre>
 */
public record TaskFlow(String flowId, TaskDef rootTask, List<TaskBranch> branchList) {

    public TaskFlow {
        if (flowId == null) {
            throw new IllegalArgumentException("flowId must not set");
        }
        if(rootTask == null){
            throw new IllegalArgumentException("rootTask cannot be null");
        }
        if (branchList == null || branchList.isEmpty()) {
            throw new IllegalArgumentException("branchList cannot be null or empty");
        }
    }

    public static TaskFlowFactory.RequireFlowIdFact newFlow() {

        TaskFlowFactory taskFlowFactory = new TaskFlowFactory();
        return taskFlowFactory.requireFlowIdFact;
    }

    public List<TaskBranch> findTaskBranches(TaskDef taskDef) {
        List<TaskBranch> result = branchList.stream()
                .filter(tb -> tb.from().id().equals(taskDef.id()))
                .toList();
        return result;
    }


    public static class TaskFlowFactory {
        private TaskDef rootTask;
        private List<TaskBranch> taskBranches = new ArrayList<>();
        private RequireFlowIdFact requireFlowIdFact = new RequireFlowIdFact();
        private RequireRootTaskFact requireRootTaskFact = new RequireRootTaskFact();
        private String flowId;

        public TaskFlowFactory addTaskBranch(TaskBranch taskBranch) {
            taskBranches.add(taskBranch);
            return this;
        }

        public TaskFlow build() {
            return new TaskFlow(this.flowId, rootTask, taskBranches);
        }

        public class RequireRootTaskFact{
            public TaskFlowFactory rootTask(TaskDef rootTask){
                TaskFlowFactory.this.rootTask = rootTask;
                return TaskFlowFactory.this;
            }
        }

        public class RequireFlowIdFact {
            public RequireRootTaskFact flowId(String flowId) {
                TaskFlowFactory.this.flowId = flowId;
                return TaskFlowFactory.this.requireRootTaskFact;
            }
        }

    }


}
