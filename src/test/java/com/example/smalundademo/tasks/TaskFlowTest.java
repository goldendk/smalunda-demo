package com.example.smalundademo.tasks;

import org.junit.jupiter.api.Test;

class TaskFlowTest {

    @Test
    public void shouldBuildExampleTaskFlow() {


        TaskDef taskA = new TaskDef("A");
        TaskDef taskB = new TaskDef("B");
        TaskDef taskC = new TaskDef("C");

        TaskFlow taskFlow = TaskFlow.newFlow().flowId(TaskConstants.EXAMPLE_TASK_FLOW_ID)
                .rootTask(taskA)
                .addTaskBranch(
                        TaskBranchBuilder.builder()
                                .from(taskA)
                                .predicate(t -> true)
                                .to(taskB)
                                .build()
                ).build();

    }

}