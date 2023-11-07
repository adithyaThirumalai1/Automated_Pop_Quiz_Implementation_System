package com.example.studentfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class McqQuizActivity extends AppCompatActivity implements View.OnClickListener {

    TextView questionDisplay;
    Button option1,option2,option3,option4;

    // Getting the FireStore Reference
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Question Number for retrieval
    int questionNumber=0;

    // This will store the answer for this particular question. Gets updated every time when updateQuestion() runs
    String answer;

    // This variable keeps track of the score.
    int score=0;

    // For Teacher email and Quiz Title
    String email,student_name,title;

    // To record answers
    Map<String,Object> record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq_quiz);

        // Initializing the UI
        questionDisplay=findViewById(R.id.question_text);
        option1=findViewById(R.id.option1);
        option2=findViewById(R.id.option2);
        option3=findViewById(R.id.option3);
        option4=findViewById(R.id.option4);

        record=new HashMap<>();

        // Getting extras from previous activity
        email=getIntent().getStringExtra("email");
        student_name=getIntent().getStringExtra("name");
        title=getIntent().getStringExtra("title");

        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        updateQuestion();

    }

    // This method/function will retrieve the question from the database and display it on the screen.
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
                        Intent intent=new Intent(McqQuizActivity.this,EndActivity.class);
                        intent.putExtra("score",score);

                        //db.collection("students").document(student_name).collection("quizzes").document(title).set(record);

                        db.collection("teachers").document(email).collection("quizzes").document(title)
                                .collection("students_attempted").document(student_name).set(record);
                        startActivity(intent);
                    }
                    questionDisplay.setText(ds.getString("question"));
                    option1.setText(ds.getString("option1"));
                    option2.setText(ds.getString("option2"));
                    option3.setText(ds.getString("option3"));
                    option4.setText(ds.getString("option4"));
                    answer=ds.getString("answer");

                }
                else
                {
                    Toast.makeText(McqQuizActivity.this,"Error",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(McqQuizActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        //questionNumber++;

    }

    private void updateScore()
    {
        score++;
    }

    private void updateQuestionNumber()
    {
        questionNumber++;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.option1:
                if(option1.getText().toString().equals(answer))
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
            case R.id.option2:
                if(option2.getText().toString().equals(answer))
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
            case R.id.option3:
                if(option3.getText().toString().equals(answer))
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
            case R.id.option4:
                if(option4.getText().toString().equals(answer))
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

    // Overriding the default functionality of back button.
    @Override
    public void onBackPressed()
    {

    }

    @Override
    public void onStop() {

        super.onStop();
        finish();
    }

}