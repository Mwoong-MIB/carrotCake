package com.scsa.android.selfManagement.toDoList;

import java.io.Serializable;

public class Task implements Serializable{
    private int _id;
    private String todo;
    private String desc;
    private int prior;
    private long time;

    public Task() {}

    public Task(int _id, String todo) {
        this._id = _id;
        this.todo = todo;
    }

    public Task(int _id, String todo, String desc, int prior, long time) {
        this._id = _id;
        this.todo = todo;
        this.desc = desc;
        this.prior = prior;
        this.time = time;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPrior() {
        return prior;
    }

    public void setPrior(int prior) {
        this.prior = prior;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Task :");
        sb.append("_id=").append(_id);
        sb.append(", todo='").append(todo).append('\'');
        sb.append(", desc='").append(desc).append('\'');
        sb.append(", prior=").append(prior);
        sb.append(", time=").append(time);
        return sb.toString();
    }
}
