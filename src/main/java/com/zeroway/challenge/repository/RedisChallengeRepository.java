package com.zeroway.challenge.repository;

import com.zeroway.challenge.entity.RedisChallenge;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RedisChallengeRepository extends CrudRepository<RedisChallenge, Long> {
}
