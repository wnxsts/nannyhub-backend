package com.nannyhub.backend.repository;

import com.nannyhub.backend.entity.Swipe;
import com.nannyhub.backend.entity.User;
import com.nannyhub.backend.enums.SwipeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwipeRepository extends JpaRepository<Swipe, Long> {

    @Query("""
        select s.toUserId
        from Swipe s
        where s.fromUserId = :fromUserId
    """)
    List<Long> findToUserIdsByFromUserId(Long fromUserId);

    boolean existsByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    boolean existsByFromUserIdAndToUserIdAndType(
            Long fromUserId,
            Long toUserId,
            SwipeType type
    );

    @Query("""
        select u
        from Swipe s
        join User u on u.id = s.fromUserId
        where s.toUserId = :nannyId
          and s.type = 'LIKE'
    """)
    List<User> findParentsWhoLikedNanny(Long nannyId);
}