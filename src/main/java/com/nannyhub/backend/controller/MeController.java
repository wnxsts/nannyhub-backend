package com.nannyhub.backend.controller;

import com.nannyhub.backend.dto.profile.MeResponse;
import com.nannyhub.backend.service.profile.MeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me")
public class MeController {
    private final MeService meService;

    @GetMapping
    public MeResponse getMe(Authentication authentication) {
        String email = authentication.getName(); // email из JWT
        return meService.getMeByEmail(email);
    }
}
