package com.sportradar.service;

import com.sportradar.model.Match;
import com.sportradar.model.NotValidMatchException;

import java.util.*;

public class Scoreboard {
    private final HashMap<String,Match> matches;
    public Scoreboard() {
        this.matches = new LinkedHashMap<>();
    }

    public void startMatch(String homeTeam, String awayTeam) {
        Match match = new Match(homeTeam, awayTeam);
        matches.put(homeTeam,match);
    }

    public void updateScore(String homeTeam, int homeScore, int awayScore) throws NotValidMatchException {
        if (!matches.containsKey(homeTeam)) {
            throw new NotValidMatchException("Match not found for the home team: " + homeTeam);
        }
        matches.get(homeTeam).updateScore(homeScore, awayScore);
    }

    public void finishMatch(String homeTeam) {
        matches.remove(homeTeam);
    }

    public List<Match> getMatches() {
        return matches.values().stream().toList();
    }

    public List<Match> getMatchesSummary() {
        List<Match> summary = new ArrayList<>(this.getMatches());
        summary.sort(Comparator.comparingInt(Match::getTotalScore).reversed()
                .thenComparing(Comparator.comparing(Match::getStartTime).reversed()));
        return summary;
    }
}
