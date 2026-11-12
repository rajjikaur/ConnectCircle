
package com.connectcircle.backend.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/creator")
public class CreatorController {

    @GetMapping("/dashboard")
    public Map<String, String> creatorDashboard(
            Authentication authentication) {

        return Map.of(
                "message", "Welcome to Creator Dashboard!",
                "email", authentication.getName(),
                "role", "INFLUENCER",
                "status", "Access Granted"
        );
    }
}

