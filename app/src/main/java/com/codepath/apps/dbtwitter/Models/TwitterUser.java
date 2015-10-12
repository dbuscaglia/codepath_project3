package com.codepath.apps.dbtwitter.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by danbuscaglia on 10/4/15.
 */
public class TwitterUser implements Parcelable {

    private String realname;
    private String handle;
    private String profilePictureUrl;
    private int favourites;
    private int followers;
    private int following;

    public int getFollowing() {
        return following;
    }

    public int getFollowers() {
        return followers;
    }

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
            u.followers = o.getInt("followers_count");
            u.following = o.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }

    public static ArrayList<TwitterUser> fromJSONArray(JSONArray a) {
        ArrayList<TwitterUser> result = new ArrayList<TwitterUser>();
        for (int i = 0; i < a.length(); i++) {
            try {
                JSONObject tweetJSON = a.getJSONObject(i);
                TwitterUser t = makeUser(tweetJSON);
                if (t != null) {
                    result.add(t);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.realname);
        dest.writeString(this.handle);
        dest.writeString(this.profilePictureUrl);
        dest.writeInt(this.favourites);
        dest.writeInt(this.followers);
        dest.writeInt(this.following);
        dest.writeLong(this.userId);
    }

    public TwitterUser() {
    }

    private TwitterUser(Parcel in) {
        this.realname = in.readString();
        this.handle = in.readString();
        this.profilePictureUrl = in.readString();
        this.favourites = in.readInt();
        this.followers = in.readInt();
        this.following = in.readInt();
        this.userId = in.readLong();
    }

    public static final Creator<TwitterUser> CREATOR = new Creator<TwitterUser>() {
        public TwitterUser createFromParcel(Parcel source) {
            return new TwitterUser(source);
        }

        public TwitterUser[] newArray(int size) {
            return new TwitterUser[size];
        }
    };
}
