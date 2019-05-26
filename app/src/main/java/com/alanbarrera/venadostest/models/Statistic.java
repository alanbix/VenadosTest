package com.alanbarrera.venadostest.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Statistic implements Serializable
{
    private int position;
    private String image;
    private String team;
    private int games;
    @SerializedName("score_diff")
    private int scoreDiff;
    private int points;

    public Statistic(int position, String image, String team, int games, int scoreDiff, int points)
    {
        this.position = position;
        this.image = image;
        this.team = team;
        this.games = games;
        this.scoreDiff = scoreDiff;
        this.points = points;
    }

    public int getPosition() {
        return position;
    }

    public String getImage() {
        return image;
    }

    public String getTeam() {
        return team;
    }

    public int getGames() {
        return games;
    }

    public int getScoreDiff() {
        return scoreDiff;
    }

    public int getPoints() {
        return points;
    }
}
