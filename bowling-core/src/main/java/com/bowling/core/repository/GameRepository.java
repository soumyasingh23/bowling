package com.bowling.core.repository;

import com.bowling.core.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    Game findByGameId(Integer gameId);
}

