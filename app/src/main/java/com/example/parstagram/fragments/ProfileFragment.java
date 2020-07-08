package com.example.parstagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.parstagram.EndlessRecyclerViewScrollListener;
import com.example.parstagram.LoginActivity;
import com.example.parstagram.Post;
import com.example.parstagram.PostAdapter;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

// Same as FeedFragment except one extra constraint...
public class ProfileFragment extends Fragment {

    List<Post> posts;
    RecyclerView rvPosts;
    PostAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    EndlessRecyclerViewScrollListener scrolling;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    // Set an adapter for the RecyclerView and query for posts whenever this fragment is opened.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        posts = new ArrayList<>();
        rvPosts = view.findViewById(R.id.rv_posts2);
        refreshLayout = view.findViewById(R.id.swipeContainer2);

        // We need to assign the same layout manager to both RecyclerView and EnlessScrollListener, so they are syncronized
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        // When we scroll, get the next page
        scrolling = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryPosts(page);
            }
        };

        rvPosts.addOnScrollListener(scrolling);

        adapter = new PostAdapter(view.getContext(), posts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(linearLayoutManager);

        // Query for posts even before a refresh
        queryPosts(0);

        // When we refresh, delete the posts we have now and replace with a more updated set
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.clear();
                queryPosts(0);
            }
        });

        // When we press sign out, intent to LoginActivity, but finish this one so the user cannot "back back" to this screen
        (view.findViewById(R.id.signout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent i = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(i);
                getActivity().finish();
            }
        });

    }

    public void queryPosts(int page) {
        ParseQuery<Post> q = ParseQuery.getQuery(Post.class);
        q.include(Post.KEY_USER);

        // The first Post that we need from page x is x * 5 + 1, so set skip(lower bound) as x * 5
        // We need 5 so set limit(upper bound) as x * 5 + 5
        q.setLimit(5 * page + 5);
        q.setSkip(5 * page);
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
