package com.codepath.apps.dbtwitter.Activities;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.codepath.apps.dbtwitter.Adapters.TweetsArrayAdapter;
import com.codepath.apps.dbtwitter.Helpers.ConnectionHelper;
import com.codepath.apps.dbtwitter.Helpers.EndlessScrollListener;
import com.codepath.apps.dbtwitter.Interfaces.TwitterApiReceiver;
import com.codepath.apps.dbtwitter.Models.Tweet;
import com.codepath.apps.dbtwitter.Models.TwitterTweetApiResponseList;
import com.codepath.apps.dbtwitter.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TwitterTimeline extends NetworkDetectingActivity implements TwitterApiReceiver {

    private TwitterClient client;
    private Context context;
    private ArrayList<Tweet> homeTweets;
    private TweetsArrayAdapter adapter;
    private ListView lvTweets;
    private long oldestTweetInMemory;  // used for infinite scrolling
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_timeline);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        addImageToMenuBar();
        oldestTweetInMemory = -1;
        client = TwitterApplication.getRestClient();
        lvTweets = (ListView) findViewById(R.id.rvTwitterStream);
        homeTweets = new ArrayList<>();
        adapter = new TweetsArrayAdapter(this, homeTweets);
        lvTweets.setAdapter(adapter);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView

                populateTimeline();
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
        context = this;
        this.swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
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
        populateTimeline();
    }

    public void addImageToMenuBar()
    {
        // no op for now
    }

    public void populateTimeline() {
        if (ConnectionHelper.isConnected(this)) {
            client.getHomeTimeline(oldestTweetInMemory, this, false);
        } else {
            this.swipeContainer.setRefreshing(false);
        }

    }

    public void refreshTimeline() {
        if (ConnectionHelper.isConnected(this)) {
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

    public void composeTweet(MenuItem mi) {
        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Compose")
                .customView(R.layout.fragment_compose, wrapInScrollView)
                .positiveText("Tweet")
                .negativeText("Nevermind")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        // save settings
                        if (ConnectionHelper.isConnected(context)) {
                            TextView tweetBody = (TextView) dialog.getCustomView().findViewById(R.id.etTweetBody);
                            client.postStatusUpdate(tweetBody.getText().toString(), new RequestParams(), (TwitterApiReceiver) context);
                        } else {
                            Toast.makeText(context, "You can not compose offline", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        // no op
                    }
                }).build();

        dialog.show();
    }

    @Override
    public void handleGetTweets(TwitterTweetApiResponseList response) {
        ArrayList<Tweet> resultPage = response.getTweets();
        if (response.isShouldRefresh()) {
            clearTweets();
        }
        addTweets(resultPage);
    }

    @Override
    public void handlePostTweet(Tweet tweet) {
        homeTweets.add(0, tweet);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void handleDataError(Throwable e, JSONObject response) {
        e.printStackTrace();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
