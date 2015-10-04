package com.codepath.apps.dbtwitter.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.dbtwitter.Adapters.TweetsArrayAdapter;
import com.codepath.apps.dbtwitter.Interfaces.TwitterApiReceiver;
import com.codepath.apps.dbtwitter.Models.Tweet;
import com.codepath.apps.dbtwitter.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TwitterTimeline extends AppCompatActivity implements TwitterApiReceiver {

    private TwitterClient client;
    private Context context;
    private ArrayList<Tweet> homeTweets;
    private TweetsArrayAdapter adapter;
    private ListView lvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_timeline);
        client = TwitterApplication.getRestClient();
        lvTweets = (ListView) findViewById(R.id.rvTwitterStream);
        homeTweets = new ArrayList<Tweet>();
        adapter = new TweetsArrayAdapter(this, homeTweets);
        lvTweets.setAdapter(adapter);
        context = this;
        populateTimeline();
    }

    public void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                adapter.addAll(Tweet.fromJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(context, errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleDataCallback(ArrayList<Tweet> resultPage) {

    }

    @Override
    public void handleDataError(Throwable e) {

    }
}
