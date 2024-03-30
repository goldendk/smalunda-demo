package com.example.smalundademo.tasks.bpmnbuilder;
@Deprecated
public record BpmnEdge(String id,
                       String bpmnElementId,
                       XY from, XY to) {


    public String toBpmnXml(){
        return """
                 <bpmndi:BPMNEdge id="%s" bpmnElement="%s">
                        <di:waypoint x="%d" y="%d" />
                        <di:waypoint x="%d" y="%d" />
                      </bpmndi:BPMNEdge>
                    
                """.formatted(id, bpmnElementId, from.x(), from.y(), to.x(), to.y());
    }
}
