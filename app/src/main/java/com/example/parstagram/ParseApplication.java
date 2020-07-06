package com.example.parstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("wilmer-parstagram") // should correspond to APP_ID env variable
                .clientKey("37DD5A0741")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://wilmer-parstagram.herokuapp.com/parse/").build());
    }
}
