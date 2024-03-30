package com.example.smalundademo.model;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.List;
@RecordBuilder
public record ProcessState(
        String processId,
        String stateId,
        List<String> waiting, List<String> retrying, List<String> failed) {
}
