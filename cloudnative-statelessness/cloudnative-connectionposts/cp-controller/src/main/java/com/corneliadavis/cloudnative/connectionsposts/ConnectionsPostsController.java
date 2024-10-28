package com.corneliadavis.cloudnative.connectionsposts;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.corneliadavis.cloudnative.Utils;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ConnectionsPostsController {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionsPostsController.class);

    @Value("${INSTANCE_IP}")
    private String ip;
    @Value("${INSTANCE_PORT}")
    private String p;

    private StringRedisTemplate template;

    @Autowired
    public ConnectionsPostsController(StringRedisTemplate template) {
        this.template = template;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/connectionsposts")
    public Iterable<PostSummary> getByUsername(@CookieValue(value = "userToken", required = false) String token,
            HttpServletResponse response) {

        // Not sure this stuff should be in here

        // A WorkflowServiceStubs communicates with the Temporal front-end service.
        WorkflowServiceStubs serviceStub = WorkflowServiceStubs.newLocalServiceStubs();

        // A WorkflowClient wraps the stub.
        // It can be used to start, signal, query, cancel, and terminate Workflows.
        WorkflowClient client = WorkflowClient.newInstance(serviceStub);

        // Workflow options configure Workflow stubs.
        // A WorkflowId prevents duplicate instances, which are removed.
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(Utils.POST_AGGREGATOR_QUEUE)
                .setWorkflowId("post-aggregator-workflow")
                .build();

        // WorkflowStubs enable calls to methods as if the Workflow object is local
        // but actually perform a gRPC call to the Temporal Service.
        PostAggregatorWorkflow workflow = client.newWorkflowStub(PostAggregatorWorkflow.class, options);

        if (token == null) {
            logger.info(Utils.ipTag(ip, p) + "connectionsPosts access attempt without auth token");
            response.setStatus(401);
        } else {
            ValueOperations<String, String> ops = this.template.opsForValue();
            String username = ops.get(token);
            if (username == null) {
                logger.info(Utils.ipTag(ip, p) + "connectionsPosts access attempt with invalid token");
                response.setStatus(401);
            } else {

                logger.info(Utils.ipTag(ip, p) + "invoking workflow to aggregate posts for user network " + username);
                ArrayList<PostSummary> postSummaries = workflow.aggregatePosts(username);

                return postSummaries;
            }
        }
        return null;
    }

}
