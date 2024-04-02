package com.example.smalundademo.tasks;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.List;

@RecordBuilder
public record TaskReport(String taskId,
                         List<Integer> initial,
                         List<Integer> running,
                         List<Integer> retrying) {
}
