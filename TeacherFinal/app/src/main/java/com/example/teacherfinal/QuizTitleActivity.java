package com.example.teacherfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class QuizTitleActivity extends AppCompatActivity {

    EditText title_field;
    Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_title);

        // Retrieving the extras
        final String email=getIntent().getStringExtra("email");

        // Initializing UI
        title_field=findViewById(R.id.quizTitle);
        enter=findViewById(R.id.enter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title=title_field.getText().toString().trim().toLowerCase();
                if(title.isEmpty())
                {
                    Toast.makeText(QuizTitleActivity.this,"Please Enter The Title",Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    Intent intent=new Intent(QuizTitleActivity.this,SelectTypeActivity.class);
                    intent.putExtra("email",email);
                    intent.putExtra("title",title);
                    startActivity(intent);
                }
            }
        });


    }
}