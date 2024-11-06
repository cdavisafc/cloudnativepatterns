package com.corneliadavis.cloudnative.connectionsposts.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.corneliadavis.cloudnative.shared.Utils;
import com.corneliadavis.cloudnative.connectionsposts.shared.PostSummary;


public class PostAggregatorActivitiesImpl implements PostAggregatorActivities {

    private static final Logger logger = LoggerFactory.getLogger(PostAggregatorWorkflowImpl.class);

    private String connectionsUrl = "http://localhost:8082/connections/";
    private String postsUrl = "http://localhost:8081/posts?userIds=";
    private String usersUrl = "http://localhost:8082/users/";
    private String ip = "worker";
    private String p = "9999";

    @Override
    public ConnectionResult[] getConnectionsForUser(String username) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<ConnectionResult[]> respConns = restTemplate.getForEntity(connectionsUrl + username,
               ConnectionResult[].class);

        return respConns.getBody();
    }

    @Override
    public PostResult[] getPostsForUsers(String ids) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<PostResult[]> respPosts = restTemplate.getForEntity(postsUrl + ids, PostResult[].class);
        PostResult[] posts = respPosts.getBody();

        return posts;
    }

    @Override
    public String getUsernameFromId(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResult> resp = restTemplate.getForEntity(usersUrl + id, UserResult.class);
        return resp.getBody().getName();
    }

}
