package com.bowling.core.repository;

import com.bowling.core.models.Frame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrameRepository extends JpaRepository<Frame, Integer> {

    List<Frame> findByGameIdAndPlayerIdOrderByFrameNumber(Integer gameId, Integer playerId);
}
