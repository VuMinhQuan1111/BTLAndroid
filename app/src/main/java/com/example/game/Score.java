package com.example.game;

public class Score {
    public String idus;
    public int score;

    public Score() {
    }

    public Score(String id, int sc) {
        this.idus = id;
        this.score = sc;
    }

    public String getIdus() {
        return idus;
    }

    public int getScore() {
        return score;
    }

    public void setIdus(String idus) {
        this.idus = idus;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
