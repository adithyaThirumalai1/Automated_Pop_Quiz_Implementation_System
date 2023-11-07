package com.example.studentfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        scoreView=findViewById(R.id.textView2);

        int score=getIntent().getIntExtra("score",-1);

        scoreView.setText(String.valueOf(score));
    }

    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(EndActivity.this,MainActivity.class);
        startActivity(intent);
    }


}