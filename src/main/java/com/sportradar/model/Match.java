package com.sportradar.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


import java.time.LocalDateTime;


@Getter

public class Match {

    @NotBlank(message = "Home team name cannot be blank")
    private final String homeTeam;
    @NotBlank(message = "Away team name cannot be blank")
    private final String awayTeam;
    @Min(value = 0, message = "Home score must be zero or positive")
    private int homeScore;
    @Min(value = 0, message = "Away score must be zero or positive")
    private int awayScore;

    private final LocalDateTime startTime;

    public Match(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = 0;
        this.awayScore = 0;
        this.startTime = LocalDateTime.now();
    }

    public void updateScore(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public int getTotalScore() {
        return homeScore + awayScore;
    }

    @Override
    public String toString() {
        return homeTeam + " " + homeScore + " - " + awayTeam + " " +  awayScore;
    }

    public Match(Match match) {
        this.homeTeam = match.getHomeTeam();
        this.awayTeam = match.getAwayTeam();
        this.homeScore = match.getHomeScore();
        this.awayScore = match.getAwayScore();
        this.startTime = match.getStartTime();
    }
}
