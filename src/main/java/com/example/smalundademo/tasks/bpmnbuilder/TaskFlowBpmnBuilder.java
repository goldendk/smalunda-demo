package com.example.smalundademo.tasks.bpmnbuilder;

import com.example.smalundademo.tasks.TaskBranch;
import com.example.smalundademo.tasks.TaskDef;
import com.example.smalundademo.tasks.TaskFlow;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 */
@Deprecated
public class TaskFlowBpmnBuilder {

    private List<String> processEvents = new ArrayList<>();
    private List<String> diagramShapeAndEdges = new ArrayList<>();

    private ProcessElementFactory elementFactory = new ProcessElementFactory();
    private DiagramElementFactory diagramElementFactory = new DiagramElementFactory();

    public String convertToBpmn(TaskFlow flow) {

        InputStream resourceAsStream = TaskFlowBpmnBuilder.class.getResourceAsStream("/bpmn/bare_bone_template.bpmn");

        String template = null;
        try {
            template = new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        TaskBranch taskBranch1 = flow.branchList().get(0); // flow is always defined as left-to-right so we can start here.
        TaskDef firstTask = taskBranch1.from();


        //create start node.
        String outgoingStartEdgeId = "edge_startEvent1_" + firstTask.id();
        String incomingToEndNodeId = "edge_lastTask_endEvent1"; //TODO: figure out last task instead of hardcoding it.
        processEvents.add(elementFactory.createStartNode("startEvent1", outgoingStartEdgeId));
        diagramShapeAndEdges.add(diagramElementFactory.createStartNodeShape(shapeId(), "startEvent1"));


        //create first task
        String taskBpmnId = "task" + firstTask.id();
        processEvents.add(elementFactory.createServiceTask(taskBpmnId, firstTask.id(), outgoingStartEdgeId, incomingToEndNodeId));
        diagramShapeAndEdges.add(diagramElementFactory.createServiceTaskShape(shapeId(), taskBpmnId));

        // add outgoing line.
        processEvents.add(elementFactory.createSequenceFlow(outgoingStartEdgeId, "startEvent1", taskBpmnId));
        diagramShapeAndEdges.add(diagramElementFactory.createEdge(shapeId(), outgoingStartEdgeId, new XY(300, 300), new XY(400, 400)));

        //create end node.
        String endEventId = "endEvent1";
        processEvents.add(elementFactory.createEndEvent(endEventId, incomingToEndNodeId));
        diagramShapeAndEdges.add(diagramElementFactory.createEndNodeShape(shapeId(), endEventId));

        //add ingoing line.
        String endEventIngoingLineId = "edge_" + taskBpmnId + "_" + "endEvent1";
        processEvents.add(elementFactory.createSequenceFlow(endEventIngoingLineId, taskBpmnId, "endEvent1"));
        diagramShapeAndEdges.add((diagramElementFactory.createEdge(shapeId(), endEventIngoingLineId, new XY(100, 100), new XY(200, 200))));

        String result = template.formatted(
                flow.flowId(),
                processEvents.stream().collect(Collectors.joining("\n")),
                flow.flowId(),
                diagramShapeAndEdges.stream().collect(Collectors.joining("\n"))
        );

        return result;
    }

    private static String shapeId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    public static void main(String... args) throws IOException {
        TaskFlowBpmnBuilder builder = new TaskFlowBpmnBuilder();
        TaskDef aDef = new TaskDef("A");
        TaskDef bDef = new TaskDef("B");
        TaskFlow test_flow = TaskFlow.newFlow().flowId("test_flow")
                .addTaskBranch(new TaskBranch(aDef, (t) -> true, bDef))
                .build();
        String s = builder.convertToBpmn(test_flow);
        Files.writeString(Path.of(".", "test_flow.bpmn"), s);

        BpmnModelInstance modelInstance = Bpmn.createExecutableProcess("test_flow")
                .startEvent("start")
                .serviceTask("task1")
                .camundaDelegateExpression("${dummy}")
                .serviceTask("task2")
                .camundaDelegateExpression("${dummy}")
                .endEvent()
                .done();

        Bpmn.writeModelToFile(new File("./test_flow.bpmn"), modelInstance);

    }
}
