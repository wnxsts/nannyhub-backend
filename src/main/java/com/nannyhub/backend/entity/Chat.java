package com.nannyhub.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chats", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"match_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_id", nullable = false, unique = true)
    private Long matchId;
}