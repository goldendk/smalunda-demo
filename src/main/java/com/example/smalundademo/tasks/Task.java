package com.example.smalundademo.tasks;


public class Task {
    private boolean locked;
    private int id;
    private TaskState state;
    private String flowId;

    private String behaviorId;
    private TaskDef taskDef;


    public Task() {
        this.state = TaskState.INITIAL;
    }

    public void lock() {
        System.out.println("Locking: " + getId());
        this.locked = true;
    }

    public void unlock() {
        System.out.println("Unlocking: " + getId());
        this.locked = false;
    }

    public void setTaskDef(TaskDef taskDef) {
        this.taskDef = taskDef;
    }

    public String getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(String behaviorId) {
        this.behaviorId = behaviorId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public String getFlowId() {
        return this.flowId;
    }

    public TaskDef getTaskDef() {
        return this.taskDef;
    }
}
