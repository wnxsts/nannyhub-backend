package com.nannyhub.backend.dto.swipe;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SwipeFeedResponse {

    private Long userId;
    private String firstName;
    private String city;
    private Integer experienceYears;
    private Integer rateHour;
}