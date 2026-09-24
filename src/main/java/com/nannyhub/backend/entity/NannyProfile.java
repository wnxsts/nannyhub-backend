package com.nannyhub.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nanny_profiles")
@Getter
@Setter
@NoArgsConstructor
public class NannyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "city")
    private String city;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "rate_hour")
    private Integer rateHour;

    @Column(name = "verified")
    private Boolean verified = false;
}