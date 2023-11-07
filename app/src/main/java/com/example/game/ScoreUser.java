package com.example.game;

public class ScoreUser {
    public String idus;
    public int score;
    public String name;

    public ScoreUser() {
    }

    public ScoreUser(String id,String name, int sc ) {

        this.idus = id;
        this.name=name;
        this.score = sc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
