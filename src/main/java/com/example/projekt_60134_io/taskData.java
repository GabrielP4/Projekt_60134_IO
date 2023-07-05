package com.example.projekt_60134_io;

public class taskData {
    private Integer taskNumber;
    private String taskDes;

    public taskData(Integer taskNumber, String taskDes) {
        this.taskNumber = taskNumber;
        this.taskDes = taskDes;
    }

    public Integer getTaskNumber() {
        return taskNumber;
    }

    public String getTaskDes() {
        return taskDes;
    }
}