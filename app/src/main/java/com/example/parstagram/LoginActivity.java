package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null)
            goToMain();

        etUsername = findViewById(R.id.entered_username);
        etPassword = findViewById(R.id.entered_password);

        // When button login is clicked, we check if username and password are accurate
        (findViewById(R.id.login_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logInInBackground(etUsername.getText().toString(), etPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e != null) {
                            Toast.makeText(LoginActivity.this, "Wrong Username/ Password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        goToMain();
                    }
                });
            }
        });

        // When user clicks register, we set up their account then go to MainActivity
        (findViewById(R.id.register_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser new_user = new ParseUser();
                new_user.setUsername(etUsername.getText().toString());
                new_user.setPassword(etPassword.getText().toString());
                try {
                    new_user.signUp();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                goToMain();
            }
        });

    }

    // Intents to MainActivity
    private void goToMain() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}