package com.codepath.apps.dbtwitter.Helpers;

import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by danbuscaglia on 10/4/15.
 */
public class TwitterStringHelper {
    public static final Pattern mentionPattern = Pattern.compile("(@[A-Za-z0-9_-]+)");
    public static final Pattern hashtagPattern = Pattern.compile("(#[A-Za-z0-9_-]+)");

    public static Spanned formatTwitterText(String unformattedText) {
        StringBuffer sb = new StringBuffer(unformattedText.length());
        Matcher o = hashtagPattern.matcher(unformattedText);

        while (o.find()) {
            o.appendReplacement(sb, "<font style=\"bold\" color=\"#0084b4\">" + o.group(1) + "</font>");
        }
        o.appendTail(sb);

        Matcher n = mentionPattern.matcher(sb.toString());
        sb = new StringBuffer(sb.length());

        while (n.find()) {
            n.appendReplacement(sb, "<font style=\"bold\" color=\"#0084b4\">" + n.group(1) + "</font>");
        }
        n.appendTail(sb);

        return Html.fromHtml(sb.toString());
    }
}
