package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        TextView username = findViewById(R.id.user);
        ImageView picture = findViewById(R.id.post_pic);
        TextView username2 = findViewById(R.id.user2);
        TextView caption = findViewById(R.id.caption);
        TextView time = findViewById(R.id.time);

        // Set up the post using values coming from Intent
        username.setText("" + getIntent().getStringExtra("username"));
        Glide.with(this).load(getIntent().getStringExtra("image")).into(picture);
        username2.setText("" + getIntent().getStringExtra("username"));
        caption.setText("" + getIntent().getStringExtra("description"));
        time.setText("" + getIntent().getStringExtra("time"));
    }
}