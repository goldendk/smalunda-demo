package com.example.smalundademo.tasks.bpmnbuilder;

import com.example.smalundademo.tasks.TaskBranch;
import com.example.smalundademo.tasks.TaskDef;
import com.example.smalundademo.tasks.TaskFlow;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.di.Edge;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CamundaTaskFlowBpmnBuilderTest {

    @Test
    public void shouldBuildSimpleLinearTaskFlow() throws IOException {
        TaskDef aDef = new TaskDef("A");
        TaskDef bDef = new TaskDef("B");
        TaskFlow test_flow = TaskFlow.newFlow().flowId("test_flow")
                .rootTask(aDef)
                .addTaskBranch(new TaskBranch(aDef, (t) -> true, bDef))
                .build();

        String s = new CamundaTaskFlowBpmnBuilder().buildBpmn(test_flow);


        assertNotNull(s);
    }



    @Test
    /**
     * Tests that task A can split into C and D.
     */
    public void shouldBuildSimpleFlowWithParallelGateway() throws IOException {
        TaskDef aDef = new TaskDef("A");
        TaskDef bDef = new TaskDef("B");
        TaskDef cDef = new TaskDef("C");
        TaskFlow test_flow = TaskFlow.newFlow().flowId("test_flow")
                .rootTask(aDef)
                .addTaskBranch(new TaskBranch(aDef, (t) -> true, bDef))
                .addTaskBranch(new TaskBranch(aDef, (t) -> true, cDef))
                .build();

        String s = new CamundaTaskFlowBpmnBuilder().buildBpmn(test_flow);
        assertNotNull(s);

        BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
        Collection<EndEvent> modelElementsByType = bpmnModelInstance.getModelElementsByType(EndEvent.class);
        assertEquals(2, modelElementsByType.size());
    }

    @Test
    /**
     * Tests that task B can split into C and D.
     */
    public void shouldBuild_ABCD_FlowWithParallelGatewayAfterB() throws IOException {
        TaskDef aDef = new TaskDef("A");
        TaskDef bDef = new TaskDef("B");
        TaskDef cDef = new TaskDef("C");
        TaskDef dDef = new TaskDef("D");
        TaskFlow test_flow = TaskFlow.newFlow().flowId("test_flow")
                .rootTask(aDef)
                .addTaskBranch(new TaskBranch(aDef, (t) -> true, bDef))
                .addTaskBranch(new TaskBranch(bDef, (t) -> true, cDef))
                .addTaskBranch(new TaskBranch(bDef, (t) -> true, dDef))
                .build();

        String s = new CamundaTaskFlowBpmnBuilder().buildBpmn(test_flow);
        assertNotNull(s);

        BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
        //Bpmn.writeModelToFile(new File("./test_flow.bpmn"), bpmnModelInstance);
        Collection<EndEvent> modelElementsByType = bpmnModelInstance.getModelElementsByType(EndEvent.class);
        assertEquals(2, modelElementsByType.size());
        assertEquals(4, bpmnModelInstance.getModelElementsByType(ServiceTask.class).size());
        assertEquals(7, bpmnModelInstance.getModelElementsByType(Edge.class).size());

    }



}