package com.trial.rajas.fitrack;

public class Activity {

    String task;
    String sign;
    Integer score;

    public Activity(String task, String sign, Integer score){
        this.task=task;
        this.sign=sign;
        this.score=score;
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

    public String addToJSON(String task, String sign, Integer score, String present){
        String activityJson= new String();
        return activityJson;
    }
}