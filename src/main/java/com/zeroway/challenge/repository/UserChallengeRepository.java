package com.zeroway.challenge.repository;

import com.zeroway.challenge.dto.ChallengeListRes;
import com.zeroway.challenge.entity.User_Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChallengeRepository extends JpaRepository<User_Challenge, Long> {

    @Query("select uc from User_Challenge uc where uc.challengeId.id = :challengeId and uc.userId.id = :userId and uc.complete = false")
    User_Challenge findChallengeList(@Param("challengeId") Long challengeId, @Param("userId") long userId);
}
