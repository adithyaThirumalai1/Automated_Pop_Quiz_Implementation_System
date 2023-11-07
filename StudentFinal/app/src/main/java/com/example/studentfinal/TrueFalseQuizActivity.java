package com.example.studentfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TrueFalseQuizActivity extends AppCompatActivity implements View.OnClickListener {


    TextView question;
    Button true_button,false_button;

    // Getting the FireStore Reference
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Question Number for retrieval
    int questionNumber=0;

    // This variable keeps track of the score.
    int score=0;

    // For Teacher email and Quiz Title
    String email,student_name,title;

    // This will store the answer for this particular question. Gets updated every time when updateQuestion() runs
    String answer;

    // To record answers
    Map<String,Object> record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_false_quiz);

        // Initializing the UI
        question=findViewById(R.id.question_text);
        true_button=findViewById(R.id.true_button);
        false_button=findViewById(R.id.false_button);

        record=new HashMap<>();

        // Getting extras from previous activity
        email=getIntent().getStringExtra("email");
        student_name=getIntent().getStringExtra("name");
        title=getIntent().getStringExtra("title");

        true_button.setOnClickListener(this);
        false_button.setOnClickListener(this);

        updateQuestion();


    }


    private void updateQuestion()
    {
        DocumentReference dref=db.collection("teachers").document(email).
                collection("quizzes").document(title).
                collection("questions").document(String.valueOf(questionNumber));

        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot ds=task.getResult();
                    if(!ds.exists())
                    {
                        Intent intent=new Intent(TrueFalseQuizActivity.this,EndActivity.class);
                        intent.putExtra("score",score);
                        db.collection("teachers").document(email).collection("quizzes").document(title)
                                .collection("students_attempted").document(student_name).set(record);
                        startActivity(intent);
                    }
                    question.setText(ds.getString("question"));
                    answer=ds.getString("answer");
                }
                else
                {
                    Toast.makeText(TrueFalseQuizActivity.this,"Error",Toast.LENGTH_LONG).show();
                    // TODO : Update this to return to the home screen/ main activity. Do the same in mcq quiz as well
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TrueFalseQuizActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        //questionNumber++;
    }

    private void updateQuestionNumber()
    {
        questionNumber++;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.true_button:
                if(true_button.getText().equals(answer))
                {
                    updateScore();
                    record.put(String.valueOf(questionNumber),"correct");
                }
                else
                {
                    record.put(String.valueOf(questionNumber),"incorrect");
                }
                updateQuestionNumber();
                updateQuestion();
                break;

            case R.id.false_button:
                if(false_button.getText().equals(answer))
                {
                    updateScore();
                    record.put(String.valueOf(questionNumber),"correct");
                }
                else
                {
                    record.put(String.valueOf(questionNumber),"incorrect");
                }
                updateQuestionNumber();
                updateQuestion();
                break;
        }

    }

    private void updateScore()
    {
        score++;
    }

    // Overriding the default functionality of back button.
    @Override
    public void onBackPressed()
    {}

    @Override
    public void onStop() {

        super.onStop();
        finish();
    }

}