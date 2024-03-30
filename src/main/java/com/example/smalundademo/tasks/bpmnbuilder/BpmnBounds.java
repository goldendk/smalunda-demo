package com.example.smalundademo.tasks.bpmnbuilder;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
@Deprecated
record BpmnBounds(int x, int y, int width, int height) {}