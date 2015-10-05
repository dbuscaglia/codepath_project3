package com.codepath.apps.dbtwitter.Models;

import java.util.ArrayList;

/**
 * Created by danbuscaglia on 10/4/15.
 */
public class TwitterTweetApiResponseList {
    private ArrayList<Tweet> tweets;
    private boolean shouldRefresh;

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    public boolean isShouldRefresh() {
        return shouldRefresh;
    }

    public TwitterTweetApiResponseList(ArrayList<Tweet> tweets, boolean shouldRefresh) {

        this.tweets = tweets;
        this.shouldRefresh = shouldRefresh;
    }
}
