package com.example.smalundademo.tasks;

import io.soabase.recordbuilder.core.RecordBuilder;

/**
 * The result of a task execution.
 */
@RecordBuilder
public record TaskResult(boolean error,
                         Exception errorCause,
                         TaskState newState) {
}
