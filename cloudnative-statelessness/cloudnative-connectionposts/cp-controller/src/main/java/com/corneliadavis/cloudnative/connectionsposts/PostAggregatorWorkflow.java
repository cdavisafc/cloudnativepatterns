package com.corneliadavis.cloudnative.connectionsposts;

import java.util.ArrayList;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.springframework.stereotype.Component;

@WorkflowInterface
public interface PostAggregatorWorkflow {

    // This workflow will be invoked via the ConnectionsPostsController
    @WorkflowMethod
    ArrayList<PostSummary> aggregatePosts(String username);
}