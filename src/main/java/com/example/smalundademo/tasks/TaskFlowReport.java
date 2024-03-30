package com.example.smalundademo.tasks;

import java.util.List;

public record TaskFlowReport(String taskFlowId,
                             List<Integer> initial,
                             List<Integer> running,
                             List<Integer> retrying
                             ) {
}
