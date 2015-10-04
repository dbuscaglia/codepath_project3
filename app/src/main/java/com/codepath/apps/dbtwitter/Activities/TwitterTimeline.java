package com.codepath.apps.dbtwitter.Activities;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.codepath.apps.dbtwitter.Adapters.TweetsArrayAdapter;
import com.codepath.apps.dbtwitter.Helpers.EndlessScrollListener;
import com.codepath.apps.dbtwitter.Interfaces.TwitterApiReceiver;
import com.codepath.apps.dbtwitter.Models.Tweet;
import com.codepath.apps.dbtwitter.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TwitterTimeline extends ActionBarActivity implements TwitterApiReceiver {

    private TwitterClient client;
    private Context context;
    private ArrayList<Tweet> homeTweets;
    private TweetsArrayAdapter adapter;
    private ListView lvTweets;
    private long oldestTweetInMemory;  // used for infinite scrolling
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_timeline);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
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
        populateTimeline();
    }

    public void populateTimeline() {
        client.getHomeTimeline(oldestTweetInMemory, this);
    }

    public void addTweets(ArrayList<Tweet> tweets) {
        for (int i =0; i < tweets.size(); i++) {
            long id = tweets.get(i).getTweetId();
            if (id < oldestTweetInMemory) {
                oldestTweetInMemory = id;
            }
        }
        adapter.addAll(tweets);
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
                        TextView tweetBody = (TextView) dialog.getCustomView().findViewById(R.id.etTweetBody);
                        client.postStatusUpdate(tweetBody.getText().toString(), new RequestParams(), (TwitterApiReceiver) context);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        // no op
                    }
                }).build();

        dialog.show();
    }

    @Override
    public void handleGetTweets(ArrayList<Tweet> resultPage) {
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
