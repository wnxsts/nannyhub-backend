package com.nannyhub.backend.service.profile;

import com.nannyhub.backend.dto.profile.MeResponse;
import com.nannyhub.backend.entity.NannyProfile;
import com.nannyhub.backend.entity.ParentProfile;
import com.nannyhub.backend.entity.User;
import com.nannyhub.backend.enums.Role;
import com.nannyhub.backend.repository.NannyProfileRepository;
import com.nannyhub.backend.repository.ParentProfileRepository;
import com.nannyhub.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeService {

    private final UserRepository userRepository;
    private final NannyProfileRepository nannyProfileRepository;
    private final ParentProfileRepository parentProfileRepository;

    public MeResponse getMeByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.NANNY) {
            var profile = nannyProfileRepository
                    .findByUserId(user.getId())
                    .orElseGet(() -> {
                        NannyProfile p = new NannyProfile();
                        p.setUser(user);
                        return nannyProfileRepository.save(p);
                    });

            return new MeResponse(
                    user.getEmail(),
                    user.getRole().name(),
                    profile.getFirstName(),
                    profile.getExperienceYears(),
                    profile.getRateHour(),
                    profile.getVerified()
            );
        }

        if (user.getRole() == Role.PARENT) {
            var profile = parentProfileRepository
                    .findByUserId(user.getId())
                    .orElseGet(() -> {
                        ParentProfile p = new ParentProfile();
                        p.setUser(user);
                        return parentProfileRepository.save(p);
                    });
            return new MeResponse(
                    user.getEmail(),
                    user.getRole().name(),
                    profile.getFirstName(),
                    null,
                    null,
                    true
            );
        }

        throw new IllegalStateException("Unknown role");
    }
}