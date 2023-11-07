package com.example.teacherfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LoggedInActivity extends AppCompatActivity {

    Button create_quiz_button, log_out_button, deploy_quiz_button, statistics_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        create_quiz_button=findViewById(R.id.create_quiz_button);
        log_out_button=findViewById(R.id.log_out);
        deploy_quiz_button=findViewById(R.id.deploy_quiz_button);
        statistics_button=findViewById(R.id.stats_button);

        final String email=getIntent().getStringExtra("email");

        // Create Button Functionality
        create_quiz_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoggedInActivity.this, QuizTitleActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });


        // Deploy Functionality
        deploy_quiz_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoggedInActivity.this,DeployQuizActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });

        // Log out Functionality
        log_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // Statistics functionality
        statistics_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoggedInActivity.this,ListQuizActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });

    }

    public void logout()
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(LoggedInActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}