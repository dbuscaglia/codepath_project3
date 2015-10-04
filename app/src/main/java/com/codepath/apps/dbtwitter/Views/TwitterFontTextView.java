package com.codepath.apps.dbtwitter.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.codepath.apps.dbtwitter.Helpers.FontCache;


/**
 * Created by danbuscaglia on 9/29/15.
 */
public class TwitterFontTextView extends TextView {

    public TwitterFontTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public TwitterFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public TwitterFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("HelveticaNeue-Regular.ttf", context);
        setTypeface(customFont);
    }
}
