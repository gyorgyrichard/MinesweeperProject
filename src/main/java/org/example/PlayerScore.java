package org.example;


public class PlayerScore implements Comparable<PlayerScore>{
    String name;
    int score;
    int time;

    public PlayerScore(String name, int score, int time) {
        this.name = name;
        this.score = score;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public int compareTo(PlayerScore playerScore) {
        if(this.score == playerScore.score)
            return Integer.compare(this.time, playerScore.time);
        return Integer.compare(playerScore.score, this.score);
    }
}
