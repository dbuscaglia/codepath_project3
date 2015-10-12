package com.codepath.apps.dbtwitter.Activities;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.dbtwitter.Adapters.SmartFragmentStatePagerAdapter;
import com.codepath.apps.dbtwitter.Fragments.TweetStreamFragment;
import com.codepath.apps.dbtwitter.Fragments.TweetsHomeStreamFragment;
import com.codepath.apps.dbtwitter.Fragments.TweetsMentionsStreamFragment;
import com.codepath.apps.dbtwitter.Fragments.TweetsUserTimelineStreamFragment;
import com.codepath.apps.dbtwitter.Helpers.ConnectionHelper;
import com.codepath.apps.dbtwitter.Interfaces.TwitterApiReceiver;
import com.codepath.apps.dbtwitter.Models.TwitterUser;
import com.codepath.apps.dbtwitter.R;
import com.codepath.apps.dbtwitter.Views.LockableViewPager;
import com.loopj.android.http.RequestParams;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class TwitterTimeline extends NetworkDetectingActivity {

    private Toolbar toolbar;
    private Context context;
    private LockableViewPager viewPager;
    private MyPagerAdapter tabAdapter;
    private PagerSlidingTabStrip tabsStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_timeline);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        context = this;
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (LockableViewPager) findViewById(R.id.viewpager);
        tabAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter (tabAdapter);
        // Give the PagerSlidingTabStrip the ViewPager
        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
        viewPager.setSwipeable(false);

    }
    // Extend from SmartFragmentStatePagerAdapter now instead for more dynamic ViewPager items
    public class MyPagerAdapter extends SmartFragmentStatePagerAdapter {
        private int NUM_ITEMS = 3;
        private WeakHashMap<Integer, String> PAGES;
        private TwitterUser user;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            PAGES = new WeakHashMap<>();
            PAGES.put(0, "Home");
            PAGES.put(1, "Mentions");
            PAGES.put(2, "User Detail");
            user = null;
        }

        public void setUser(TwitterUser u) {
            this.user = u;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }


        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    // Show users home timeline
                    return TweetsHomeStreamFragment.newInstance(0, PAGES.get(0).toString());
                case 1: // Fragment # 1 - Mentions
                    return TweetsMentionsStreamFragment.newInstance(1, PAGES.get(1).toString());
                case 2: // Fragment #2 - User Detail
                    return TweetsUserTimelineStreamFragment.newInstance(2, PAGES.get(2).toString(), this.user);
                default:
                    // Show users home timeline
                    return TweetsHomeStreamFragment.newInstance(0, PAGES.get(0).toString());
            }
        }

        // Returns the page title for the top indicator
        @Override
        public String getPageTitle(int position) {
            return PAGES.get(position).toString();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter_timeline, menu);
        return true;
    }
    public void showProfile(MenuItem mi) {
        tabAdapter.setUser(null);
        viewPager.setCurrentItem(2);
        tabAdapter.notifyDataSetChanged();
    }

    public void showProfileByID(TwitterUser user) {
        tabAdapter.setUser(user);
        viewPager.setCurrentItem(2);
        tabAdapter.notifyDataSetChanged();
    }

}
