package com.zeroway.challenge.repository;

import com.zeroway.challenge.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface LevelRepository extends JpaRepository<Level, Integer>, LevelRepositoryCustom {


}
