# Project 4 - Simple Twitter Client Continued

Extend the simple Twitter client that supports fragments

Time spent: ~12 hours spent in total.

## User Stories

The following **required** functionality is completed:
Week 4
* [x] Includes all required user stories from Week 3 Twitter Client
* [x] User can switch between Timeline and Mention views using tabs.
* [x] User can view their home timeline tweets.
* [x] User can view the recent mentions of their username.
* [x] User can navigate to view their own profile
* [x] User can see picture, tagline, # of followers, # of following, and tweets on their profile.
* [x] User can click on the profile image in any tweet to see another user's profile.
* [x] User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
* [x] Profile view should include that user's timeline
* [x] User can infinitely paginate any of these timelines (home, mentions, user) by scrolling to the bottom

Week 3
* [x] User can sign in to Twitter using OAuth login
* [x] User can view the tweets from their home timeline
* [x] User should be displayed the username, name, and body for each tweet
* [x] User should be displayed the relative timestamp for each tweet "8m", "7h"
* [x] User can view more tweets as they scroll with infinite pagination
* [x] User can compose a new tweet
* [x] User can click a “Compose” icon in the Action Bar on the top right
* [x] User can then enter a new tweet and post this to twitter
* [x] User is taken back to home timeline with new tweet visible in timeline

The following **optional** features are implemented:

* [] While composing a tweet, user can see a character counter with characters remaining for tweet out of 140
* [x] Links in tweets are clickable and will launch the web browser (see autolink)
* [x] User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh)
* [] User can open the twitter app offline and see last loaded tweets
* [] User can tap a tweet to display a "detailed" view of that tweet
* [] User can select "reply" from detail view to respond to a tweet
* [x] Improve the user interface and theme the app to feel "twitter branded"

The following **bonus** features are implemented:

* [] User can see embedded image media within the tweet detail view
* [x] Compose activity is replaced with a modal overlay

The following **extra** features are also implemented:
* [x] Hashtags and @ats styled via regex

## Video Walkthrough

[Imgur](http://i.imgur.com/YMCYEhW.gifv)

## Open-source libraries used

 * [scribe-java](https://github.com/fernandezpablo85/scribe-java) - Simple OAuth library for handling the authentication flow.
 * [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
 * [codepath-oauth](https://github.com/thecodepath/android-oauth-handler) - Custom-built library for managing OAuth authentication and signing of requests
 * [Picasso](https://github.com/square/picasso) - Used for async image loading and caching them in memory and on disk.
 * [ActiveAndroid](https://github.com/pardom/ActiveAndroid) - Simple ORM for persisting a local SQLite database on the Android device
- [Material Dialogs] (https://github.com/afollestad/material-dialogs) - clean modal design
