package com.codepath.apps.dbtwitter.Fragments;

import android.os.Bundle;

import com.codepath.apps.dbtwitter.Helpers.ConnectionHelper;
import com.codepath.apps.dbtwitter.Models.TwitterTweetApiResponseList;

/**
 * Created by danbuscaglia on 10/10/15.
 */
public class TweetsHomeStreamFragment extends TweetStreamFragment {

    @Override
    public void populateTimeline() {
        if (ConnectionHelper.isConnected(this.getContext())) {
            client.getHomeTimeline(oldestTweetInMemory, this, false);
        } else {
            this.swipeContainer.setRefreshing(false);
        }
    }
    @Override
    public void refreshTimeline() {
        if (ConnectionHelper.isConnected(this.getContext())) {
            client.getHomeTimeline(-1, this, true);
        } else {
            this.swipeContainer.setRefreshing(false);
        }
    }

    public static TweetsHomeStreamFragment newInstance(int pageNumber, String fragmentTitle) {
        TweetsHomeStreamFragment streamFragment = new TweetsHomeStreamFragment();
        Bundle args = new Bundle();
        args.putInt("pageNumber", pageNumber);
        args.putString("fragmentTitle", fragmentTitle);
        streamFragment.setArguments(args);
        return streamFragment;
    }

}
