package com.corneliadavis.cloudnative;

import com.corneliadavis.cloudnative.config.CloudnativeApplication;
import com.corneliadavis.cloudnative.connections.User;
import com.corneliadavis.cloudnative.connections.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeansException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CloudnativeApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {
        "newfromconnectionscontroller.connectionsUrl:http://localhost:8080/connections/",
        "newfromconnectionscontroller.postsUrl:http://localhost:8080/posts?userIds=",
        "newfromconnectionscontroller.usersUrl:http://localhost:8080/users/"})
@AutoConfigureMockMvc
public class CloudnativeStatelessnessApplicationTests implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Autowired
    private  MockMvc mockMvc;

    @Test
    public void contextLoads() {
    }

    @Test
    public void	actuator() throws Exception {

        mockMvc.perform(get("/actuator/env"))
                .andExpect(status().isOk());
    }


    @Test
    public void	checkUserCounts() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(3)));
    }
    @Test
    public void	checkConnectionCounts() throws Exception {
        mockMvc.perform(get("/connections"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(3)));

    }

    @Test
    public void exceptionOnNonUniqueUsername() {
    UserRepository userRepository = applicationContext.getBean(UserRepository.class);
    assertThrows(Exception.class, () -> {
        userRepository.save(new User("doesn't matter", "cdavisafc"));
    });
    }

}
