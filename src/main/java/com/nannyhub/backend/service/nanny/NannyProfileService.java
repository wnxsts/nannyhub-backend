package com.nannyhub.backend.service.nanny;

import com.nannyhub.backend.dto.profile.nanny.NannyProfileRequest;
import com.nannyhub.backend.entity.NannyProfile;
import com.nannyhub.backend.repository.NannyProfileRepository;
import com.nannyhub.backend.entity.User;
import com.nannyhub.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NannyProfileService {
    private final NannyProfileRepository nannyProfileRepository;
    private final UserRepository userRepository;


    public void create(User user,String firstName,String lastName, String city) {
        NannyProfile profile = new NannyProfile();
        profile.setUser(user);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setCity(city);
        nannyProfileRepository.save(profile);
        nannyProfileRepository.save(profile);

    }
    public void updateProfile(String email, NannyProfileRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        NannyProfile profile = nannyProfileRepository
                .findByUserId(user.getId())
                .orElseGet(() -> {
                    NannyProfile np = new NannyProfile();
                    np.setUser(user);
                    return np;
                });

        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setCity(request.getCity());
        profile.setExperienceYears(request.getExperienceYears());
        profile.setRateHour(request.getRateHour());

        nannyProfileRepository.save(profile);
    }


}
