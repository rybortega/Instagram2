package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View to_add = LayoutInflater.from(context).inflate(R.layout.post, parent, false);
        return new ViewHolder(to_add);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        holder.bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Save this ViewHolder's features and bind when needed
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView picture;
        TextView username2;
        TextView caption;
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.username = itemView.findViewById(R.id.user);
            this.picture = itemView.findViewById(R.id.post_pic);
            this.username2 = itemView.findViewById(R.id.user2);
            this.caption = itemView.findViewById(R.id.caption);
            this.time = itemView.findViewById(R.id.time);
        }

        public void bind(final Post post) {
            this.username.setText("" + post.getUser().getUsername());
            Glide.with(context).load(post.getImage().getUrl()).into(this.picture);
            this.username2.setText("" + post.getUser().getUsername());
            this.caption.setText("" + post.getDescription());
            this.time.setText("" + post.getCreatedAt().toString());

            // If the description is clicked, go to PostActivity
            // I didn't use Parcel to putExtra the entire Post object
            // since it is much better to putExtra each individual attribute
            this.caption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PostActivity.class);
                    i.putExtra("username", post.getUser().getUsername());
                    i.putExtra("image", post.getImage().getUrl());
                    i.putExtra("description", post.getDescription());
                    i.putExtra("time", post.getCreatedAt().toString());
                    context.startActivity(i);
                }
            });
        }
    }
}
