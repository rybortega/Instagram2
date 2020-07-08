package com.example.parstagram;

import android.content.Context;
import android.content.Intent;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

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

        ImageView profilePic;
        TextView username;
        ImageView picture;
        TextView username2;
        TextView caption;
        TextView time;
        LinearLayout headline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profilePic = itemView.findViewById(R.id.post_profile_pic);
            this.username = itemView.findViewById(R.id.user);
            this.picture = itemView.findViewById(R.id.post_pic);
            this.username2 = itemView.findViewById(R.id.user2);
            this.caption = itemView.findViewById(R.id.caption);
            this.time = itemView.findViewById(R.id.time);
            this.headline = itemView.findViewById(R.id.headline);
        }

        public void bind(final Post post) {

            // If this post's user has a profile picture, load it, else load the deafult
            ParseFile image = post.getUser().getParseFile("profilePic");
            if (image != null) {
                String url = image.getUrl();
                Glide.with(context).load(url).circleCrop().into(this.profilePic);
            } else
                this.profilePic.setImageResource(R.drawable.georgio);
            this.username.setText("" + post.getUser().getUsername());
            Glide.with(context).load(post.getImage().getUrl()).into(this.picture);
            this.username2.setText("" + post.getUser().getUsername());
            this.caption.setText("" + post.getDescription());
            this.time.setText("" + post.getCreatedAt().toString());

            this.headline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String username = post.getUser().getUsername();
                    Intent i = new Intent(context, ProfileActivity.class);
                    i.putExtra("username", username);
                    ParseFile pic = post.getUser().getParseFile("profilePic");
                    if(pic != null){
                        i.putExtra("profilePic", pic.getUrl());
                    }
                    context.startActivity(i);
                }
            });

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
