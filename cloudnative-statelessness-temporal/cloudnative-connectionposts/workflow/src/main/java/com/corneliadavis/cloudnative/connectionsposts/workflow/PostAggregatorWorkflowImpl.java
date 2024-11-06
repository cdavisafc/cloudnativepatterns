package com.corneliadavis.cloudnative.connectionsposts.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import com.corneliadavis.cloudnative.shared.Utils;
import com.corneliadavis.cloudnative.connectionsposts.shared.PostSummary;
import com.corneliadavis.cloudnative.connectionsposts.shared.PostAggregatorWorkflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class PostAggregatorWorkflowImpl implements PostAggregatorWorkflow {

    private static final Logger logger = LoggerFactory.getLogger(PostAggregatorWorkflowImpl.class);


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


    private String ip = "worker";
    private String p = "9999";

        // RetryOptions specify how to automatically handle retries when Activities fail
    private final RetryOptions retryoptions = RetryOptions.newBuilder()
        .setInitialInterval(Duration.ofSeconds(1)) // Wait 1 second before first retry
        .setMaximumInterval(Duration.ofSeconds(20)) // Do not exceed 20 seconds between retries
        .setBackoffCoefficient(2) // Wait 1 second, then 2, then 4, etc
        .setMaximumAttempts(7) // Fail after 5000 attempts
        .build();

    // ActivityOptions specify the limits on how long an Activity can execute before
    // being interrupted by the Orchestration service
    private final ActivityOptions defaultActivityOptions = ActivityOptions.newBuilder()
        .setRetryOptions(retryoptions) // Apply the RetryOptions defined above
        .setStartToCloseTimeout(Duration.ofSeconds(2)) // Max execution time for single Activity
        .setScheduleToCloseTimeout(Duration.ofSeconds(500)) // Entire duration from scheduling to completion including queue time
        .build();

        private final Map<String, ActivityOptions> perActivityMethodOptions = new HashMap<String, ActivityOptions>() {
            {
                // A heartbeat time-out is a proof-of life indicator that an activity is still
                // working.
                // The 5 second duration used here waits for up to 5 seconds to hear a
                // heartbeat.
                // If one is not heard, the Activity fails.
                // The `withdraw` method is hard-coded to succeed, so this never happens.
                // Use heartbeats for long-lived event-driven applications.
                put(p, ActivityOptions.newBuilder().setHeartbeatTimeout(Duration.ofSeconds(5)).build());
            }
        };
    

    // ActivityStubs enable calls to methods as if the Activity object is local but actually perform an RPC invocation
    private final PostAggregatorActivities aggregatorActivityStub = Workflow.newActivityStub(PostAggregatorActivities.class, defaultActivityOptions, perActivityMethodOptions);

    public ArrayList<PostSummary> aggregatePosts(String username) {

        ArrayList<PostSummary> postSummaries = new ArrayList<>();
        logger.info(Utils.ipTag(ip, p) + "getting posts for user network " + username);

        String ids = "";

        // get connections
        ConnectionResult[] connections = aggregatorActivityStub.getConnectionsForUser(username);
         for (int i = 0; i < connections.length; i++) {
            if (i > 0)
                ids += ",";
            ids += connections[i].getFollowed().toString();
        }
        logger.info(Utils.ipTag(ip, p) + "connection ids = " + ids);

        // get posts for those connections

        postSummaries = aggregatorActivityStub.getPostsForUsers(ids);
/*        logger.info(Utils.ipTag(ip, p) + "num Posts = " + posts.length);
        for (PostResult post : posts) {
            postSummaries.add(new PostSummary(aggregatorActivityStub.getUsernameFromId(post.getUserId()), post.getTitle(), post.getDate()));
        }*/

        return postSummaries; 
    }

}