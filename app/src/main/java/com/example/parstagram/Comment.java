package com.example.parstagram;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public Comment() {
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser new_user) {
        put("user", new_user);
    }

    public String getCommentText() {
        return getString("commentText");
    }

    public void setCommentText(String s) {
        put("commentText", s);
    }

    public Post getPost() {
        return (Post) getParseObject("post");
    }

    public void setPost(Post new_post) {
        put("post", new_post);
    }
}
