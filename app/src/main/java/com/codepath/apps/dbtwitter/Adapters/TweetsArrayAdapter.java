package com.codepath.apps.dbtwitter.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.codepath.apps.dbtwitter.Models.Tweet;
import com.codepath.apps.dbtwitter.R;

import java.util.List;


/**
 * Created by danbuscaglia on 10/4/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }
}
