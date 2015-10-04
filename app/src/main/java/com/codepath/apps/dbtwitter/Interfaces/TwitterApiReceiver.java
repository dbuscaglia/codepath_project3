package com.codepath.apps.dbtwitter.Interfaces;

import com.codepath.apps.dbtwitter.Models.Tweet;

import java.util.ArrayList;

/**
 * Created by danbuscaglia on 10/4/15.
 */
public interface TwitterApiReceiver {
    void handleDataCallback(ArrayList<Tweet> resultPage);
    void handleDataError(Throwable e);
}
