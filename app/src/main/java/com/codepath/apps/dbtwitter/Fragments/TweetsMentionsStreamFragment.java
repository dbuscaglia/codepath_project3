package com.codepath.apps.dbtwitter.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.dbtwitter.Helpers.ConnectionHelper;
import com.codepath.apps.dbtwitter.Models.Tweet;
import com.codepath.apps.dbtwitter.Models.TwitterTweetApiResponseList;

import java.util.ArrayList;

/**
 * Created by danbuscaglia on 10/10/15.
 */
public class TweetsMentionsStreamFragment extends TweetStreamFragment {

    @Override
    public void populateTimeline() {
        if (ConnectionHelper.isConnected(this.getContext())) {
            client.getMentionsTimeline(oldestTweetInMemory, this, false);
        } else {
            this.swipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void refreshTimeline() {
        if (ConnectionHelper.isConnected(this.getContext())) {
            client.getMentionsTimeline(-1, this, true);
        } else {
            this.swipeContainer.setRefreshing(false);
        }
    }
    public static TweetsMentionsStreamFragment newInstance(int pageNumber, String fragmentTitle) {
        TweetsMentionsStreamFragment streamFragment = new TweetsMentionsStreamFragment();
        Bundle args = new Bundle();
        args.putInt("pageNumber", pageNumber);
        args.putString("fragmentTitle", fragmentTitle);
        streamFragment.setArguments(args);
        return streamFragment;
    }

}
