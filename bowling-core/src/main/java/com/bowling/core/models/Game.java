package com.bowling.core.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GAME")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Integer gameId;

    @Column(name = "FRAME_COMPLETED")
    private Integer frameCompleted;

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getFrameCompleted() {
        return frameCompleted;
    }

    public void setFrameCompleted(Integer frameCompleted) {
        this.frameCompleted = frameCompleted;
    }
}
