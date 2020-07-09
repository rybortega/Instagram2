package com.example.parstagram.adapters;

import android.content.Context;
import android.content.Intent;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.activities.PostActivity;
import com.example.parstagram.activities.ProfileActivity;
import com.example.parstagram.models.Like;
import com.example.parstagram.models.Post;
import com.example.parstagram.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
        ImageView comment;
        ImageView like;
        TextView numLikes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profilePic = itemView.findViewById(R.id.post_profile_pic);
            this.username = itemView.findViewById(R.id.user);
            this.picture = itemView.findViewById(R.id.post_pic);
            this.username2 = itemView.findViewById(R.id.user2);
            this.caption = itemView.findViewById(R.id.caption);
            this.time = itemView.findViewById(R.id.time);
            this.headline = itemView.findViewById(R.id.headline);
            this.comment = itemView.findViewById(R.id.comment_button);
            this.like = itemView.findViewById(R.id.like_button);
            this.numLikes = itemView.findViewById(R.id.num_likes);
        }

        public void bind(final Post post) {

            // Find out if this User has liked this post, and set the proper Image representing so
            final Like[] hasLiked = {hasLiked(post)};
            if (hasLiked[0] == null)
                like.setImageResource(R.drawable.ufi_heart);
            else
                like.setImageResource(R.drawable.ufi_heart_active);

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

            // When we click the profile pic or top username, go to ProfileActivity
            this.headline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String username = post.getUser().getUsername();
                    Intent i = new Intent(context, ProfileActivity.class);
                    i.putExtra("username", username);
                    ParseFile pic = post.getUser().getParseFile("profilePic");
                    if (pic != null) {
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
                    i.putExtra("objectId", post.getObjectId());
                    context.startActivity(i);
                }
            });

            // A click on comment icon send us to PostActivity but with the intent to focus in the comment EditText
            this.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PostActivity.class);
                    i.putExtra("objectId", post.getObjectId());
                    i.putExtra("comment", true);
                    context.startActivity(i);
                }
            });

            // Set the correct current number of likes
            numLikes.setText("" + post.getInt("likes") + " likes");

            // If we hit like, change the post's like count, save it, and update the Parse Server's Like class,
            // as well as the like count TextView, and Like icon's image resource
            this.like.setOnClickListener(new View.OnClickListener() {
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
                                    Toast.makeText(context, "Error saving like", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                like.setImageResource(R.drawable.ufi_heart);
                                try {
                                    hasLiked[0].delete();
                                    hasLiked[0] = null;
                                } catch (ParseException ex) {
                                    ex.printStackTrace();
                                    Toast.makeText(context, "Error removing like", Toast.LENGTH_SHORT).show();
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
    }
}
