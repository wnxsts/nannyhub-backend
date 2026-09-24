package com.nannyhub.backend.service.swipe;

import com.nannyhub.backend.dto.swipe.SwipeFeedResponse;
import com.nannyhub.backend.dto.swipe.SwipeRequest;
import com.nannyhub.backend.entity.Chat;
import com.nannyhub.backend.entity.Match;
import com.nannyhub.backend.entity.Swipe;
import com.nannyhub.backend.entity.User;
import com.nannyhub.backend.enums.Role;
import com.nannyhub.backend.enums.SwipeType;
import com.nannyhub.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SwipeService {

    private final UserRepository userRepository;
    private final SwipeRepository swipeRepository;
    private final MatchRepository matchRepository;
    private final NannyProfileRepository nannyProfileRepository;
    private final ChatRepository chatRepository;

    public Long swipe(String fromEmail, SwipeRequest request) {

        User fromUser = userRepository.findByEmail(fromEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User toUser = userRepository.findById(request.getToUserId())
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        Long fromUserId = fromUser.getId();
        Long toUserId = toUser.getId();

//        if (swipeRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId)) {
//            return false;
//        }
        if (
                matchRepository.existsByUserAIdAndUserBId(fromUserId, toUserId)
                        || matchRepository.existsByUserAIdAndUserBId(toUserId, fromUserId)
        ) {
            return null;
        }

        if (swipeRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId)) {
            return null;
        }


        Swipe swipe = Swipe.builder()
                .fromUserId(fromUserId)
                .toUserId(toUserId)
                .type(request.getType())
                .createdAt(LocalDateTime.now())
                .build();

        swipeRepository.save(swipe);

        if (request.getType() == SwipeType.LIKE) {
            boolean mutual = swipeRepository
                    .existsByFromUserIdAndToUserIdAndType(
                            toUserId,
                            fromUserId,
                            SwipeType.LIKE
                    );

            if (mutual) {
                Match match = Match.builder()
                        .userAId(fromUserId)
                        .userBId(toUserId)
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .build();

                Match savedMatch = matchRepository.save(match);

                Chat chat = Chat.builder()
                        .matchId(savedMatch.getId())
                        .build();

                chatRepository.save(chat);

                return savedMatch.getId();
            }
        }

        return null;
    }

    public List<SwipeFeedResponse> getFeed(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Long> swipedUserIds =
                swipeRepository.findToUserIdsByFromUserId(user.getId());

        if (user.getRole() == Role.PARENT) {
            return nannyProfileRepository.findAll()
                    .stream()
                    .filter(p -> !p.getUser().getId().equals(user.getId()))
                    .filter(p -> !swipedUserIds.contains(p.getUser().getId()))
                    .map(p -> new SwipeFeedResponse(
                            p.getUser().getId(),
                            p.getFirstName(),
                            p.getCity(),
                            p.getExperienceYears(),
                            p.getRateHour()
                    ))
                    .toList();
        }

        return List.of();

    }

    public List<SwipeFeedResponse> getIncomingLikes(String email) {

        User nanny = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (nanny.getRole() != Role.NANNY) {
            return List.of();
        }

        return swipeRepository.findParentsWhoLikedNanny(nanny.getId())
                .stream()
                .map(parent -> new SwipeFeedResponse(
                        parent.getId(),
                        parent.getEmail(),
                        null,
                        null,
                        null
                ))
                .toList();
    }
}

