package com.nannyhub.backend.controller;

import com.nannyhub.backend.dto.profile.parent.ParentProfileRequest;
import com.nannyhub.backend.service.parent.ParentProfileService;
import com.nannyhub.backend.entity.User;
import com.nannyhub.backend.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/parent/profile")

public class ParentProfileController {
    private  final ParentProfileService parentProfileService;
    private  final UserRepository userRepository;

    public ParentProfileController(ParentProfileService parentProfileService, UserRepository userRepository) {
        this.parentProfileService = parentProfileService;
        this.userRepository = userRepository;
    }
    @PreAuthorize("hasRole('PARENT')")
    @PostMapping
    public String create(@RequestBody ParentProfileRequest request,
                         Authentication authentication
    ) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        parentProfileService.create(
                user,
                request.getFirstName(),
                request.getLastName(),
                request.getCity()
        );

        return "Parent profile created";
    }

    @PutMapping
    @PreAuthorize("hasRole('PARENT')")
    public void updateProfile(
            Authentication authentication,
            @RequestBody ParentProfileRequest request
    ) {
        parentProfileService.updateProfile(
                authentication.getName(),
                request
        );
    }

}
