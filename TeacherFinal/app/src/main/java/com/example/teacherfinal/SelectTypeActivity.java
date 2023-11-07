package com.example.teacherfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectTypeActivity extends AppCompatActivity {

    Button mcq;
    Button trueFalse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type);

        mcq=findViewById(R.id.mcq_button);
        trueFalse=findViewById(R.id.trueFalse_button);

        // Retrieving the extras
        final String email=getIntent().getStringExtra("email");
        final String title=getIntent().getStringExtra("title");

        // For MCQ Quiz Type
        mcq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectTypeActivity.this,McqQuizActivity.class);
                intent.putExtra("email",email);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });

        // For True/False Quiz Type
        trueFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectTypeActivity.this,TrueFalseQuizActivity.class);
                intent.putExtra("email",email);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });


    }
}