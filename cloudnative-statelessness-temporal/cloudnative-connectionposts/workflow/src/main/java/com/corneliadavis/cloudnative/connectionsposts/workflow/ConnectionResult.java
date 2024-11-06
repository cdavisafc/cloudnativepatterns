package com.corneliadavis.cloudnative.connectionsposts.workflow;

public class ConnectionResult implements java.io.Serializable {
    Long followed;

    public ConnectionResult() {
    }

    public ConnectionResult(Long followed) {
        this.followed = followed;
    }

    public Long getFollowed() {
        return followed;
    }
}
