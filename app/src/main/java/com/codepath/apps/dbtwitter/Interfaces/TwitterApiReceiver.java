package com.codepath.apps.dbtwitter.Interfaces;

import com.codepath.apps.dbtwitter.Models.Tweet;
import com.codepath.apps.dbtwitter.Models.TwitterTweetApiResponseList;
import com.codepath.apps.dbtwitter.Models.TwitterUser;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by danbuscaglia on 10/4/15.
 */
public interface TwitterApiReceiver {
    void handleGetUser(ArrayList<TwitterUser> u);
    void handleGetTweets(TwitterTweetApiResponseList resultPage);
    void handleDataError(Throwable e, JSONObject response);
    void handlePostTweet(Tweet tweet);
    void handleGetOwner(TwitterUser u);
}
