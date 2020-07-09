package com.example.parstagram.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parstagram.models.Comment;
import com.example.parstagram.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    List<Comment> comments;
    Context context;


    public CommentAdapter(Context context, List<Comment> comments){
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View to_add = LayoutInflater.from(context).inflate(R.layout.comment, parent, false);
        return new ViewHolder(to_add);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvCommenter;
        TextView tvCommentText;
        TextView tvCommentDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvCommenter = itemView.findViewById(R.id.commenter);
            this.tvCommentText = itemView.findViewById(R.id.comment_text);
            this.tvCommentDate = itemView.findViewById(R.id.comment_date);
        }
        public void bind(Comment comment){
            this.tvCommenter.setText("" + comment.getUser().getUsername());
            this.tvCommentText.setText("" + comment.getCommentText());
            this.tvCommentDate.setText("" + comment.getCreatedAt().toString());
        }
    }
}
