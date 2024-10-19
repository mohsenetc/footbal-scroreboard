import com.sportradar.model.Match;
import com.sportradar.service.Scoreboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardTest {

    private Scoreboard scoreboard;

    @BeforeEach
    void setUp() {
        scoreboard = new Scoreboard();
    }

    @Test
    void testStartNewMatch() {
        scoreboard.startMatch("Home Team", "Away Team");
        List<Match> matches = scoreboard.getMatches();

        assertEquals(1, matches.size());
        Match match = matches.getFirst();
        assertEquals("Home Team", match.getHomeTeam());
        assertEquals("Away Team", match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    @Test
    void testUpdateScore() {
        scoreboard.startMatch("Home Team", "Away Team");
        scoreboard.updateScore("Home Team", 2, 3);

        Match match = scoreboard.getMatches().getFirst();
        assertEquals(2, match.getHomeScore());
        assertEquals(3, match.getAwayScore());
    }

    @Test
    void testFinishMatch() {
        scoreboard.startMatch("Home Team", "Away Team");
        scoreboard.finishMatch("Home Team");

        List<Match> matches = scoreboard.getMatches();
        assertTrue(matches.isEmpty());
    }

    @Test
    void testGetMatchesSummary() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", 0, 5);

        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.updateScore("Spain", 10, 2);

        scoreboard.startMatch("Germany", "France");
        scoreboard.updateScore("Germany", 2, 2);

        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.updateScore("Uruguay", 6, 6);

        scoreboard.startMatch("Argentina", "Australia");
        scoreboard.updateScore("Argentina", 3, 1);

        List<Match> summary = scoreboard.getMatchesSummary();

        assertEquals(5, summary.size());
        assertEquals("Uruguay 6 - Italy 6", summary.get(0).toString());
        assertEquals("Spain 10 - Brazil 2", summary.get(1).toString());
        assertEquals("Mexico 0 - Canada 5", summary.get(2).toString());
        assertEquals("Argentina 3 - Australia 1", summary.get(3).toString());
        assertEquals("Germany 2 - France 2", summary.get(4).toString());
    }
}
