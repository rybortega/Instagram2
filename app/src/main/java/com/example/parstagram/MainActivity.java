package com.example.parstagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etDescription;
    ImageView ivPicture;
    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDescription = findViewById(R.id.description);
        ivPicture = findViewById(R.id.post_picture);

        // When we click submit, check that there is a picture attached, then save post
        (findViewById(R.id.submit_post)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePost(etDescription.getText().toString(), ParseUser.getCurrentUser());
            }
        });

    }

    public File getPhotoFileUri(String s) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "APP_TAG");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("APP_TAG", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + s);

        return file;
    }

    // Create a new Post and push this to MongoDB
    public void savePost(String description, ParseUser currentUser) {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(currentUser);

        // Save in background for efficiency
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "Error saving post", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(MainActivity.this, "Saved post", Toast.LENGTH_SHORT).show();

                //Trash what we just saved
                etDescription.setText("");
                ivPicture.setImageResource(0);
            }
        });
    }

    // Retrieve a list of all the posts
    public void queryPosts() {
        ParseQuery<Post> q = ParseQuery.getQuery(Post.class);
        q.include(Post.KEY_USER);
        q.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "Error getting posts", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Post p : objects) {
                    System.out.println(p.getDescription() + " " + p.getUser().getUsername());
                }
            }
        });
    }
}