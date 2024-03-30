package com.example.smalundademo.tasks;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.function.Predicate;

@RecordBuilder
public record TaskBranch(TaskDef from,
                         Predicate<Task> predicate,
                         TaskDef to) {

    public TaskBranch {

        if (from == null) {
            throw new IllegalArgumentException("Must have from task def");
        }
        if (predicate == null) {
            throw new IllegalArgumentException("Must have predicate");
        }
        if (to == null) {
            throw new IllegalArgumentException("Must have to task def");
        }
    }


}
