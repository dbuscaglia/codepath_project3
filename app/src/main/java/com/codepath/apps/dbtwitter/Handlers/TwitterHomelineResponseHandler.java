package com.codepath.apps.dbtwitter.Handlers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.dbtwitter.Activities.TwitterClient;
import com.codepath.apps.dbtwitter.Interfaces.TwitterApiReceiver;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by danbuscaglia on 10/4/15.
 */
public class TwitterHomelineResponseHandler extends JsonHttpResponseHandler {
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        Toast.makeText((Context) receiver, response.toString(), Toast.LENGTH_LONG);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Toast.makeText((Context) receiver, response.toString(), Toast.LENGTH_LONG);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        Toast.makeText((Context) receiver, errorResponse.toString(), Toast.LENGTH_LONG);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Toast.makeText((Context) receiver, responseString, Toast.LENGTH_LONG);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        Toast.makeText((Context) receiver, responseString, Toast.LENGTH_LONG);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Toast.makeText((Context) receiver, errorResponse.toString(), Toast.LENGTH_LONG);
    }

    private TwitterClient client;
    private TwitterApiReceiver receiver;

    public TwitterHomelineResponseHandler(TwitterApiReceiver receiver, TwitterClient client) {
        this.client = client;
        this.receiver = receiver;

    }

    public void getHomeTimeline() {
        this.client.getHomeTimeline(this);
    }

}
