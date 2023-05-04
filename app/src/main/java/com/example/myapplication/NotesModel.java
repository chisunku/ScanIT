package com.example.myapplication;

public class NotesModel {
    int checked;

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    String task;

    public NotesModel(String task, int checked){
        this.checked = checked;
        this.task = task;
    }

}
