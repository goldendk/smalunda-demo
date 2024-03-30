package com.example.smalundademo.tasks;

public enum TaskState {
    /*
    * Initial state is what all tasks are created in
    */
    INITIAL,
    /*
    * Runtime states.
    */
    IN_PROGRESS,
    RETRYING,

    /*
    * These are 'done states'
    * */
    DONE,
    FAILED
}
