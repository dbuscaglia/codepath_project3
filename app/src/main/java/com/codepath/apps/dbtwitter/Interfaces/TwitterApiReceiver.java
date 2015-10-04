package com.codepath.apps.dbtwitter.Interfaces;

import com.codepath.apps.dbtwitter.Models.Tweet;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by danbuscaglia on 10/4/15.
 */
public interface TwitterApiReceiver {
    void handleGetTweets(ArrayList<Tweet> resultPage);
    void handleDataError(Throwable e, JSONObject response);
    void handlePostTweet(Tweet tweet);
}
