package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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


public class PostActivity extends AppCompatActivity {

    List<Comment> comments;
    CommentAdapter adapter;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        TextView username = findViewById(R.id.user);
        ImageView picture = findViewById(R.id.post_pic);
        TextView username2 = findViewById(R.id.user2);
        TextView caption = findViewById(R.id.caption);
        TextView time = findViewById(R.id.time);

        // Set up the Comments RecyclerView
        comments = new ArrayList<>();
        adapter = new CommentAdapter(this, comments);
        RecyclerView rvComments = findViewById(R.id.rvComments);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the post through a query and set up featured views
        post = getPost(getIntent().getStringExtra("objectId"));
        username.setText("" + post.getUser());
        Glide.with(this).load(post.getImage().getUrl()).into(picture);
        username2.setText("" + post.getUser());
        caption.setText("" + post.getDescription());
        time.setText("" + post.getCreatedAt());

        // Query for comments
        getComments(0);
    }

    // Get Post knowing the post's objectId
    private Post getPost(String objectId){
        ParseQuery<Post> q = ParseQuery.getQuery(Post.class);
        q.whereEqualTo("objectId", objectId);
        try {
            List<Post> res = q.find();
            return res.get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // QUery for the comments pertaining to this post
    public void getComments(int page){
        ParseQuery<Comment> q = ParseQuery.getQuery(Comment.class);
        q.include("post");
        q.whereEqualTo("post", post);

        q.setLimit(10 * page + 10);
        q.setSkip(10 * page);

        q.orderByDescending("createdAt");

        q.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if(e != null){
                    Toast.makeText(PostActivity.this, "Error getting comments", Toast.LENGTH_SHORT).show();
                    return;
                }
                comments.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}