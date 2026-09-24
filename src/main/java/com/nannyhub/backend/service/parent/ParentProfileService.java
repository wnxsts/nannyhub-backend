package com.nannyhub.backend.service.parent;

import com.nannyhub.backend.dto.profile.parent.ParentProfileRequest;
import com.nannyhub.backend.entity.ParentProfile;
import com.nannyhub.backend.repository.ParentProfileRepository;
import com.nannyhub.backend.entity.User;
import com.nannyhub.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentProfileService {

    private final ParentProfileRepository repository;
    private final UserRepository userRepository;

    public void create(User user, String firstName, String lastName, String city) {
        ParentProfile profile = new ParentProfile();
        profile.setUser(user);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setCity(city);
        repository.save(profile);
    }

    public void updateProfile(String email, ParentProfileRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ParentProfile profile = repository
                .findByUserId(user.getId())
                .orElseGet(() -> {
                    ParentProfile pp = new ParentProfile();
                    pp.setUser(user);
                    return pp;
                });

        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setCity(request.getCity());

        repository.save(profile);
    }
}