package com.example.resourceserver.controller;

import com.example.resourceserver.dto.GroupDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/login")
    public void login(Authentication authentication) {
        System.out.println(authentication);
    }

    @GetMapping("/admin")
    public void access(Authentication authentication) {
        System.out.println(authentication);
    }

    @PutMapping("/group")
    public void createGroup(GroupDto dto) {
        // ToDo: call KeycloakApi here
    }
}
