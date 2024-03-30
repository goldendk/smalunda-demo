package com.example.smalundademo.tasks.bpmnbuilder;

import com.example.smalundademo.tasks.bpmnbuilder.TaskFlowBpmnBuilder;
import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
@Deprecated
record BpmnShape(String id,
                 String bpmnElementId,
                 BpmnBounds bounds,
                 String colouring,
                 boolean bpmnLabel) {

    public String toShapeXml() {


        String bpmnlabel = "";
        if (bpmnLabel) {
            bpmnlabel = "        <bpmndi:BPMNLabel />\n";
        }
        return """
                <bpmndi:BPMNShape id="%s" bpmnElement="%s" %s>
                        <dc:Bounds x="%d" y="%d" width="%d" height="%d" />
                        %s
                      </bpmndi:BPMNShape>
                """.formatted(id,
                bpmnElementId,
                colouring == null ? "" : colouring,
                bounds.x(),
                bounds.y(),
                bounds.width(),
                bounds.height(),
                bpmnlabel
        );
    }
}

