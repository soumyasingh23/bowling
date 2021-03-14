package com.bowling.service;

import com.bowling.common.BadRequestException;
import com.bowling.common.constants.Constants;
import com.bowling.common.dto.PlayerDto;
import com.bowling.core.models.Config;
import com.bowling.core.models.Frame;
import com.bowling.core.models.Game;
import com.bowling.core.models.Player;
import com.bowling.core.repository.ConfigRepository;
import com.bowling.core.repository.FrameRepository;
import com.bowling.core.repository.GameRepository;
import com.bowling.core.repository.PlayerRepository;
import com.bowling.service.mapper.GameMapper;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    private static final Integer TOTAL_PINS = 10;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private FrameRepository frameRepository;

    @Autowired
    private GameMapper gameMapper;

    public Integer startGame(List<String> playerNames) {
        Config config = configRepository.findByConfigId(Constants.NO_OF_LANES);
        Integer noOfLanes = Integer.valueOf(config.getValue());

        Config config1 = configRepository.findByConfigId(Constants.NO_OF_PLAYERS_PER_LANE);
        Integer noOfPlayersEachLane = Integer.valueOf(config1.getValue());

        if (noOfLanes * noOfPlayersEachLane < playerNames.size()) {
            throw new BadRequestException("Maximum number of players allowed is: " + noOfLanes * noOfPlayersEachLane);
        }
        Game game = new Game();
        game.setFrameCompleted(0);
        game = gameRepository.save(game);
        List<Player> players = new ArrayList<>();
        int i=0;
        for (String name : playerNames) {
            Player player = new Player();
            player.setName(name);
            player.setGameId(game.getGameId());
            player.setLane(((i++) / noOfPlayersEachLane) + 1);
            players.add(player);
        }
        playerRepository.saveAll(players);
        return game.getGameId();
    }

    public void playNextSet(Integer gameId) {
        Game game = gameRepository.findByGameId(gameId);
        if (game == null) {
            throw new BadRequestException("Invalid gameId");
        }
        if (game.getFrameCompleted() == 10) {
            throw new BadRequestException("Game has ended");
        }
        List<Player> players = playerRepository.findByGameId(gameId);
        List<Frame> frames = new ArrayList<>();
        game.setFrameCompleted(game.getFrameCompleted() + 1);
        setScore(players, game, frames);
        playerRepository.saveAll(players);
        frameRepository.saveAll(frames);
    }

    private void setScore(List<Player> players, Game game, List<Frame> frameList) {
        Integer pinsKnocked;
        Integer pinsToKnock;
        Integer totalFrameScore;
        Integer score;
        boolean lastFrame = game.getFrameCompleted().equals(9);
        int noOfTries = lastFrame ? 3 : 2;

        for (Player player : players) {
            pinsToKnock = TOTAL_PINS;
            totalFrameScore = 0;

            for (int i = 0; i < noOfTries; ++i) {

                Frame frame = new Frame();
                frame.setNoOfTry(i + 1);
                frame.setPlayerId(player.getId());
                frame.setGameId(game.getGameId());
                frame.setFrameNumber(game.getFrameCompleted());

                pinsKnocked = RandomUtils.nextInt(0, pinsToKnock + 1);
                score = pinsKnocked;
                if (pinsKnocked.equals(TOTAL_PINS)) {
                    score += 10; //strike
                    pinsToKnock = TOTAL_PINS;
                    if (!lastFrame) {
                        frame.setScore(score);
                        frameList.add(frame);
                        break;
                    }
                } else if (pinsKnocked.equals(pinsToKnock)) {
                    score += 5; //spare
                    pinsToKnock = TOTAL_PINS;
                } else {
                    pinsToKnock = TOTAL_PINS - pinsKnocked;
                }

                frame.setScore(score);
                frameList.add(frame);
                totalFrameScore += score;

                //allow player 3rd try if player has completed spare/strike
                if (lastFrame && i == 1 && !pinsToKnock.equals(TOTAL_PINS)) {
                    break;
                }
            }

            player.setScore(player.getScore() == null ? totalFrameScore : totalFrameScore + player.getScore());
        }
    }

    public List<PlayerDto> getGameScore(Integer gameId){
        Game game = gameRepository.findByGameId(gameId);
        if (game == null) {
            throw new BadRequestException("Invalid gameId");
        }
        List<PlayerDto> playerDtoList = new ArrayList<>();
        List<Player> players = playerRepository.findByGameId(gameId);
        for(Player player: players){
            PlayerDto playerDto = gameMapper.toDto(player);
            List<Frame> frames = frameRepository.findByGameIdAndPlayerIdOrderByFrameNumber(player.getGameId(), player.getId());
            playerDto.setFrames(gameMapper.toDtoList(frames));
            playerDtoList.add(playerDto);
        }
        return playerDtoList;

    }

}
