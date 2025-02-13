package com.corneliadavis.cloudnative.connectionsposts.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.corneliadavis.cloudnative.shared.Utils;

@Component
public class PostAggregatorActivitiesImpl implements PostAggregatorActivities {

    private static final Logger logger = LoggerFactory.getLogger(PostAggregatorWorkflowImpl.class);

    @Value("${connectionpostscontroller.connectionsUrl}")
    private String connectionsUrl;
    @Value("${connectionpostscontroller.postsUrl}")
    private String postsUrl;
    @Value("${connectionpostscontroller.usersUrl}")
    private String usersUrl;
    // I'm not yet sure if ip an port are relevant in the temporal setting
    @Value("${INSTANCE_IP}")
    private String ip;
    @Value("${INSTANCE_PORT}")
    private String p;

    @Autowired
    public PostAggregatorActivitiesImpl() {
    }

    @Override
    public ConnectionResult[] getConnectionsForUser(String username) {

        logger.info(Utils.ipTag(ip, p) + "Getting Connections for user = " + username);
        logger.info("connectionsURL = " + connectionsUrl);

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
