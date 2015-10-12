package com.codepath.apps.dbtwitter.Activities;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.apps.dbtwitter.Interfaces.TwitterApiReceiver;
import com.codepath.apps.dbtwitter.Models.Tweet;
import com.codepath.apps.dbtwitter.Models.TwitterTweetApiResponseList;
import com.codepath.apps.dbtwitter.Models.TwitterUser;
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
    public static final int PAGE_SIZE = 25;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getHomeTimeline(long max_id, final TwitterApiReceiver receiver, final boolean shouldRefresh) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        getRequestTweets(apiUrl, buildParams(max_id, -1), receiver, shouldRefresh);
    }

    public void getMentionsTimeline(long max_id, final TwitterApiReceiver receiver, final boolean shouldRefresh) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        getRequestTweets(apiUrl, buildParams(max_id, -1), receiver, shouldRefresh);
    }

    public void getUserTimeline(long max_id, TwitterUser user, final TwitterApiReceiver receiver, final boolean shouldRefresh) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        getRequestTweets(apiUrl, buildParams(max_id, getUserIdFromUser(user)), receiver, shouldRefresh);
    }

    private long getUserIdFromUser(TwitterUser u) {
        long user_id;
        if (u == null) {
            user_id = -1;
        } else {
            user_id = u.getUserId();
        }
        return user_id;
    }

    public void getUserDetail(TwitterUser user, final TwitterApiReceiver receiver, final boolean shouldRefresh) {
        String apiUrl = getApiUrl("users/lookup.json");
        RequestParams params = new RequestParams();
        long user_id = getUserIdFromUser(user);
        if (user_id != -1) {
            params.put("user_id", user_id);
        }
        getRequestUsers(apiUrl, params, receiver, shouldRefresh);
    }

    public void getLoggedInUserDetail(final TwitterApiReceiver receiver) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        getLoggedInUser(apiUrl, null, receiver, false);
    }

    public void getUsersDetail(ArrayList<TwitterUser> users, final TwitterApiReceiver receiver, final boolean shouldRefresh) {
        /**
         * to get a lot of user data in the background for persistence
         */
        String apiUrl = getApiUrl("users/lookup.json");
        String user_ids = "";
        for (int i = 0; i < users.size(); i++) {
            if (i == 0) {
                user_ids = user_ids + String.valueOf(users.get(i).getUserId());
            } else {
                user_ids = user_ids + ", " +  String.valueOf(users.get(i).getUserId());
            }
        }
        RequestParams params = new RequestParams();
        params.put("user_id", user_ids);
        postRequest(apiUrl, params, receiver, shouldRefresh);
    }

    private RequestParams buildParams(long max_id, long uid) {
        RequestParams params = new RequestParams();
        params.put("count", TwitterClient.PAGE_SIZE);
        if (max_id != -1) {
            params.put("max_id", max_id);
        }
        if (uid != -1) {
            params.put("user_id", uid);
        }
        return params;
    }

    public void getRequestTweets(String apiUrl, RequestParams params, final TwitterApiReceiver receiver, final boolean shouldRefresh){

        getClient().get(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray tweets) {
                ArrayList<Tweet> newTweets = Tweet.fromJSONArray(tweets);
                TwitterTweetApiResponseList response = new TwitterTweetApiResponseList(newTweets, shouldRefresh);
                receiver.handleGetTweets(response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                throw new RuntimeException("DEAD");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                receiver.handleDataError(throwable, errorResponse);
            }
        });
    }

    public void getLoggedInUser(String apiUrl, RequestParams params, final TwitterApiReceiver receiver, final boolean shouldRefresh) {
        getClient().get(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject usersResponse) {
                TwitterUser twitter_user = TwitterUser.makeUser(usersResponse);
                Log.d("DANB", twitter_user.toString());
                receiver.handleGetOwner(twitter_user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DANB", errorResponse.toString());
                receiver.handleDataError(throwable, errorResponse);
            }
        });

    }

    public void getRequestUsers(String apiUrl, RequestParams params, final TwitterApiReceiver receiver, final boolean shouldRefresh){
        getClient().get(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray usersResponse) {
                ArrayList<TwitterUser> users = TwitterUser.fromJSONArray(usersResponse);
                receiver.handleGetUser(users);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                receiver.handleDataError(throwable, errorResponse);
            }
        });
    }


    public void postRequest(String apiUrl, RequestParams params, final TwitterApiReceiver receiver, final boolean shouldRefresh){
        getClient().post(apiUrl, params, new JsonHttpResponseHandler() {

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