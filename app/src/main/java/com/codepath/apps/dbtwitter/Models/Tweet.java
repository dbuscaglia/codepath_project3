package com.codepath.apps.dbtwitter.Models;

import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by danbuscaglia on 10/4/15.
 */
@Table(name = "Tweets")
public class Tweet extends Model {
    @Column(name = "uid", index = true)
    private long tweetId;
    @Column(name = "tweetBody")
    private String tweetBody;
    @Column(name = "user")
    private TwitterUser user;
    @Column(name = "timetsamp")
    private String timestamp;
    @Column(name="favorites")
    private int favorites;
    @Column(name="retweets")
    private int retweets;


    public static Tweet makeTweet(JSONObject o) {
        // body, uniqueid, createdAt
        Tweet t = new Tweet();
        try {
            t.tweetBody= o.getString("text");
            t.tweetId= o.getLong("id");
            t.timestamp = getRelativeTimeAgo(o.getString("created_at"));
            t.user = TwitterUser.makeUser(o.getJSONObject("user"));
            t.retweets = o.getInt("retweet_count");
            t.favorites = o.getInt("favorite_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return t;
    }

    public long getTweetId() {
        return tweetId;
    }
    public int getFavorites() {
        return favorites;
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return relativeDate;
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

    public TwitterUser getUser() {
        return user;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getRetweets() {
        return retweets;
    }


}
