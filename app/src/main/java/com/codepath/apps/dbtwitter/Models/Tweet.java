package com.codepath.apps.dbtwitter.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by danbuscaglia on 10/4/15.
 */
public class Tweet {

    private long tweetId;

    private String tweetBody;
    private TwitterUser user;
    private String timestamp;
    private int stars;
    private int retweets;



    public static Tweet makeTweet(JSONObject o) {
        // body, uniqueid, createdAt
        Tweet t = new Tweet();
        try {
            t.tweetBody= o.getString("text");
            t.tweetId= o.getLong("id");
            t.timestamp = o.getString("created_at");
            t.user = TwitterUser.makeUser(o.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray a) {
        ArrayList<Tweet> result = new ArrayList<Tweet>();
        for (int i = 0; i < a.length(); i++) {
            try {
                JSONObject tweetJSON = a.getJSONObject(i);
                Tweet t = makeTweet(tweetJSON);
                if (t != null) {
                    result.add(t);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return result;
    }

    public String getTweetBody() {
        return tweetBody;
    }

    public void setTweetBody(String tweetBody) {
        this.tweetBody = tweetBody;
    }

    public TwitterUser getUser() {
        return user;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStars() {
        return stars;
    }

    public int getRetweets() {
        return retweets;
    }


}
