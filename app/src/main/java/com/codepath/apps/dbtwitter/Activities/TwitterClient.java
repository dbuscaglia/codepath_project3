package com.codepath.apps.dbtwitter.Activities;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.apps.dbtwitter.Interfaces.TwitterApiReceiver;
import com.codepath.apps.dbtwitter.Models.Tweet;
import com.codepath.apps.dbtwitter.Models.TwitterTweetApiResponseList;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "YMPQvFpAOWSGxy3PY6XRvdz9Y";       // Change this
	public static final String REST_CONSUMER_SECRET = "6I1eMmYtYnnetE3QRnCkfy0R8bbhG4dAOkWEIFMLWCNqusJRr6"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpdbtweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getHomeTimeline(long max_id, final TwitterApiReceiver receiver, final boolean shouldRefresh) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        // add params
        RequestParams params = new RequestParams();

        params.put("count", 25);
        if (max_id != -1) {
            params.put("max_id", max_id);
        }
        getClient().get(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray tweets) {
                ArrayList<Tweet> newTweets = Tweet.fromJSONArray(tweets);
                TwitterTweetApiResponseList response = new TwitterTweetApiResponseList(newTweets, shouldRefresh);
                receiver.handleGetTweets(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                receiver.handleDataError(throwable, errorResponse);
            }
        });
    }

    public void postStatusUpdate(String tweet, RequestParams params, final TwitterApiReceiver receiver) {
        params.put("status", tweet);

        String tweetUrl = getApiUrl("statuses/update.json");
        getClient().post(tweetUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                receiver.handlePostTweet(Tweet.makeTweet(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                receiver.handleDataError(throwable, errorResponse);
            }
        });
    }

    //  COMPOSE

}