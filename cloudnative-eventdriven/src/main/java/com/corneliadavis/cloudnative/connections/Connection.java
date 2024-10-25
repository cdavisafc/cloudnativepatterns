package com.corneliadavis.cloudnative.connections;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Created by corneliadavis on 9/4/17.
 */
@Entity
public class Connection {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Long follower;
    private Long followed;

    protected Connection() {}

    public Connection(Long follower, Long followed) {
        this.follower = follower;
        this.followed = followed;
    }

    public Long getId() {
        return id;
    }

    public Long getFollower() {
        return follower;
    }

   public Long getFollowed() {
        return followed;
    }
}
