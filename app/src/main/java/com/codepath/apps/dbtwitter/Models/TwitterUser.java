package com.codepath.apps.dbtwitter.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by danbuscaglia on 10/4/15.
 */
public class TwitterUser {

    private String realname;
    private String handle;
    private String profilePictureUrl;
    private int favourites;

    public int getFavourites() {
        return favourites;
    }

    private long userId;

    public String getRealname() {
        return realname;
    }

    public String getHandle() {
        return "@" + handle;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public long getUserId() {
        return userId;
    }


    public static TwitterUser makeUser (JSONObject o) {
        TwitterUser u = new TwitterUser();
        try {
            u.userId = o.getLong("id");
            u.realname = o.getString("name");
            u.handle = o.getString("screen_name");
            u.profilePictureUrl = o.getString("profile_image_url");
            u.favourites = o.getInt("favourites_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }
}
