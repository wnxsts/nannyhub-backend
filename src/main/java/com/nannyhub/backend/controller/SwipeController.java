package com.nannyhub.backend.controller;

import com.nannyhub.backend.dto.swipe.SwipeFeedResponse;
import com.nannyhub.backend.dto.swipe.SwipeRequest;
import com.nannyhub.backend.service.swipe.SwipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/swipes")
public class SwipeController {

    private final SwipeService swipeService;

    @PostMapping
    public Long swipe(
            Authentication authentication,
            @RequestBody SwipeRequest request
    ) {
        return swipeService.swipe(authentication.getName(), request);
    }
    @GetMapping("/feed")
    public List<SwipeFeedResponse> getFeed(Authentication authentication) {
        return swipeService.getFeed(authentication.getName());
    }

    @GetMapping("/incoming-likes")
    public List<SwipeFeedResponse> getIncomingLikes(Authentication authentication) {
        return swipeService.getIncomingLikes(authentication.getName());
    }
}

