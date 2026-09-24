package com.nannyhub.backend.dto.profile.parent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParentProfileRequest {
    private String firstName;
    private String lastName;
    private String city;
}