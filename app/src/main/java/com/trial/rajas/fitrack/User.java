package com.trial.rajas.fitrack;

import android.content.Intent;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;

/**
 * Created by rajas on 10/18/2017.
 */

public class User {



    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static Integer getScore() {
        return score;
    }

    public static ArrayList<User> getFriends() {
        return Friends;
    }

    public static ArrayList<Activity> getUserActivities() {
        return userActivities;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    public static void setScore(Integer score) {
        User.score = score;
    }

    public static void setFriends(ArrayList<User> friends) {
        Friends = friends;
    }

    public static void setUserActivities(ArrayList<Activity> userActivities) {
        User.userActivities = userActivities;
    }

    public static void createNewUser(String username, String password, Integer score){
        setUsername(username);
        setPassword(password);
        setScore(score);
    }

    public static String username;
    public static String password;
    public static Integer score;
    public static ArrayList<User> Friends;
    public static ArrayList<Activity> userActivities;


}
