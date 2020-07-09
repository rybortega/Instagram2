package com.example.parstagram.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.parstagram.R;
import com.example.parstagram.fragments.ComposeFragment;
import com.example.parstagram.fragments.FeedFragment;
import com.example.parstagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);

        // When a different Nav item is selected, replace the current frag with an instance
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment frag;
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        frag = new FeedFragment();
                        break;
                    case R.id.create_post:
                        frag = new ComposeFragment();
                        break;
                    default:
                        frag = new ProfileFragment();
                        break;
                }
                // Update fragment by replacing
                fragmentManager.beginTransaction().replace(R.id.frag_container, frag).commit();
                return true;
            }
        });
        bottomNav.setSelectedItemId(R.id.create_post);
    }

}