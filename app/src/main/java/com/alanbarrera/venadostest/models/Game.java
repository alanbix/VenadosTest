package com.alanbarrera.venadostest.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Game
{
    private boolean local;
    private String opponent;
    @SerializedName("opponent_image")
    private String opponentImage;
    private Date datetime;
    private String league;
    @SerializedName("home_score")
    private int homeScore;
    @SerializedName("away_score")
    private int awayScore;

    public Game(boolean local, String opponent, String opponentImage, Date datetime, String league, int homeScore, int awayScore)
    {
        this.local = local;
        this.opponent = opponent;
        this.opponentImage = opponentImage;
        this.datetime = datetime;
        this.league = league;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public boolean isLocal() {
        return local;
    }

    public String getOpponent() {
        return opponent;
    }

    public String getOpponentImage() {
        return opponentImage;
    }

    public Date getDatetime() {
        return datetime;
    }

    public String getLeague() {
        return league;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }
}
