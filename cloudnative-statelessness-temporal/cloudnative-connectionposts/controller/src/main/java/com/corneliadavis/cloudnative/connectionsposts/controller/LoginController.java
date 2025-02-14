package com.corneliadavis.cloudnative.connectionsposts.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    private StringRedisTemplate template;

    @Autowired
    public LoginController(StringRedisTemplate template) {
        this.template = template;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void whoareyou(@RequestParam(value = "username", required = false) String username,
            HttpServletResponse response) {

        if (username == null)
            response.setStatus(400);
        else {
            UUID uuid = UUID.randomUUID();
            String userToken = uuid.toString();

            ValueOperations<String, String> ops = this.template.opsForValue();
            ops.set(userToken, username);
            response.addCookie(new Cookie("userToken", userToken));
        }
    }

}
