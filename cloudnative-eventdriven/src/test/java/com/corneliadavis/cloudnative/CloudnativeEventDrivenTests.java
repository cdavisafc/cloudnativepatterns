package com.corneliadavis.cloudnative;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.corneliadavis.cloudnative.config.CloudnativeApplication;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CloudnativeApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class CloudnativeEventDrivenTests {

        @Autowired
        private MockMvc mockMvc;

        @Test
        public void contextLoads() {
        }

        @Test
        public void actuator() throws Exception {

                mockMvc.perform(get("/actuator/env"))
                                .andExpect(status().isOk());
        }

        @Test
        public void checkUserCounts() throws Exception {
                mockMvc.perform(get("/users"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType("application/json"))
                                .andExpect(jsonPath("$", hasSize(3)));
        }

        @Test
        public void checkPostCounts() throws Exception {

                mockMvc.perform(get("/posts"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType("application/json"))
                                .andExpect(jsonPath("$", hasSize(4)));

        }

        @Test
        public void checkConnectionCounts() throws Exception {
                mockMvc.perform(get("/connections"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType("application/json"))
                                .andExpect(jsonPath("$", hasSize(3)));

        }

        @Test
        public void checkNewPostCounts() throws Exception {
                mockMvc.perform(get("/connectionsposts/cdavisafc"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType("application/json"))
                                .andExpect(jsonPath("$", hasSize(2)));
        }

}
