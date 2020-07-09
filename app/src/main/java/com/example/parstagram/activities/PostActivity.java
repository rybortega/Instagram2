package com.example.parstagram.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.parstagram.models.Comment;
import com.example.parstagram.adapters.CommentAdapter;
import com.example.parstagram.models.Like;
import com.example.parstagram.models.Post;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class PostActivity extends AppCompatActivity {

    List<Comment> comments;
    CommentAdapter adapter;
    Post post;
    EditText new_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        (findViewById(R.id.new_comment_container)).setVisibility(View.VISIBLE);
        (findViewById(R.id.comment_section)).setVisibility(View.VISIBLE);

        TextView username = findViewById(R.id.user);
        ImageView picture = findViewById(R.id.post_pic);
        TextView username2 = findViewById(R.id.user2);
        TextView caption = findViewById(R.id.caption);
        TextView time = findViewById(R.id.time);
        final ImageView like = findViewById(R.id.like_button);
        final TextView numLikes = findViewById(R.id.num_likes);

        // Set up the Comments RecyclerView
        comments = new ArrayList<>();
        adapter = new CommentAdapter(this, comments);
        RecyclerView rvComments = findViewById(R.id.rvComments);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the post through a query and set up featured views
        post = getPost(getIntent().getStringExtra("objectId"));
        username.setText("" + post.getUser().getUsername());
        Glide.with(this).load(post.getImage().getUrl()).into(picture);
        username2.setText("" + post.getUser().getUsername());
        caption.setText("" + post.getDescription());
        time.setText("" + post.getCreatedAt());

        // Find out if this User has liked this post, and set the proper Image representing so
        final Like[] hasLiked = {hasLiked(post)};
        if (hasLiked[0] == null)
            like.setImageResource(R.drawable.ufi_heart);
        else
            like.setImageResource(R.drawable.ufi_heart_active);

        // Query for comments
        getComments(0);

        //Set up the comment the use entered and the onclicklistener to handle the submission
        new_comment = findViewById(R.id.new_comment);
        (findViewById(R.id.submit_comment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveComment(new_comment.getText().toString());
            }
        });

        if (getIntent().hasExtra("comment"))
            new_comment.requestFocus();

        // Set the correct current number of likes
        numLikes.setText("" + post.getInt("likes") + " likes");

        // If we hit like, change the post's like count, save it, and update the Parse Server's Like class,
        // as well as the like count TextView, and Like icon's image resource
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post.put("likes", hasLiked[0] != null ? post.getInt("likes") - 1 : post.getInt("likes") + 1);
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        numLikes.setText("" + post.getInt("likes") + " likes");
                        if (hasLiked[0] == null) {
                            like.setImageResource(R.drawable.ufi_heart_active);
                            Like newLike = new Like();
                            newLike.setUser(ParseUser.getCurrentUser());
                            newLike.setPost(post);
                            try {
                                newLike.save();
                                hasLiked[0] = newLike;
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                                Toast.makeText(PostActivity.this, "Error saving like", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            like.setImageResource(R.drawable.ufi_heart);
                            try {
                                hasLiked[0].delete();
                                hasLiked[0] = null;
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                                Toast.makeText(PostActivity.this, "Error removing like", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
            }
        });
    }

    // Query the Like table to see if this user has liked this post, if so return this Like object
    // so that we may potentially delete it
    private Like hasLiked(Post post) {
        ParseQuery<Like> q = ParseQuery.getQuery(Like.class);
        q.whereEqualTo("post", post);
        q.whereEqualTo("user", ParseUser.getCurrentUser());
        try {
            List<Like> res = q.find();
            if (res.isEmpty())
                return null;
            return res.get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Create a new Comment Object and save it
    private void saveComment(String comment) {
        Comment toSave = new Comment();
        toSave.setCommentText(comment);
        toSave.setUser(ParseUser.getCurrentUser());
        toSave.setPost(post);
        try {
            toSave.save();
            new_comment.setText("");
            comments.add(0, toSave);
            adapter.notifyDataSetChanged();
        } catch (ParseException e) {
            Toast.makeText(PostActivity.this, "Error saving comment", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    // Get Post knowing the post's objectId
    private Post getPost(String objectId) {
        ParseQuery<Post> q = ParseQuery.getQuery(Post.class);
        q.include("user");
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
    public void getComments(int page) {
        ParseQuery<Comment> q = ParseQuery.getQuery(Comment.class);
        q.include("post");
        q.include("user");
        q.whereEqualTo("post", post);

        q.setLimit(10 * page + 10);
        q.setSkip(10 * page);

        q.orderByDescending("createdAt");

        q.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if (e != null) {
                    Toast.makeText(PostActivity.this, "Error getting comments", Toast.LENGTH_SHORT).show();
                    return;
                }
                comments.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}