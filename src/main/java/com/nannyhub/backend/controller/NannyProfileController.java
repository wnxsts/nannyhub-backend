package com.nannyhub.backend.controller;

import com.nannyhub.backend.dto.profile.nanny.NannyProfileRequest;
import com.nannyhub.backend.service.nanny.NannyProfileService;
import com.nannyhub.backend.entity.User;
import com.nannyhub.backend.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nanny/profile")

public class NannyProfileController {

    private final NannyProfileService nannyProfileService;
    private final UserRepository userRepository;

    public NannyProfileController(NannyProfileService nannyProfileService, UserRepository userRepository) {
        this.nannyProfileService = nannyProfileService;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('NANNY')")
    @PostMapping
    public String create(@RequestBody NannyProfileRequest request,
                         Authentication authentication
    ) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        nannyProfileService.create(
                user,
                request.getFirstName(),
                request.getLastName(),
                request.getCity()
        );

        return "Nanny profile created";
    }

    @PreAuthorize("hasRole('NANNY')")
    @PutMapping
    public void updateProfile(
            Authentication authentication,
            @RequestBody NannyProfileRequest request
    ) {
        String email = authentication.getName();
        nannyProfileService.updateProfile(email, request);
    }
}
