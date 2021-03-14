package com.bowling.service;

import com.bowling.common.BadRequestException;
import com.bowling.common.constants.Constants;
import com.bowling.core.models.Config;
import com.bowling.core.models.Game;
import com.bowling.core.models.Player;
import com.bowling.core.repository.ConfigRepository;
import com.bowling.core.repository.FrameRepository;
import com.bowling.core.repository.GameRepository;
import com.bowling.core.repository.PlayerRepository;
import com.bowling.service.mapper.GameMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@PrepareForTest({GameService.class})
public class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ConfigRepository configRepository;

    @Mock
    private FrameRepository frameRepository;

    @Mock
    private GameMapper gameMapper;

    @Test(expected = BadRequestException.class)
    public void testTooManyPlayers() {
        Mockito.when(configRepository.findByConfigId(Constants.NO_OF_LANES)).thenReturn(noOfLanesConfig());
        Mockito.when(configRepository.findByConfigId(Constants.NO_OF_PLAYERS_PER_LANE)).thenReturn(playersPerLaneConfig());
        gameService.startGame(Arrays.asList("a", "b", "c"));
    }

    @Test
    public void testStartGame() {
        List<Player> players = new ArrayList<>();
        Game game = new Game();
        game.setGameId(1);
        Mockito.when(configRepository.findByConfigId(Constants.NO_OF_LANES)).thenReturn(noOfLanesConfig());
        Mockito.when(configRepository.findByConfigId(Constants.NO_OF_PLAYERS_PER_LANE)).thenReturn(playersPerLaneConfig());
        Mockito.when(playerRepository.saveAll(Mockito.<Player>anyList())).thenReturn(players);
        Mockito.when(gameRepository.save(Mockito.any(Game.class))).thenReturn(game);
        Integer gameId = gameService.startGame(Arrays.asList("a", "b"));
        Assert.assertEquals(gameId, game.getGameId());
    }

    @Test(expected = BadRequestException.class)
    public void testGameIsNull() {
        Mockito.when(gameRepository.findByGameId(Mockito.anyInt())).thenReturn(null);
        gameService.playNextSet(1);
    }

    @Test(expected = BadRequestException.class)
    public void testGameHasEnded() {
        Game game = new Game();
        game.setFrameCompleted(10);
        Mockito.when(gameRepository.findByGameId(Mockito.anyInt())).thenReturn(game);
        gameService.playNextSet(1);
    }

    private Config noOfLanesConfig() {
        Config config = new Config();
        config.setConfigId(Constants.NO_OF_LANES);
        config.setValue("2");
        return config;
    }

    private Config playersPerLaneConfig() {
        Config config = new Config();
        config.setConfigId(Constants.NO_OF_PLAYERS_PER_LANE);
        config.setValue("1");
        return config;
    }
}
