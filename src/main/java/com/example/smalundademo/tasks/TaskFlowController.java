package com.example.smalundademo.tasks;

import com.example.smalundademo.tasks.bpmnbuilder.CamundaTaskFlowBpmnBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taskflow")
public class TaskFlowController {
    private final TaskRepository repository;
    private final TaskService service;

    public TaskFlowController(TaskRepository repository, TaskService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/list")
    public List<String> getTaskFlowList() {
        return TaskConfiguration.taskFlows.keySet().stream().sorted().toList();
    }

    @GetMapping("/report/{id}")
    public TaskFlowReport getReport(@PathVariable("id") String taskFlowId) {
        TaskFlowReport taskFlowReport = repository.getTaskFlowReport(taskFlowId);
        return taskFlowReport;
    }

    @GetMapping(value = "/bpmn/{taskFlowId}", produces = "application/xml")
    @ResponseBody
    public String getTaskFlowAsBpmn(@PathVariable("taskFlowId") String taskFlowId) {

        TaskFlow taskFlow = TaskConfiguration.taskFlows.get(taskFlowId);
        if (taskFlow == null) {
            throw new RuntimeException("Taskflow not found: " + taskFlowId);
        }
        CamundaTaskFlowBpmnBuilder camundaTaskFlowBpmnBuilder = new CamundaTaskFlowBpmnBuilder();

        String s = camundaTaskFlowBpmnBuilder.buildBpmn(taskFlow);
        return s;
    }

}
