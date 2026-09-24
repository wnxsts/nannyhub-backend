package com.nannyhub.backend.dto.profile.nanny;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NannyProfileRequest {
    private String firstName;
    private String lastName;
    private String city;
    private Integer experienceYears;
    private Integer rateHour;
}