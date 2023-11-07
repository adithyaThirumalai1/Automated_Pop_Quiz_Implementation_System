package com.example.teacherfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TrueFalseQuizActivity extends AppCompatActivity {

    EditText question_field;
    Button true_button,false_button,set_button,next_button,done_button;

    // Creating Keys For The Database
    public static final String QUESTION_KEY="question";
    public static final String ANSWER_KEY="answer";
    public static final String KEY_NAME="name";
    public static final String KEY_TITLE="title";
    public static final String KEY_TYPE="type";
    public static final String KEY_CODE="code";

    // Creating FireStore Object
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    //  Count of the number of questions in each quiz for document naming
    int questionNumber=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_false_quiz);

        // Initializing the UI
        question_field=findViewById(R.id.question);
        true_button=findViewById(R.id.true_button);
        false_button=findViewById(R.id.false_button);
        set_button=findViewById(R.id.set_button);
        next_button=findViewById(R.id.next_button);
        done_button=findViewById(R.id.done_button);

        // Retrieving all the extras
        final String userEmail=getIntent().getStringExtra("email");
        final String quizTitle=getIntent().getStringExtra("title");

        // Getting instance of the TrueFalseQuestion class
        final TrueFalseQuestion trueFalseQuestion=new TrueFalseQuestion();

        // True Button Functionality
        true_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question=question_field.getText().toString().trim();

                // Checking if the user has entered a question
                if(question.isEmpty())
                {
                    Toast.makeText(TrueFalseQuizActivity.this,"Please Enter a Question",Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    trueFalseQuestion.setQuestion(question);
                    trueFalseQuestion.setAnswer("True");
                    false_button.setEnabled(false);
                }

            }
        });

        // False Button Functionality
        false_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question=question_field.getText().toString().trim();

                // Checking if the user has entered a question
                if(question.isEmpty())
                {
                    Toast.makeText(TrueFalseQuizActivity.this,"Please Enter a Question",Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    trueFalseQuestion.setQuestion(question);
                    trueFalseQuestion.setAnswer("False");
                    true_button.setEnabled(false);
                }
            }
        });

        // Set Button Functionality
        set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String question=trueFalseQuestion.getQuestion();
                String answer=trueFalseQuestion.getAnswer();

                // Checking if both question and answer has been entered
                if(question.isEmpty() || answer.isEmpty())
                {
                    Toast.makeText(TrueFalseQuizActivity.this,"Enter question and Answer",Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    Map<String,Object> data = new HashMap<>();
                    data.put(QUESTION_KEY,question);
                    data.put(ANSWER_KEY,answer);
                    db.collection("teachers").document(userEmail).collection("quizzes").document(quizTitle)
                            .collection("questions").document(String.valueOf(questionNumber)).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(TrueFalseQuizActivity.this,"Question Added Successfully",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TrueFalseQuizActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        // Next Button Functionality
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question_field.getText().clear();
                questionNumber++;
                true_button.setEnabled(true);
                false_button.setEnabled(true);
            }
        });

        // Done Button Functionality
        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> data=new HashMap<>();
                data.put(KEY_NAME,userEmail);
                data.put(KEY_TITLE,quizTitle);
                data.put(KEY_TYPE,"trueFalse");
                String code=randomString();
                data.put(KEY_CODE,code);

                db.collection("references").document(code).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(TrueFalseQuizActivity.this,"Quiz Created Successfully",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TrueFalseQuizActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

                Intent intent=new Intent(TrueFalseQuizActivity.this,LoggedInActivity.class);
                startActivity(intent);
            }
        });

    }

    // Function to generate random string for the quiz code
    public String randomString()
    {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }
}