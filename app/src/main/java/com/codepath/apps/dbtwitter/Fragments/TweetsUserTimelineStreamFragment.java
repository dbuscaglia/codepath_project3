package com.codepath.apps.dbtwitter.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.dbtwitter.Activities.TwitterClient;
import com.codepath.apps.dbtwitter.Helpers.ConnectionHelper;
import com.codepath.apps.dbtwitter.Helpers.EndlessScrollListener;
import com.codepath.apps.dbtwitter.Models.Tweet;
import com.codepath.apps.dbtwitter.Models.TwitterTweetApiResponseList;
import com.codepath.apps.dbtwitter.Models.TwitterUser;
import com.codepath.apps.dbtwitter.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by danbuscaglia on 10/11/15.
 */
public class TweetsUserTimelineStreamFragment extends TweetStreamFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_detail, container, false);
        outOfData = true;
        view = v;
        initMenu();
        initFragmentState(v);
        updateUserDetails(v);

        if (this.user == null) {
            getOwner();
        } else {
            updateUserDetails(v);
        }
        return v;
    }

    protected void getOwner() {
        if (ConnectionHelper.isConnected(this.getContext())) {
            client.getLoggedInUserDetail(this);
        } else {
            this.swipeContainer.setRefreshing(false);
        }
    }

    public void handleInfiniteScroll() {
        if (initializedScrollListener == false) {
            lvTweets.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public boolean onLoadMore(int page, int totalItemsCount) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to your AdapterView
                    if (outOfData == false && user != null) {
                        populateTimeline();
                        return true;
                    } else {
                        return false;
                    }

                }
            });
        }
        initializedScrollListener = true;
    }

    protected void updateUserDetails(View v) {
        // populate user detail
    }

    @Override
    public void populateTimeline() {
        if (this.user == null) {
            //
        } else {
            if (ConnectionHelper.isConnected(this.getContext())) {
                client.getUserTimeline(oldestTweetInMemory, this.user, this, false);
            }
            updateUserDetails();
        }

    }
    @Override
    public void refreshTimeline() {
        if (this.user != null) {
            client.getUserTimeline(-1, this.user, this, false);
            client.getUserDetail(this.user, this, false);
        }
    }

    @Override
    public void handleGetTweets(TwitterTweetApiResponseList response) {
        ArrayList<Tweet> resultPage = response.getTweets();
        if (response.isShouldRefresh()) {
            clearTweets();
        }
        if (resultPage.size() < TwitterClient.PAGE_SIZE) {
            this.outOfData = true;
            timeline(resultPage);
        } else {
            timeline(resultPage);
        }
    }

    public void timeline(ArrayList<Tweet> resultPage) {
        addTweets(resultPage);

    }

    public static TweetsUserTimelineStreamFragment newInstance(int pageNumber, String fragmentTitle, TwitterUser user) {
        TweetsUserTimelineStreamFragment streamFragment = new TweetsUserTimelineStreamFragment();
        Bundle args = new Bundle();
        args.putInt("pageNumber", pageNumber);
        args.putString("fragmentTitle", fragmentTitle);
        streamFragment.setArguments(args);
        streamFragment.user = user;
        return streamFragment;
    }

    @Override
    public void handleGetUser(ArrayList<TwitterUser> u) {
        /**
         * Always going to be the first result, since we arent asking for bulk
         */
        handleUserUpdate(u);
    }

    public void handleUserUpdate(ArrayList<TwitterUser> u) {
        if (u.size() > 0) {
            user = u.get(0);
            updateUserDetails();
            populateTimeline();
        }
    }

    public void updateUserDetails() {
        //
        ImageView profilePic = (ImageView) view.findViewById(R.id.iv_UserProfile);
        TextView handle = (TextView) view.findViewById(R.id.tv_UserHandle);
        TextView name = (TextView) view.findViewById(R.id.tv_UserRealName);
        TextView following = (TextView) view.findViewById(R.id.tv_Following);
        TextView followers = (TextView) view.findViewById(R.id.tv_Followers);

        handle.setText(this.user.getHandle());
        name.setText(this.user.getRealname());
        followers.setText(String.valueOf(this.user.getFollowers()));
        following.setText(String.valueOf(this.user.getFollowing()));

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(0)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(getContext())
                .load(this.user.getProfilePictureUrl())
                .fit()
                .transform(transformation)
                .into(profilePic);
    }


    @Override
    public void handleGetOwner(TwitterUser u) {
        setOwner(u);
    }
    public void setOwner(TwitterUser u) {
        this.user = u;
        this.outOfData = false;
        if (view != null) {
            adapter.clear();
            populateTimeline();
            updateUserDetails();
        }
    }
}
