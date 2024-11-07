package com.corneliadavis.cloudnative.connectionsposts.workflow;

import java.time.Duration;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corneliadavis.cloudnative.connectionsposts.shared.PostAggregatorWorkflow;
import com.corneliadavis.cloudnative.connectionsposts.shared.PostSummary;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

public class PostAggregatorWorkflowImpl implements PostAggregatorWorkflow {

    private static final Logger logger = LoggerFactory.getLogger(PostAggregatorWorkflowImpl.class);

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
            .setScheduleToCloseTimeout(Duration.ofSeconds(500)) // Entire duration from scheduling to completion
                                                                // including queue time
            .build();

    // ActivityStubs enable calls to methods as if the Activity object is local but
    // actually perform an RPC invocation
    private final PostAggregatorActivities aggregatorActivityStub = Workflow
            .newActivityStub(PostAggregatorActivities.class, defaultActivityOptions, null);

    public ArrayList<PostSummary> aggregatePosts(String username) {

        ArrayList<PostSummary> postSummaries = new ArrayList<>();
        logger.info("getting posts for user network " + username);

        String ids = "";

        // get connections
        ConnectionResult[] connections = aggregatorActivityStub.getConnectionsForUser(username);
        for (int i = 0; i < connections.length; i++) {
            if (i > 0)
                ids += ",";
            ids += connections[i].getFollowed().toString();
        }
        logger.debug("connection ids = " + ids);

        // get posts for those connections
        PostResult[] posts = aggregatorActivityStub.getPostsForUsers(ids);

        for (PostResult post : posts) {
            postSummaries.add(new PostSummary(aggregatorActivityStub.getUsernameFromId(post.getUserId()),
                    post.getTitle(), post.getDate()));
        }

        return postSummaries;
    }

}