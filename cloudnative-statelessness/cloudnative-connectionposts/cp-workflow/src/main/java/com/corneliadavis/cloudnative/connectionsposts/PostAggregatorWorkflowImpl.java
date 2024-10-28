package com.corneliadavis.cloudnative.connectionsposts;

import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;

import com.corneliadavis.cloudnative.Utils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PostAggregatorWorkflowImpl implements PostAggregatorWorkflow {

    private static final Logger logger = LoggerFactory.getLogger(PostAggregatorWorkflowImpl.class);

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class PostResult {
        @JsonProperty
        Long userId;
        @JsonProperty
        String title;
        @JsonProperty
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ConnectionResult {
        @JsonProperty
        Long followed;

        public Long getFollowed() {
            return followed;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class UserResult {
        @JsonProperty
        String name;

        public String getName() {
            return name;
        }
    }

    /*
     * @Value("${connectionpostscontroller.connectionsUrl}")
     * private String connectionsUrl;
     * 
     * @Value("${connectionpostscontroller.postsUrl}")
     * private String postsUrl;
     * 
     * @Value("${connectionpostscontroller.usersUrl}")
     * private String usersUrl;
     * 
     * @Value("${INSTANCE_IP}")
     * private String ip;
     * 
     * @Value("${INSTANCE_PORT}")
     * private String p;
     */

    private String connectionsUrl = "http://localhost:8082/connections/";
    private String postsUrl = "http://localhost:8081/posts?userIds=";
    private String usersUrl = "http://localhost:8082/users/";
    private String ip = "worker";
    private String p = "9999";

    public ArrayList<PostSummary> aggregatePosts(String username) {
        ArrayList<PostSummary> postSummaries = new ArrayList<>();
        logger.info(Utils.ipTag(ip, p) + "getting posts for user network " + username);
        logger.info(Utils.ipTag(ip, p) + "connectionsUrl = " + connectionsUrl);

        String ids = "";
        RestTemplate restTemplate = new RestTemplate();

        // get connections
        ResponseEntity<ConnectionResult[]> respConns = restTemplate.getForEntity(connectionsUrl + username,
                ConnectionResult[].class);
        ConnectionResult[] connections = respConns.getBody();
        for (int i = 0; i < connections.length; i++) {
            if (i > 0)
                ids += ",";
            ids += connections[i].getFollowed().toString();
        }
        logger.info(Utils.ipTag(ip, p) + "connections = " + ids);

        // get posts for those connections
        ResponseEntity<PostResult[]> respPosts = restTemplate.getForEntity(postsUrl + ids, PostResult[].class);
        PostResult[] posts = respPosts.getBody();

        for (PostResult post : posts) {
            postSummaries.add(new PostSummary(getUsersname(post.getUserId()), post.getTitle(), post.getDate()));
        }

        return postSummaries;
    }

    private String getUsersname(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResult> resp = restTemplate.getForEntity(usersUrl + id, UserResult.class);
        return resp.getBody().getName();
    }
}