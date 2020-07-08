package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ViewHolder> {

    List<Post> posts;
    Context context;

    public ProfilePostAdapter(Context context, List<Post> posts){
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View to_add = LayoutInflater.from(context).inflate(R.layout.post_preview, parent, false);
        return new ViewHolder(to_add);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView previewPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.previewPic = itemView.findViewById(R.id.preview_pic);
        }
        public void bind(final Post post){
            Glide.with(context).load(post.getImage().getUrl()).into(this.previewPic);
            this.previewPic.setOnClickListener(new View.OnClickListener() {
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
