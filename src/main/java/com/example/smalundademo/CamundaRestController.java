package com.example.smalundademo;

import com.example.smalundademo.model.ProcessDef;
import com.example.smalundademo.model.ProcessState;
import com.example.smalundademo.model.ProcessStateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api")
public class CamundaRestController {


    @GetMapping("/processStates/{processId}")
    public ProcessState getProcessState(@PathVariable("processId") String processId) {
        return b(2, IntStream.of(10, 11, 12), IntStream.of(13, 14, 15), IntStream.of(16));
        // b(1, IntStream.of(1, 2, 3), IntStream.of(4,5,6), IntStream.of(7,8,9)),
        // b(3, IntStream.of(17), IntStream.of(4,5,6), IntStream.of(19, 20)))
    }

    @GetMapping("/processDefinition/{processDefId}")
    public ProcessDef getProcessDef(@PathVariable("processDefId") String processDefId) throws IOException {
        InputStream resourceAsStream = CamundaRestController.class.getResourceAsStream("/bpmn/" + processDefId);
        String s = new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);
        return new ProcessDef(processDefId, s);
    }


    private ProcessState b(int stateNumber, IntStream failed, IntStream waiting, IntStream retrying) {
        return ProcessStateBuilder.builder()
                .stateId("state" + stateNumber)
                .failed(failed.mapToObj(i -> "task" + i).collect(Collectors.toList()))
                .retrying(retrying.mapToObj(i -> "task" + i).collect(Collectors.toList()))
                .waiting(waiting.mapToObj(i -> "task" + i).collect(Collectors.toList())).build();
    }


}
