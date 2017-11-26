package com.trial.rajas.fitrack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * Created by rajas on 11/21/2017.
 */

public class JSONConversion {

    public static ArrayList<String> getListFromJSONString(String jsonString){
        JsonParser parser = new JsonParser();
        ArrayList<String> listToReturn = new ArrayList<String>();
        JsonArray user2JSONArray = parser.parse(jsonString).getAsJsonArray(); //friendsJSONString
        for (JsonElement user2JsonElement : user2JSONArray) {
            JsonObject json = user2JsonElement.getAsJsonObject();
            String user2Friend = json.get("friend").getAsString();
            listToReturn.add(user2Friend);
        }
        return listToReturn;
    }

    public static ArrayList<String> getMatchListFromJSONString(String jsonString){
        JsonParser parser = new JsonParser();
        ArrayList<String> listToReturn = new ArrayList<String>();
        JsonArray user2JSONArray = parser.parse(jsonString).getAsJsonArray(); //friendsJSONString
        for (JsonElement user2JsonElement : user2JSONArray) {
            JsonObject json = user2JsonElement.getAsJsonObject();
            String user2Friend = json.get("match").getAsString();
            listToReturn.add(user2Friend);
        }
        return listToReturn;
    }

    public static ArrayList<Activity> getListFromJSONStringForActivity(String jsonString){
        JsonParser parser = new JsonParser();
        ArrayList<Activity> listToReturn = new ArrayList<Activity>();
        JsonArray user2JSONArray = parser.parse(jsonString).getAsJsonArray(); //friendsJSONString
        for (JsonElement user2JsonElement : user2JSONArray) {
            JsonObject json = user2JsonElement.getAsJsonObject();
            String task= json.getAsJsonObject().get("task").getAsString();
            String sign= json.getAsJsonObject().get("sign").getAsString();
            Integer score= json.getAsJsonObject().get("score").getAsInt();
            Activity activity=new Activity(task, sign, score);
            listToReturn.add(activity);
        }
        return listToReturn;
    }
}
