package com.nannyhub.backend.entity;

import com.nannyhub.backend.enums.SwipeType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "swipes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"from_user_id", "to_user_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Swipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_user_id", nullable = false)
    private Long fromUserId;

    @Column(name = "to_user_id", nullable = false)
    private Long toUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SwipeType type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}