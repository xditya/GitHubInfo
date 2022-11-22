package me.xditya.githubinfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class ViewProfile extends AppCompatActivity {

    ImageView imgProfilePic;
    TextView tvViewDetails;
    Button btnOpenProfile, btnMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        // hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        imgProfilePic = findViewById(R.id.imgProfilePic);
        tvViewDetails = findViewById(R.id.tvViewDetails);
        btnOpenProfile = findViewById(R.id.btnOpenProfile);
        btnMainActivity = findViewById(R.id.btnMainActivity);

        // keep open-profile and back button hidden until search is completed.
        btnOpenProfile.setVisibility(View.GONE);
        btnMainActivity.setVisibility(View.GONE);

        Intent intent = getIntent();
        Bundle bnd = intent.getExtras();

        String username = (String) bnd.get("username");

        // fetch results from GitHub API

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(ViewProfile.this);
        String url = "https://api.github.com/users/" + username;
        final String[] githubProfileUrl = {""};
        final String[] msgBuilder = {""};

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (!jsonResponse.isNull("message")) {
                            Toast.makeText(ViewProfile.this, "No such user found on GitHub!", Toast.LENGTH_SHORT).show();
                            btnMainActivity.setVisibility(View.VISIBLE);
                        }
                        else {
                            githubProfileUrl[0] = (String) jsonResponse.get("html_url");
                            msgBuilder[0] += "UserName: " + jsonResponse.get("login");
                            msgBuilder[0] += "\nID: " + jsonResponse.get("id");
                            msgBuilder[0] += "\nURL: " + githubProfileUrl[0];
                            String imageURL = (String) jsonResponse.get("avatar_url");
                            Glide.with(ViewProfile.this)
                                    .load(imageURL)
                                    .into(imgProfilePic);
                            tvViewDetails.setText(msgBuilder[0]);
                            btnOpenProfile.setVisibility(View.VISIBLE);
                            btnOpenProfile.setOnClickListener(view -> {
                                Intent intent12 = new Intent(Intent.ACTION_VIEW);
                                intent12.setData(Uri.parse(githubProfileUrl[0]));
                                startActivity(intent12);
                            });
                            btnMainActivity.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    tvViewDetails.setText(R.string.userNotFound);
                    Toast.makeText(ViewProfile.this, "Error fetching details from GitHub API!", Toast.LENGTH_SHORT).show();
                    btnMainActivity.setVisibility(View.VISIBLE);
                });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        btnMainActivity.setOnClickListener(view -> finish());
    }
}