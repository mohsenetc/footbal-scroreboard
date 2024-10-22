package com.sportradar.service;

import com.sportradar.model.Match;
import com.sportradar.model.NotValidMatchException;
import com.sportradar.model.ScoreHolder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Scoreboard {
    private final ConcurrentHashMap<String, Match> matches;

    private final Validator validator;

    public Scoreboard(Validator validator) {
        this.matches = new ConcurrentHashMap<>();
        this.validator = validator;
    }

    public void startMatch(@Valid Match match) {
        matches.put(match.getHomeTeam(), match);
    }

    public synchronized void updateScore(String homeTeam,
                            @Min(value = 0, message = "Home score must be zero or positive") int homeScore,
                            @Min(value = 0, message = "Away score must be zero or positive") int awayScore)
            throws NotValidMatchException {

        // Validate scores
        ScoreHolder scoreHolder = new ScoreHolder(homeScore, awayScore);
        Errors errors = new BeanPropertyBindingResult(scoreHolder, "scoreHolder");
        validator.validate(scoreHolder, errors);

        if (errors.hasErrors()) {
            throw new NotValidMatchException(errors.getAllErrors().getFirst().getDefaultMessage());
        }

        Match match = matches.get(homeTeam);
        if (match == null) {
            throw new NotValidMatchException("Match not found for the home team: " + homeTeam);
        }

        match.updateScore(homeScore, awayScore);
    }


    public synchronized void finishMatch(String homeTeam) {
        matches.remove(homeTeam);
    }

    public synchronized void finishAllMatches(){
        matches.clear();
    }

    public List<Match> getMatches() {
        return new ArrayList<>(matches.values());
    }

    public Optional<Match> getMatchByHomeTeam(String homeTeam) {
        return Optional.ofNullable(matches.get(homeTeam));
    }

    public List<Match> getMatchesSummary() {
        List<Match> summary = new ArrayList<>(this.getMatches());
        summary.sort(Comparator.comparingInt(Match::getTotalScore).reversed()
                .thenComparing(Comparator.comparing(Match::getStartTime).reversed()));
        return summary;
    }
}
