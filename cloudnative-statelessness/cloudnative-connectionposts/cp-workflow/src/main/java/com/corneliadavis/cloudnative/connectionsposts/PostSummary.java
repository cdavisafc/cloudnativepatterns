package com.corneliadavis.cloudnative.connectionsposts;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by corneliadavis on 9/4/17.
 */
public class PostSummary implements Serializable {

    private Date date;
    private String usersname;
    private String title;

    public PostSummary(String usersname, String title, Date date2) {
        this.date = date2;
        this.usersname = usersname;
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public String getUsersname() {
        return usersname;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "PostSummary{" +
                "date=" + date +
                ", usersname='" + usersname + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

}
