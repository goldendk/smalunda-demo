package com.example.smalundademo.tasks;


import java.util.List;
import java.util.Map;

public record TaskFlowReport(String taskFlowId, List<TaskReport> taskReports) {
}
