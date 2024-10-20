package com.sportradar.service;

import com.sportradar.model.Match;
import com.sportradar.model.NotValidMatchException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Scoreboard {
    private final ConcurrentHashMap<String, Match> matches;

    @Autowired
    private Validator validator;


    public Scoreboard() {
        this.matches = new ConcurrentHashMap<>();
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


    private record ScoreHolder(@Min(value = 0, message = "Home score must be zero or positive") int homeScore,
                               @Min(value = 0, message = "Away score must be zero or positive") int awayScore) {
        private ScoreHolder(int homeScore, int awayScore) {
            this.homeScore = homeScore;
            this.awayScore = awayScore;
        }

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

    public List<Match> getMatchesSummary() {
        List<Match> summary = new ArrayList<>(this.getMatches());
        summary.sort(Comparator.comparingInt(Match::getTotalScore).reversed()
                .thenComparing(Comparator.comparing(Match::getStartTime).reversed()));
        return summary;
    }
}
