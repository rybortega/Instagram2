package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    ParseUser user;
    List<Post> posts;
    ProfilePostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        posts = new ArrayList<>();
        String username = getIntent().getStringExtra("username");
        TextView tvUsername = findViewById(R.id.user_username);
        tvUsername.setText("" + username);

        // If this user has a profile pic, load it in, else set the default
        ImageView profilePic = findViewById(R.id.user_profile_pic);
        if (getIntent().hasExtra("profilePic"))
            Glide.with(this).load(getIntent().getStringExtra("profilePic")).circleCrop().into(profilePic);
        else
            profilePic.setImageResource(R.drawable.georgio);

        // Retrieve the ParseUser object as a query so that we can get posts that match this ParseUser object
        user = getUser(username);

        // Set up RecyclerView and Adapter
        RecyclerView rvPosts = findViewById(R.id.rv_posts2);
        adapter = new ProfilePostAdapter(this, posts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new GridLayoutManager(this, 3));

        //Seek their posts
        queryPosts(0);

    }

    // Get the ParseUser knowing their username
    private ParseUser getUser(String username) {
        ParseQuery<ParseUser> q = ParseQuery.getQuery(ParseUser.class);
        q.whereEqualTo("username", username);
        try {
            List<ParseUser> res = q.find();
            return res.get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieve a list of all the posts
    public void queryPosts(int page) {
        ParseQuery<Post> q = ParseQuery.getQuery(Post.class);
        q.include(Post.KEY_USER);

        // The first Post that we need from page x is x * 5 + 1, so set skip(lower bound) as x * 5
        // We need 5 so set limit(upper bound) as x * 5 + 5
        q.setLimit(5 * page + 5);
        q.setSkip(5 * page);

        q.whereEqualTo(Post.KEY_USER, user);

        q.addDescendingOrder("createdAt");
        q.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null) {
                    Toast.makeText(ProfileActivity.this, "Error getting posts", Toast.LENGTH_SHORT).show();

                    return;
                }
                posts.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}