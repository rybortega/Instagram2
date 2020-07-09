package com.example.parstagram;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Like")
public class Like extends ParseObject {

    public Like() {
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser u) {
        put("user", u);
    }

    public Post getPost() {
        return (Post) getParseObject("post");
    }

    public void setPost(Post p) {
        put("post", p);
    }

}
