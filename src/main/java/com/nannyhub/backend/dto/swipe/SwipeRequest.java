package com.nannyhub.backend.dto.swipe;

import com.nannyhub.backend.enums.SwipeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SwipeRequest {
    private Long toUserId;
    private SwipeType type;
}