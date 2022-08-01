package com.zeroway.challenge.repository;


import com.zeroway.challenge.entity.Challenge;
import com.zeroway.challenge.entity.User_Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;



@Repository
public interface UserChallengeRepository extends JpaRepository<User_Challenge, Long> {

    User_Challenge findByUser_IdAndChallenge_Id(Long userId, Long challengeId);

    @Query("select uc from User_Challenge uc left join uc.challenge c where (uc.user.id = :userId and c.level.id = :userLevel and uc.complete=false) or (uc.user.id = :userId and c.level.id = :userLevel and uc.updatedAt > :todayLdt and uc.complete=true)")
    List<User_Challenge> findTodayChallenge(@Param("userId") Long userId, @Param("userLevel") Integer userLevel, @Param("todayLdt") LocalDateTime todayLdt);

}
