package com.trial.rajas.fitrack;

public class Activity {

    String task;
    String sign;
    Integer score;
    String action;

    public Activity(String task, String sign, Integer score, String action){
        this.task=task;
        this.sign=sign;
        this.score=score;
        this.action=action;
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

    public String getAction() {
        return action;
    }

    public String addToJSON(String task, String sign, Integer score, String present){
        String activityJson= new String();
        return activityJson;
    }
}