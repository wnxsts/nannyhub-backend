package com.nannyhub.backend.repository;

import com.nannyhub.backend.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByUserAIdOrUserBId(Long userAId, Long userBId);

    @Query("""
        select m from Match m
        where m.userAId = :userId or m.userBId = :userId
    """)
    List<Match> findByUserId(Long userId);

    @Query("""
        select case
            when m.userAId = :userId then m.userBId
            else m.userAId
        end
        from Match m
        where (m.userAId = :userId or m.userBId = :userId)
          and m.active = true
    """)
    List<Long> findMyMatches(Long userId);

    @Query("""
        select m, np
        from Match m
        join NannyProfile np
          on np.user.id =
             case
               when m.userAId = :userId then m.userBId
               else m.userAId
             end
        where (m.userAId = :userId or m.userBId = :userId)
          and m.active = true
    """)
    List<Object[]> findMatchesWithNannyProfiles(Long userId);

    @Query("""
        select m, pp
        from Match m
        join ParentProfile pp
          on pp.user.id =
             case
               when m.userAId = :userId then m.userBId
               else m.userAId
             end
        where (m.userAId = :userId or m.userBId = :userId)
          and m.active = true
    """)
    List<Object[]> findMatchesWithParentProfiles(Long userId);

    boolean existsByUserAIdAndUserBId(Long userAId, Long userBId);
}
