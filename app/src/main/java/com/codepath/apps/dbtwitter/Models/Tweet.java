package com.codepath.apps.dbtwitter.Models;

import android.text.format.DateUtils;

import com.activeandroid.Model;
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

    private long tweetId;

    private String tweetBody;
    private TwitterUser user;
    private String timestamp;
    private int stars;
    private int retweets;

    public long getTweetId() {
        return tweetId;
    }

    public static Tweet makeTweet(JSONObject o) {
        // body, uniqueid, createdAt
        Tweet t = new Tweet();
        try {
            t.tweetBody= o.getString("text");
            t.tweetId= o.getLong("id");
            t.timestamp = getRelativeTimeAgo(o.getString("created_at"));
            t.user = TwitterUser.makeUser(o.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return t;
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
