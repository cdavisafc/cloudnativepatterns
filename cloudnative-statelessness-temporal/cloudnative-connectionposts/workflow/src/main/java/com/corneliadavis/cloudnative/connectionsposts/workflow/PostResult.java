package com.corneliadavis.cloudnative.connectionsposts.workflow;

import java.util.Date;

public class PostResult {
    Long userId;
    String title;
    Date date;

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }
}
