package com.example.smalundademo.tasks.bpmnbuilder;

@Deprecated
class ProcessElementFactory {

        public String createExclusiveGateway(String id,
                                             String defaultFlowId,
                                             String incomingEdgeId,
                                             String outgoingEdgeId) {
            String defaultFlowAttribute = "";
            if (defaultFlowId != null) {
                defaultFlowAttribute = """
                        default="%s"
                        """.formatted(defaultFlowId);
            }

            return """
                    <bpmn:exclusiveGateway id="%s" %s >
                          <bpmn:incoming>%s</bpmn:incoming>
                          <bpmn:outgoing>%s</bpmn:outgoing>
                        </bpmn:exclusiveGateway>
                    """.formatted(id, defaultFlowAttribute, incomingEdgeId, outgoingEdgeId);
        }


        public String createSequenceFlow(String flowId, String sourceId, String targetId) {
            return """
                        <bpmn:sequenceFlow id="%s" sourceRef="%s" targetRef="%s" />
                    """.formatted(flowId, sourceId, targetId);

        }

        public String createEndEvent(String eventId, String incomingEdgeId) {
            return """
                     <bpmn:endEvent id="%s">
                          <bpmn:incoming>%s</bpmn:incoming>
                        </bpmn:endEvent>
                    """.formatted(eventId, incomingEdgeId);

        }

        public String createServiceTask(String taskId, String taskName, String incomingEdgeId, String outgoingEdgeId) {
            return """
                    <bpmn:serviceTask id="%s" name="%s"
                        camunda:asyncAfter="true"
                        camunda:exclusive="false"
                        camunda:expression="a=2"
                        camunda:resultVariable="a">
                          <bpmn:extensionElements />
                          <bpmn:incoming>%s</bpmn:incoming>
                          <bpmn:outgoing>%s</bpmn:outgoing>
                        </bpmn:serviceTask>
                    """.formatted(taskId, taskName, incomingEdgeId, outgoingEdgeId);
        }

        public String createStartNode(String eventId, String outgoingEdgeId) {

            return """    
                        <bpmn:startEvent id="%s">
                          <bpmn:outgoing>%s</bpmn:outgoing>
                        </bpmn:startEvent>
                    """.formatted(eventId, outgoingEdgeId);
        }

    }