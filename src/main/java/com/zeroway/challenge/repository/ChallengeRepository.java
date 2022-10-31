package com.zeroway.challenge.repository;

import com.zeroway.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
