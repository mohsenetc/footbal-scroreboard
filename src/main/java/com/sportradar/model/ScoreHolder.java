package com.sportradar.model;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class ScoreHolder{
    @Min(value = 0, message = "Home score must be zero or positive")
    private final int homeScore;
    @Min(value = 0, message = "Away score must be zero or positive")
    private final int awayScore;
    public ScoreHolder( int homeScore,
                        int awayScore) {
            this.homeScore = homeScore;
            this.awayScore = awayScore;
        }

    }