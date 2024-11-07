package com.corneliadavis.cloudnative.connectionsposts.workflow;

import com.corneliadavis.cloudnative.connectionsposts.shared.PostAggregatorWorkflow;
import com.corneliadavis.cloudnative.connectionsposts.shared.Shared;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.beans.factory.annotation.Value;


import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

@Configuration
public class PostAggregatorWorkerConfig {

    // @Bean
    // @Value("${connectionpostscontroller.connectionsUrl}")
    // private String connectionsUrl;

    @Bean
    public WorkflowServiceStubs serviceStub() {
        // Create a stub that accesses a Temporal Service on the local development
        // machine
        return WorkflowServiceStubs.newLocalServiceStubs();
    }

    private WorkerFactory workerFactory;

    @Bean
    public WorkerFactory workerFactory(WorkflowServiceStubs service) {

        // The Worker uses the Client to communicate with the Temporal Service
        WorkflowClient client = WorkflowClient.newInstance(service);

        this.workerFactory = WorkerFactory.newInstance(client);
        // A WorkerFactory creates Workers
        return this.workerFactory;
    }

    @Bean
    public Worker worker(WorkerFactory factory, 
                         @Value("${connectionpostscontroller.connectionsUrl}") String connectionsUrl,
                         @Value("${connectionpostscontroller.postsUrl}") String postsUrl,
                         @Value("${connectionpostscontroller.usersUrl}") String usersUrl,
                         @Value("${INSTANCE_IP}") String ip,
                         @Value("${INSTANCE_PORT}") String p) {

        // A Worker listens to one Task Queue.
        // This Worker processes both Workflows and Activities
        Worker worker = factory.newWorker(Shared.POST_AGGREGATOR_QUEUE);

        // Register a Workflow implementation with this Worker
        // The implementation must be known at runtime to dispatch Workflow tasks
        // Workflows are stateful so a type is needed to create instances.
        worker.registerWorkflowImplementationTypes(PostAggregatorWorkflowImpl.class);

        // Register Activity implementation(s) with this Worker.
        // The implementation must be known at runtime to dispatch Activity tasks
        // Activities are stateless and thread safe so a shared instance is used.
        worker.registerActivitiesImplementations(new PostAggregatorActivitiesImpl(connectionsUrl, postsUrl, usersUrl, ip, p));

        return worker;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void startWorker() {
        this.workerFactory.start();
    }

}
