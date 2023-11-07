package com.example.studentfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {


    EditText name_field, code_field;
    Button enter_button;

    // Getting the FireStore instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing the UI
        name_field=findViewById(R.id.name);
        code_field=findViewById(R.id.room_code);
        enter_button=findViewById(R.id.enter);

        enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=name_field.getText().toString().trim().toLowerCase();
                String code=code_field.getText().toString().trim();

                // Checking if both fields have been entered
                if(name.isEmpty() || code.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Please Enter all the Fields",Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    DocumentReference dref=db.collection("references").document(code);
                    dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot ds=task.getResult();
                            if(!ds.exists())
                            {
                                Toast.makeText(MainActivity.this,"Incorrect Room Code",Toast.LENGTH_LONG).show();
                                return;
                            }
                            // Retrieving the email of the professor and title of the quiz
                            final String email=ds.getString("name");
                            final String title=ds.getString("title");
                            final String type=ds.getString("type");

                            // Sending after checking the type of the quiz
                            if(type.equals("mcq"))
                            {
                                Intent intent=new Intent(MainActivity.this,McqQuizActivity.class);

                                // Passing additional information to the next Activity
                                intent.putExtra("email",email);
                                intent.putExtra("name",name);
                                intent.putExtra("title",title);
                                startActivity(intent);
                            }

                            if(type.equals("trueFalse"))
                            {
                                Intent intent=new Intent(MainActivity.this,TrueFalseQuizActivity.class);

                                // Passing additional information to the next Activity
                                intent.putExtra("email",email);
                                intent.putExtra("name",name);
                                intent.putExtra("title",title);
                                startActivity(intent);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });


    }

    @Override
    public void onBackPressed()
    {

    }

}