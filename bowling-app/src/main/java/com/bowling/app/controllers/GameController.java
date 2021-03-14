package com.bowling.app.controllers;

import com.bowling.common.dto.PlayerDto;
import com.bowling.service.GameService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @ApiOperation(value = "Start game")
    @PostMapping(value = "/start")
    public Integer startGame(@RequestParam(value = "playerNames") List<String> players) {
        return gameService.startGame(players);
    }

    @ApiOperation(value = "Play next set")
    @PostMapping(value = "play/{gameId}")
    public void startGame(@PathVariable(value = "gameId") Integer gameId) {//not null?
        gameService.playNextSet(gameId);
    }

    @ApiOperation(value = "Get game score")
    @GetMapping(value = "/score/{gameId}")
    public List<PlayerDto> getScore(@PathVariable(value = "gameId") Integer gameId) {
        return gameService.getGameScore(gameId);
    }
}
