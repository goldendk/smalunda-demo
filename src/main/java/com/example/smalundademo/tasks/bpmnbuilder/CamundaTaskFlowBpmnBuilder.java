package com.example.smalundademo.tasks.bpmnbuilder;

import com.example.smalundademo.tasks.TaskBranch;
import com.example.smalundademo.tasks.TaskDef;
import com.example.smalundademo.tasks.TaskFlow;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.builder.*;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnDiagram;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnPlane;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CamundaTaskFlowBpmnBuilder {

    public String buildBpmn(TaskFlow taskFlow) {


        org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder<?, ?> builder = Bpmn.createExecutableProcess(taskFlow.flowId())
                .startEvent("startEvent1")
                .name("Start Event 1");

        TaskDef rootTask = taskFlow.rootTask();

        ServiceTaskBuilder rootTaskBuilder = builder
                .serviceTask(rootTask.id())
                .name(rootTask.id())
                .camundaDelegateExpression("${dummy}");

        Map<TaskDef, ServiceTaskBuilder> serviceBuildersToHandle = new HashMap<>();
        serviceBuildersToHandle.put(rootTask, rootTaskBuilder);


        while (!serviceBuildersToHandle.isEmpty()) {
            TaskDef currentTaskDef = serviceBuildersToHandle.keySet().iterator().next();
            ServiceTaskBuilder currentBuilder = serviceBuildersToHandle.remove(currentTaskDef);

            //find out if gateway or not.
            List<TaskBranch> firstOutcomes = taskFlow.findTaskBranches(currentTaskDef);

            if (firstOutcomes.size() > 1) {
                //Element should be a parallel gateway.
                ParallelGatewayBuilder gwBuilder = currentBuilder.parallelGateway();
                //add service task for each outcome and add to 'remaining' tasks to handle outcomes for.
                for (TaskBranch taskBranch : firstOutcomes) {
                    ServiceTaskBuilder serviceTaskBuilder = gwBuilder.serviceTask(taskBranch.to().id())
                            .name(taskBranch.to().id())
                            .camundaDelegateExpression("${dummy}");
                    serviceBuildersToHandle.put(taskBranch.to(), serviceTaskBuilder);
                }
            } else if (firstOutcomes.size() == 1) {
                TaskBranch nextTaskBranch = firstOutcomes.get(0);
                //Element should be a task.
                ServiceTaskBuilder serviceTaskBuilder = currentBuilder
                        .serviceTask(nextTaskBranch.to().id())
                        .name(nextTaskBranch.to().id())
                        .camundaDelegateExpression("${dummy}");
                serviceBuildersToHandle.put(nextTaskBranch.to(),serviceTaskBuilder);
            } else { // is empty
                currentBuilder.endEvent();
            }
        }


        BpmnModelInstance bpmn = builder.done();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bpmn.writeModelToStream(baos, bpmn);
        return baos.toString(StandardCharsets.UTF_8);
    }


    public static void main(String... args) throws IOException {
        String gatewayId = "gatewayId";
        AbstractFlowNodeBuilder<?, ?> builder = Bpmn.createExecutableProcess("test_flow")
                .startEvent("start")
                .serviceTask("task1")
                .camundaDelegateExpression("${dummy}");
        {
            ParallelGatewayBuilder gwBuilder = builder.parallelGateway(gatewayId);
            //add service task for each outcome.

            gwBuilder.serviceTask("task2")
                    .camundaDelegateExpression("${dummy}").endEvent("endEvent1");

            gwBuilder.serviceTask("task3")
                    .camundaDelegateExpression("${dummy}")
                    .endEvent("endEvent2");
        }


        BpmnModelInstance done = builder.done();


        Bpmn.writeModelToFile(new File("./test_flow.bpmn"), done);

    }
}
