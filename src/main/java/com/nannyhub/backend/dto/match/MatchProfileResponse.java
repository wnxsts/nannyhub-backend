package com.nannyhub.backend.dto.match;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatchProfileResponse {
    private Long userId;
    private String firstName;
    private String city;
    private Integer experienceYears;
    private Integer rateHour;
}