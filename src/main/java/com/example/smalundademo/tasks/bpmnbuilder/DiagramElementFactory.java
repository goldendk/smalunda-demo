package com.example.smalundademo.tasks.bpmnbuilder;

@Deprecated
class DiagramElementFactory {
    private final static BpmnBounds startNodeBounds = new BpmnBounds(-1, -1, 36, 36);
    private final static BpmnBounds serviceTaskBounds = new BpmnBounds(-1, -1, 100, 80);
    private final static String serviceTaskColouring = """
            bioc:stroke="#205022" bioc:fill="#c8e6c9" color:background-color="#c8e6c9" color:border-color="#205022"\040""";

    private final int startX = 179;
    private final int startY = 169;

    private final int edgeLength = 85;


    public String createStartNodeShape(String id, String bpmnElementId) {
        return BpmnShapeBuilder.builder()
                .id(id)
                .bpmnElementId(bpmnElementId)
                .bounds(BpmnBoundsBuilder
                        .builder(startNodeBounds)
                        .x(startX)
                        .y(startY)
                        .build())
                .build()
                .toShapeXml();
    }


    public String createServiceTaskShape(String id, String bpmnElementId) {
        return BpmnShapeBuilder.builder()
                .id(id)
                .bounds(BpmnBoundsBuilder
                        .builder(startNodeBounds)
                        .x(280)
                        .y(240)
                        .build())
                .bpmnElementId(bpmnElementId)
                .bpmnLabel(true)
                .colouring(serviceTaskColouring)
                .build().toShapeXml();
    }


    public String createEndNodeShape(String shapeId, String bpmnElementId) {
        return createStartNodeShape(shapeId, bpmnElementId);
    }

    public String createEdge(String id, String bpmnElementId, XY from, XY to) {
        return """
                <bpmndi:BPMNEdge id="%s" bpmnElement="%s">
                        <di:waypoint x="%d" y="%d" />
                        <di:waypoint x="%d" y="%d" />
                      </bpmndi:BPMNEdge>
                """.formatted(id,
                bpmnElementId,
                from.x(), from.y(),
                to.x(), to.y());

    }
}