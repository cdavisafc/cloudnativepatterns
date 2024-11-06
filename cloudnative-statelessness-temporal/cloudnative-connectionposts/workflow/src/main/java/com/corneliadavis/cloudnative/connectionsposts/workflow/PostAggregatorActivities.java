package com.corneliadavis.cloudnative.connectionsposts.workflow;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import java.util.ArrayList;
import com.corneliadavis.cloudnative.connectionsposts.shared.PostSummary;

@ActivityInterface
public interface PostAggregatorActivities {

    @ActivityMethod
    ConnectionResult[] getConnectionsForUser(String username);

    @ActivityMethod
    PostResult[] getPostsForUsers(String ids);

    @ActivityMethod
    String getUsernameFromId(Long id);
}
