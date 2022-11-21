package me.xditya.githubinfo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    EditText edtUsername;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        edtUsername = findViewById(R.id.edtUserName);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(view -> {
            String username = edtUsername.getText().toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(MainActivity.this, "Give a username!", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(MainActivity.this, "Opening Profile Info...", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), ViewProfile.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
}
