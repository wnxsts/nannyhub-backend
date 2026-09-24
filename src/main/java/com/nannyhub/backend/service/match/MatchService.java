package com.nannyhub.backend.service.match;

import com.nannyhub.backend.entity.User;
import com.nannyhub.backend.repository.MatchRepository;
import com.nannyhub.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    public List<Long> getMyMatches(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        return matchRepository.findByUserId(user.getId())
                .stream()
                .map(m ->
                        m.getUserAId().equals(user.getId())
                                ? m.getUserBId()
                                : m.getUserAId()
                )
                .toList();
    }
}
