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

public class McqQuizActivity extends AppCompatActivity {


    EditText question_field, option1_field, option2_field, option3_field, option4_field, answer_field;
    Button set_button,next_button,done_button;

    // Creating the keys for database
    public static final String KEY_QUESTION="question";
    public static final String KEY_OPTION1="option1";
    public static final String KEY_OPTION2="option2";
    public static final String KEY_OPTION3="option3";
    public static final String KEY_OPTION4="option4";
    public static final String KEY_ANSWER="answer";
    public static final String KEY_NAME="name";
    public static final String KEY_TITLE="title";
    public static final String KEY_TYPE="type";
    public static final String KEY_CODE="code";

    // Getting firestore instance
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    // Count of the number of questions in each quiz for document naming
    int questionNumber=0; // Question numbering will start with zero

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq_quiz);

        // Getting extras from previous activity
        final String userEmail=getIntent().getStringExtra("email");
        final String quizTitle=getIntent().getStringExtra("title");

        // Initializing UI
        question_field=findViewById(R.id.question);
        option1_field=findViewById(R.id.option1);
        option2_field=findViewById(R.id.option2);
        option3_field=findViewById(R.id.option3);
        option4_field=findViewById(R.id.option4);
        answer_field=findViewById(R.id.answer);
        set_button=findViewById(R.id.set_question);
        next_button=findViewById(R.id.next_question);
        done_button=findViewById(R.id.done);


        // Setting Question
        set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Getting data from all the fields
                String question=question_field.getText().toString().trim();
                String option1=option1_field.getText().toString().trim();
                String option2=option2_field.getText().toString().trim();
                String option3=option3_field.getText().toString().trim();
                String option4=option4_field.getText().toString().trim();
                String answer=answer_field.getText().toString().trim();

                // Checking if any field is empty
                if(question.isEmpty() || option1.isEmpty()
                        || option2.isEmpty() || option3.isEmpty()
                        || option4.isEmpty() || answer.isEmpty())
                {
                    Toast.makeText(McqQuizActivity.this,"Please Enter All the Fields",Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    if(answer.equals(option1) || answer.equals(option2) || answer.equals(option3) || answer.equals(option4))
                    {
                        // Putting the Question in a map
                        Map<String,Object> data=new HashMap<>();
                        data.put(KEY_QUESTION,question);
                        data.put(KEY_OPTION1,option1);
                        data.put(KEY_OPTION2,option2);
                        data.put(KEY_OPTION3,option3);
                        data.put(KEY_OPTION4,option4);
                        data.put(KEY_ANSWER,answer);

                        // Putting the map in the firestore database
                        db.collection("teachers").document(userEmail).collection("quizzes").document(quizTitle)
                                .collection("questions").document(String.valueOf(questionNumber)).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(McqQuizActivity.this,"Question Added Successfully",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(McqQuizActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });

                        questionNumber++;
                        set_button.setEnabled(false);
                    }
                    else
                    {
                        Toast.makeText(McqQuizActivity.this,"Your answer does not match any of your options",Toast.LENGTH_LONG).show();
                        return;
                    }




                }

            }
        });

        // Next Button
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                question_field.getText().clear();
                option1_field.getText().clear();
                option2_field.getText().clear();
                option3_field.getText().clear();
                option4_field.getText().clear();
                answer_field.getText().clear();
                //questionNumber++;
                set_button.setEnabled(true);
            }
        });

        // Done Button
        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> data=new HashMap<>();
                data.put(KEY_NAME,userEmail);
                data.put(KEY_TITLE,quizTitle);
                data.put(KEY_TYPE,"mcq");
                String code=randomString(); // Randomly generated string
                data.put(KEY_CODE,code);

                db.collection("references").document(code).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(McqQuizActivity.this,"Quiz Created Successfully",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(McqQuizActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });


                Intent intent=new Intent(McqQuizActivity.this,LoggedInActivity.class);
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