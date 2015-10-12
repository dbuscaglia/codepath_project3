package com.codepath.apps.dbtwitter.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.codepath.apps.dbtwitter.Activities.TwitterApplication;
import com.codepath.apps.dbtwitter.Activities.TwitterClient;
import com.codepath.apps.dbtwitter.Activities.TwitterTimeline;
import com.codepath.apps.dbtwitter.Adapters.TweetsArrayAdapter;
import com.codepath.apps.dbtwitter.Helpers.ConnectionHelper;
import com.codepath.apps.dbtwitter.Helpers.EndlessScrollListener;
import com.codepath.apps.dbtwitter.Interfaces.TwitterApiReceiver;
import com.codepath.apps.dbtwitter.Models.Tweet;
import com.codepath.apps.dbtwitter.Models.TwitterTweetApiResponseList;
import com.codepath.apps.dbtwitter.Models.TwitterUser;
import com.codepath.apps.dbtwitter.R;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by danbuscaglia on 10/10/15.
 */
public class TweetStreamFragment extends Fragment implements TwitterApiReceiver {

    public TwitterClient client;
    public BroadcastReceiver mTweetReceiver;
    protected Context context;
    protected ArrayList<Tweet> tweetStream;
    protected TweetsArrayAdapter adapter;
    protected ListView lvTweets;
    protected long oldestTweetInMemory;  // used for infinite scrolling
    protected Toolbar toolbar;
    protected SwipeRefreshLayout swipeContainer;
    protected TwitterApiReceiver mStream;
    protected int page;
    protected String title;
    protected boolean outOfData;
    protected boolean initializedScrollListener;
    protected TwitterUser user;
    protected View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        view = v;
        outOfData = false;
        initMenu();
        initFragmentState(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mTweetReceiver);
    }

    protected void initFragmentState(View v) {
        initContextAndClient(v);
        initStream();
        initListeners(v);
    }

    protected void initMenu() {
        addImageToMenuBar();
        setHasOptionsMenu(true);
    }

    protected void initStream() {
        populateTimeline();
    }

    protected void initListeners(View v) {
        initializedScrollListener = false;
        this.swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        this.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeline();
            }
        });
        // Configure the refreshing colors
        this.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        this.adapter.setOnTweetClickedListener(new TweetsArrayAdapter.OnTweetClickedListener() {
            @Override
            public void onTweetClicked(int position) {
                displayTweet(adapter.getItem(position));
            }

            @Override
            public void onProfileClicked(int position) {
                showProfile(adapter.getItem(position));
            }

            @Override
            public void onRetweetClicked(int position) {
                retweetTweet(adapter.getItem(position));
            }
        });

    }

    private void displayTweet(Tweet t) {
        Toast.makeText(this.context, "test", Toast.LENGTH_SHORT).show();
    }

    private void showProfile(Tweet t) {
        TwitterTimeline timeline = (TwitterTimeline) getActivity();
        timeline.showProfileByID(t.getUser());
    }

    private void retweetTweet(Tweet t) {
        Toast.makeText(this.context, "test", Toast.LENGTH_LONG).show();
    }

    protected void initContextAndClient(View v) {
        oldestTweetInMemory = -1;
        mStream = this;
        client = TwitterApplication.getRestClient();
        lvTweets = (ListView) v.findViewById(R.id.rvTwitterStream);
        tweetStream = new ArrayList<>();
        adapter = new TweetsArrayAdapter(this.getContext(), tweetStream);
        lvTweets.setAdapter(adapter);
        context = getActivity();
    }

    public static TweetStreamFragment newInstance(int pageNumber, String fragmentTitle) {
        TweetStreamFragment streamFragment = new TweetStreamFragment();
        Bundle args = new Bundle();
        args.putInt("pageNumber", pageNumber);
        args.putString("fragmentTitle", fragmentTitle);
        streamFragment.setArguments(args);
        return streamFragment;
    }

    // creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addImageToMenuBar()
    {
        // no op for now
    }

    public void populateTimeline() {
        if (ConnectionHelper.isConnected(this.getContext())) {
            client.getHomeTimeline(oldestTweetInMemory, this, false);
        } else {
            this.swipeContainer.setRefreshing(true);
        }

    }

    public void refreshTimeline() {
        if (ConnectionHelper.isConnected(this.getContext())) {
            client.getHomeTimeline(-1, this, true);
        } else {
            this.swipeContainer.setRefreshing(false);
        }
    }

    public void clearTweets() {
        adapter.clear();
        this.swipeContainer.setRefreshing(false);
    }

    public void addTweets(ArrayList<Tweet> tweets) {
        oldestTweetInMemory = tweets.get(tweets.size() - 1).getTweetId();
        adapter.addAll(tweets);
        this.swipeContainer.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void handleInfiniteScroll() {
        if (initializedScrollListener == false) {
            lvTweets.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public boolean onLoadMore(int page, int totalItemsCount) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to your AdapterView
                    if (outOfData == false) {
                        populateTimeline();
                        return true;
                    } else{
                        return false;
                    }

                }
            });
        }
        initializedScrollListener = true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_compose:
                // Do Fragment menu item stuff here
                composeTweet(item);
                return true;
            default:
                break;
        }

        return false;
    }

    public void composeTweet(MenuItem mi) {
        // calls whatever fragment is active and calls the base fragments compose tweet method
        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(this.getContext())
                .title("Compose")
                .customView(R.layout.fragment_compose, wrapInScrollView)
                .positiveText("Tweet")
                .negativeText("Nevermind")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        // save settings
                        TextView tweetBody = (TextView) dialog.getCustomView().findViewById(R.id.etTweetBody);
                        String tweetText = tweetBody.getText().toString();
                        postTweet(tweetText);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        // no op
                    }
                }).build();

        dialog.show();
    }

    public void postTweet(String tweetText) {
        client.postStatusUpdate(tweetText, new RequestParams(), this);
    }

    @Override
    public void handleGetTweets(TwitterTweetApiResponseList response) {
        ArrayList<Tweet> resultPage = response.getTweets();
        if (response.isShouldRefresh()) {
            clearTweets();
        }
        if (resultPage.size() < TwitterClient.PAGE_SIZE) {
            this.outOfData = true;
            addTweets(resultPage);
        } else {
            addTweets(resultPage);
        }
        handleInfiniteScroll();
    }
    @Override
    public void handlePostTweet(Tweet tweet) {
        tweetStream.add(0, tweet);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void handleDataError(Throwable e, JSONObject response) {
        e.printStackTrace();
        // TODO implement handling
    }

    @Override
    public void handleGetUser(ArrayList<TwitterUser> u) {
        throw new RuntimeException("Fuck you");
    }

    @Override
    public void handleGetOwner(TwitterUser u) {
        //
    }


}
