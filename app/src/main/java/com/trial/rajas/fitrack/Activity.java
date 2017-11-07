package com.trial.rajas.fitrack;

public class Activity {

    String task;
    Integer score;
    String sign;

    public Activity(String task, Integer score, String sign){
        this.task=task;
        this.score=score;
        this.sign=sign;
    }

    public String getTask() {
        return task;
    }

    public Integer getScore() {
        return score;
    }

    public String getSign() {
        return sign;
    }
}
