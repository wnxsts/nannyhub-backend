package com.nannyhub.backend.dto.swipe;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SwipeResultResponse {
    private boolean matched;
    private Long chatId;
}
