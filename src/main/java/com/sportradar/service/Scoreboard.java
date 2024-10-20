package com.sportradar.service;

import com.sportradar.model.Match;
import com.sportradar.model.NotValidMatchException;
import jakarta.validation.*;

import java.util.*;

public class Scoreboard {
    private final HashMap<String,Match> matches;
    private final Validator validator; // Add a validator

    public Scoreboard() {
        this.matches = new LinkedHashMap<>();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    public void startMatch(String homeTeam, String awayTeam) throws NotValidMatchException {
        Match match = new Match(homeTeam, awayTeam);
        validateMatch(match);
        matches.put(homeTeam, match);
    }
    private void validateMatch(Match match) throws NotValidMatchException {
        Set<ConstraintViolation<Match>> violations = validator.validate(match);
        if (!violations.isEmpty()) {
            // Collect and throw the validation messages
            StringBuilder message = new StringBuilder();
            for (ConstraintViolation<Match> violation : violations) {
                message.append(violation.getMessage()).append("; ");
            }
            throw new NotValidMatchException(message.toString());
        }
    }

    public void updateScore(String homeTeam, int homeScore, int awayScore) throws NotValidMatchException {

        Match match = matches.get(homeTeam);
        if (match == null) {
            throw new NotValidMatchException("Match not found for the home team: " + homeTeam);
        }
        Match tempMatch = new Match(match);
        tempMatch.updateScore(homeScore, awayScore);
        validateMatch(tempMatch);
        match.updateScore(homeScore, awayScore);

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
