package com.example.parstagram.fragments;

import android.widget.Toast;

import com.example.parstagram.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

// Same as FeedFragment except one extra constraint...
public class ProfileFragment extends FeedFragment {
    @Override
    public void queryPosts() {
        ParseQuery<Post> q = ParseQuery.getQuery(Post.class);
        q.include(Post.KEY_USER);
        q.setLimit(20);
        q.addDescendingOrder("createdAt");

        // User that made the post must be the current User signed in
        q.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());

        q.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "Error getting posts", Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                    return;
                }
                posts.addAll(objects);
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });
    }
}
