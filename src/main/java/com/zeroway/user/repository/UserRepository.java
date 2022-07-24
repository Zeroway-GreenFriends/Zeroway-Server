package com.zeroway.user.repository;

import com.zeroway.challenge.dto.ChallengeRes;
import com.zeroway.common.StatusType;
import com.zeroway.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsUserByEmailAndStatus(String email, StatusType status);

    //challenge
    @Query("select new com.zeroway.challenge.dto.ChallengeRes(u.nickname, l.id, u.exp, l.imageUrl) from User u join u.level l where u.id = :userId")
    ChallengeRes findChallengeRes(@Param("userId") Long userId);

}
