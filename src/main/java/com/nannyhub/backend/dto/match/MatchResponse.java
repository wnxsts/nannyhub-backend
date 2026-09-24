package com.nannyhub.backend.dto.match;

public record MatchResponse(
        Long matchId,
        Long userId,
        String firstName,
        String city,
        Integer experienceYears,
        Integer rateHour
) {}
