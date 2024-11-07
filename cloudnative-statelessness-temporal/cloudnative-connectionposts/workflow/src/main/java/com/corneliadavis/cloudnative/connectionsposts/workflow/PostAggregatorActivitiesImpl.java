package com.corneliadavis.cloudnative.connectionsposts.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.corneliadavis.cloudnative.shared.Utils;

public class PostAggregatorActivitiesImpl implements PostAggregatorActivities {

    private static final Logger logger = LoggerFactory.getLogger(PostAggregatorWorkflowImpl.class);

    private final String connectionsUrl;
    private final String postsUrl;
    private final String usersUrl;
    // I'm not yet sure if ip an port are relevant in the temporal setting
    private final String ip;
    private final String p;

    public PostAggregatorActivitiesImpl(String connectionsUrl, String postsUrl, String usersUrl, String ip, String p) {
        this.connectionsUrl = connectionsUrl;
        this.postsUrl = postsUrl;
        this.usersUrl = usersUrl;
        this.ip = ip;
        this.p = p;
    }

    @Override
    public ConnectionResult[] getConnectionsForUser(String username) {

        logger.info(Utils.ipTag(ip, p) + "Getting Connections for user = " + username);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<ConnectionResult[]> respConns = restTemplate.getForEntity(connectionsUrl + username,
                ConnectionResult[].class);

        return respConns.getBody();
    }

    @Override
    public PostResult[] getPostsForUsers(String ids) {

        logger.info(Utils.ipTag(ip, p) + "Getting Posts for user ids = " + ids);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<PostResult[]> respPosts = restTemplate.getForEntity(postsUrl + ids, PostResult[].class);
        PostResult[] posts = respPosts.getBody();

        return posts;
    }

    @Override
    public String getUsernameFromId(Long id) {

        logger.info(Utils.ipTag(ip, p) + "Getting Username for user id = " + id);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<UserResult> resp = restTemplate.getForEntity(usersUrl + id, UserResult.class);

        return resp.getBody().getName();
    }

}
