package com.sportradar;

import com.sportradar.model.Match;
import com.sportradar.model.NotValidMatchException;
import com.sportradar.service.Scoreboard;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardTest {

    private Scoreboard scoreboard;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.afterPropertiesSet();
        scoreboard = new Scoreboard(validatorFactoryBean);
    }

    @Test
    @SneakyThrows
    void testStartNewMatch() {
        scoreboard.startMatch(new Match("Home Team", "Away Team"));
        List<Match> matches = scoreboard.getMatches();

        assertEquals(1, matches.size());
        Match match = matches.getFirst();
        assertEquals("Home Team", match.getHomeTeam());
        assertEquals("Away Team", match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());

    }

    @Test
    @SneakyThrows
    void testUpdateScore() {
        scoreboard.startMatch(new Match("Home Team", "Away Team"));
        scoreboard.updateScore("Home Team", 2, 3);

        Match match = scoreboard.getMatches().getFirst();
        assertEquals(2, match.getHomeScore());
        assertEquals(3, match.getAwayScore());
    }

    @Test
    @SneakyThrows
    void testFinishMatch() {
        scoreboard.startMatch(new Match("Home Team", "Away Team"));
        scoreboard.finishMatch("Home Team");

        List<Match> matches = scoreboard.getMatches();
        assertTrue(matches.isEmpty());
    }

    @Test
    @SneakyThrows
    void testFinishAllMatch() {
        scoreboard.startMatch(new Match("Home Team", "Away Team"));
        scoreboard.startMatch(new Match("UK", "US"));
        scoreboard.finishAllMatches();

        List<Match> matches = scoreboard.getMatches();
        assertTrue(matches.isEmpty());
    }

    @Test
    void testGetMatchHomeTeamAvailable(){
        scoreboard.startMatch(new Match("IRAN", "UAE"));
        Optional<Match> iran = scoreboard.getMatchByHomeTeam("IRAN");
        assertTrue(iran.isPresent());
    }

    @Test
    void testGetMatchHomeTeamNotAvailable(){
        scoreboard.startMatch(new Match("USA", "UAE"));
        Optional<Match> iran = scoreboard.getMatchByHomeTeam("UK");
        assertTrue(iran.isEmpty());
    }

    @Test
    @SneakyThrows
    void testUpdateInvalidMatchShouldThrowException() {
        scoreboard.startMatch(new Match("UK", "USA"));
        assertThrows(NotValidMatchException.class, () -> scoreboard.updateScore("UAE", 2, 3));
    }

    @Test
    @SneakyThrows
    void testGetMatchesSummary() {
        scoreboard.startMatch(new Match("Mexico", "Canada"));
        scoreboard.updateScore("Mexico", 0, 5);

        scoreboard.startMatch(new Match("Spain", "Brazil"));
        scoreboard.updateScore("Spain", 10, 2);

        scoreboard.startMatch(new Match("Germany", "France"));
        scoreboard.updateScore("Germany", 2, 2);

        scoreboard.startMatch(new Match("Uruguay", "Italy"));
        scoreboard.updateScore("Uruguay", 6, 6);

        scoreboard.startMatch(new Match("Argentina", "Australia"));
        scoreboard.updateScore("Argentina", 3, 1);

        List<Match> summary = scoreboard.getMatchesSummary();

        assertEquals(5, summary.size());
        assertEquals("Uruguay 6 - Italy 6", summary.get(0).toString());
        assertEquals("Spain 10 - Brazil 2", summary.get(1).toString());
        assertEquals("Mexico 0 - Canada 5", summary.get(2).toString());
        assertEquals("Argentina 3 - Australia 1", summary.get(3).toString());
        assertEquals("Germany 2 - France 2", summary.get(4).toString());
    }


    @Test
    @SneakyThrows
    void testHomeScoreMustBeZeroOrPositive() {
        scoreboard.startMatch(new Match("UK", "USA"));
        assertThrows(NotValidMatchException.class, () -> scoreboard.updateScore("UK", -1, 0));
    }

    @Test
    @SneakyThrows
    void testAwayScoreMustBeZeroOrPositive() {
        scoreboard.startMatch(new Match("UK", "USA"));
        assertThrows(NotValidMatchException.class, () -> scoreboard.updateScore("UK", 0, -1));
    }
}