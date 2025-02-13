package com.corneliadavis.cloudnative;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.corneliadavis.cloudnative.connectionsposts.workflow.PostAggregatorWorker;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PostAggregatorWorker.class)
@TestPropertySource(properties = {
        "connectionscontroller.connectionsUrl:http://localhost:8080/connections/",
        "connectionscontroller.postsUrl:http://localhost:8080/posts?userIds=",
        "connectionscontroller.usersUrl:http://localhost:8080/users/" })
@AutoConfigureMockMvc
public class CloudnativeStatelessnessApplicationTests implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Test
    public void contextLoads() {
    }
    /*
     * @Test
     * public void actuator() throws Exception {
     * 
     * mockMvc.perform(get("/actuator/env"))
     * .andExpect(status().isOk());
     * 
     * }
     */
    /*
     * @Test
     * public void loginNoName() throws Exception {
     * mockMvc.perform(post("/login"))
     * .andExpect(status().is4xxClientError());
     * }
     * 
     * @Test
     * public void loginNamed() throws Exception {
     * mockMvc.perform(post("/login").param("username", "cdavisafc"))
     * .andExpect(cookie().exists("userToken"));
     * }
     */
    // TODO: Fix this test - it's currently an integration test in that it requires
    // connections and posts services
    // need to mock those for this test.
    /*
     * @Test
     * public void helloValidToken() throws Exception {
     * assertFalse(CloudnativeApplication.validTokens.isEmpty());
     * 
     * String validToken =
     * CloudnativeApplication.validTokens.keySet().iterator().next();
     * String validName = CloudnativeApplication.validTokens.get(validToken);
     * 
     * mockMvc.perform(get("/connectionsPosts").cookie(new Cookie("userToken",
     * validToken)))
     * .andExpect(status().isOk());
     * }
     */

}
