package com.nannyhub.backend.controller;

import com.nannyhub.backend.dto.match.MatchProfileResponse;
import com.nannyhub.backend.dto.match.MatchResponse;
import com.nannyhub.backend.entity.Match;
import com.nannyhub.backend.entity.NannyProfile;
import com.nannyhub.backend.entity.ParentProfile;
import com.nannyhub.backend.entity.User;
import com.nannyhub.backend.enums.Role;
import com.nannyhub.backend.repository.MatchRepository;
import com.nannyhub.backend.repository.NannyProfileRepository;
import com.nannyhub.backend.repository.ParentProfileRepository;
import com.nannyhub.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final NannyProfileRepository nannyProfileRepository;
    private final ParentProfileRepository parentProfileRepository;

    @GetMapping
    public List<MatchProfileResponse> getMyMatches(Authentication auth) {
        User me = userRepository.findByEmail(auth.getName()).orElseThrow();
        List<Long> matchedUserIds = matchRepository.findMyMatches(me.getId());

        if (me.getRole() == Role.PARENT) {
            return matchedUserIds.stream()
                    .map(id -> nannyProfileRepository.findByUserId(id).orElse(null))
                    .filter(Objects::nonNull)
                    .map(p -> new MatchProfileResponse(
                            p.getUser().getId(),
                            p.getFirstName(),
                            p.getCity(),
                            p.getExperienceYears(),
                            p.getRateHour()
                    ))
                    .toList();
        }

        return matchedUserIds.stream()
                .map(id -> parentProfileRepository.findByUserId(id).orElse(null))
                .filter(Objects::nonNull)
                .map(p -> new MatchProfileResponse(
                        p.getUser().getId(),
                        p.getFirstName(),
                        p.getCity(),
                        null,
                        null
                ))
                .toList();
    }

    @GetMapping("/with-profiles")
    public List<MatchResponse> getMyMatchesWithProfiles(Authentication auth) {
        User me = userRepository.findByEmail(auth.getName()).orElseThrow();

        if (me.getRole() == Role.PARENT) {
            return matchRepository.findMatchesWithNannyProfiles(me.getId())
                    .stream()
                    .map(row -> {
                        Match m = (Match) row[0];
                        NannyProfile p = (NannyProfile) row[1];
                        return new MatchResponse(
                                m.getId(),
                                p.getUser().getId(),
                                p.getFirstName(),
                                p.getCity(),
                                p.getExperienceYears(),
                                p.getRateHour()
                        );
                    })
                    .toList();
        }

        return matchRepository.findMatchesWithParentProfiles(me.getId())
                .stream()
                .map(row -> {
                    Match m = (Match) row[0];
                    ParentProfile p = (ParentProfile) row[1];
                    return new MatchResponse(
                            m.getId(),
                            p.getUser().getId(),
                            p.getFirstName(),
                            p.getCity(),
                            null,
                            null
                    );
                })
                .toList();
    }
}
