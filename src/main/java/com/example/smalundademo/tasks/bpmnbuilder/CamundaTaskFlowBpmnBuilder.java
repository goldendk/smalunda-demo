package com.example.smalundademo.tasks.bpmnbuilder;

import com.example.smalundademo.tasks.TaskBranch;
import com.example.smalundademo.tasks.TaskDef;
import com.example.smalundademo.tasks.TaskFlow;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.builder.AbstractEventBuilder;
import org.camunda.bpm.model.bpmn.builder.ServiceTaskBuilder;
import org.camunda.bpm.model.bpmn.builder.StartEventBuilder;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnDiagram;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnPlane;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CamundaTaskFlowBpmnBuilder {

    public String buildBpmn(TaskFlow taskFlow) {


        org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder<?, ?> builder = Bpmn.createExecutableProcess(taskFlow.flowId())
                .startEvent("startEvent1")
                .name("Start Event 1");

        TaskBranch firstBranch = taskFlow.branchList().get(0);

        builder = builder
                .serviceTask("task" + firstBranch.from().id())
                .name(firstBranch.from().id())
                .camundaDelegateExpression("${dummy}");

        //find out if gateway or not.
        List<TaskBranch> firstOutcomes = taskFlow.findTaskBranches(firstBranch.from());

        if(firstOutcomes.size() > 1){
            //Element should be a parallel gateway.
            builder = builder.parallelGateway();
        }
        else{
            //Element should be a task.
            builder = builder
                    .serviceTask("task" + firstBranch.to().id())
                    .name(firstBranch.to().id())
                    .camundaDelegateExpression("${dummy}");
        }

        builder = builder.endEvent("endEvent1")
                .name("End Event 1");

        BpmnModelInstance bpmn = builder.done();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bpmn.writeModelToStream(baos, bpmn);
        return baos.toString(StandardCharsets.UTF_8);
    }


    public static void main(String... args) throws IOException {

    }
}
