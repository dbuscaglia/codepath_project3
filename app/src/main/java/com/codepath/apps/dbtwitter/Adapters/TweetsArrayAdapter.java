package com.codepath.apps.dbtwitter.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.dbtwitter.Helpers.TwitterStringHelper;
import com.codepath.apps.dbtwitter.Models.Tweet;
import com.codepath.apps.dbtwitter.R;
import com.codepath.apps.dbtwitter.Views.TwitterFontTextView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;


/**
 * Created by danbuscaglia on 10/4/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.layout_tweet_card, tweets);
    }

    private static class ViewHolder {
        TextView body;
        TextView realName;
        TextView handle;
        TextView postTime;
        TextView stars;
        TextView retweets;
        ImageView profilePic;


    }
    public interface OnTweetClickedListener {
        void onTweetClicked(int position);
        void onProfileClicked(int position);
        void onRetweetClicked(int position);
    }

    private OnTweetClickedListener onTweetClickedListener;

    public void setOnTweetClickedListener(OnTweetClickedListener onTweetClickedListener) {
        this.onTweetClickedListener = onTweetClickedListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Tweet so = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_tweet_card, parent, false);
            viewHolder.profilePic = (ImageView) convertView.findViewById(R.id.ivTweetProfilePic);
            viewHolder.body = (TextView) convertView.findViewById(R.id.tvTweetBody);
            viewHolder.realName = (TextView) convertView.findViewById(R.id.tvUserRealName);
            viewHolder.handle = (TextView) convertView.findViewById(R.id.tvTwitterHandle);
            viewHolder.postTime = (TextView) convertView.findViewById(R.id.tvPostTime);
            viewHolder.stars = (TextView) convertView.findViewById(R.id.tvTotalStars);
            viewHolder.retweets = (TextView) convertView.findViewById(R.id.tvTotalRetweets);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTweetClickedListener != null) {
                    onTweetClickedListener.onProfileClicked(position);
                }
            }
        });

        viewHolder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTweetClickedListener != null) {
                    onTweetClickedListener.onTweetClicked(position);
                }
            }
        });

        viewHolder.stars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTweetClickedListener != null) {
                    onTweetClickedListener.onRetweetClicked(position);
                }
            }
        });
        viewHolder.profilePic.setImageResource(0);
        // Lookup view for data population

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(0)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(getContext())
                .load(so.getUser().getProfilePictureUrl())
                .fit()
                .transform(transformation)
                .into(viewHolder.profilePic);

        Spannable s1 = (Spannable) Html.fromHtml(so.getTweetBody());
        Spannable s = stripUnderlines(s1);
        // #HASHTAG
        viewHolder.body.setText(TwitterStringHelper.formatTwitterText(s.toString()));
        Linkify.addLinks(viewHolder.body, Linkify.ALL);
        viewHolder.handle.setText(so.getUser().getHandle());
        viewHolder.realName.setText(so.getUser().getRealname());
        viewHolder.postTime.setText(so.getTimestamp());
        viewHolder.stars.setText(String.valueOf(so.getFavorites()));
        viewHolder.retweets.setText(String.valueOf(so.getRetweets()));
        return convertView;
    }

    private Spannable stripUnderlines(Spannable s) {

        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        return s;
    }

    private class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }
        @Override public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
}
