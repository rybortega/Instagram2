package com.example.parstagram.fragments;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parstagram.MainActivity;
import com.example.parstagram.Post;
import com.example.parstagram.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class ComposeFragment extends Fragment {

    EditText etDescription;
    ImageView ivPicture;
    File photoFile;

    public ComposeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etDescription = view.findViewById(R.id.description);
        ivPicture = view.findViewById(R.id.post_picture);

        // When we click submit, check that there is a picture attached, then save post
        (view.findViewById(R.id.submit_post)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photoFile == null || ivPicture.getDrawable() == null) {
                    Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                savePost(etDescription.getText().toString(), ParseUser.getCurrentUser());
            }
        });

        // When we click to get picture, open up camera and store result File into photoFile
        (view.findViewById(R.id.take_picture)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoFile = getPhotoFileUri("photo.jpg");

                Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                startActivityForResult(i, 100);

            }
        });
    }

    public File getPhotoFileUri(String s) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "APP_TAG");

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
        post.setImage(new ParseFile(photoFile));

        // Save in background for efficiency
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "Error saving post", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), "Saved post", Toast.LENGTH_SHORT).show();

                //Trash what we just saved
                etDescription.setText("");
                ivPicture.setImageResource(0);
            }
        });
    }

    // When we come back from taking a picture, Set the preview as the new picture's BitMap
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                ivPicture.setImageBitmap(BitmapFactory.decodeFile(photoFile.getAbsolutePath()));

                return;
            }
            Toast.makeText(getContext(), "Error taking image", Toast.LENGTH_SHORT).show();
        }
    }

}