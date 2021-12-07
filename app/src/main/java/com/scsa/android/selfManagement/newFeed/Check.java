package com.scsa.android.selfManagement.newFeed;

import java.io.Serializable;

public class Check implements Serializable {

    private String title;
    private String desc;
    private String link;

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getLink() {
        return link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(title).append("\n");
        sb.append(desc);
        return sb.toString();
    }
}
