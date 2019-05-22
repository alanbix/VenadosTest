package com.alanbarrera.venadostest.models;

public class Statistic
{
    private int position;
    private String image;
    private String team;
    private int games;
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
