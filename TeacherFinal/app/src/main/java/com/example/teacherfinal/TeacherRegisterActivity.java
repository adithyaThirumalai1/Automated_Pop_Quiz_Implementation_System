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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class TeacherRegisterActivity extends AppCompatActivity {

    Button register_button;
    EditText username_field,password_field,confirm_password_field,email_field;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);


        // Initializing The UI
        register_button=findViewById(R.id.register_button);
        username_field=findViewById(R.id.username);
        email_field=findViewById(R.id.email);
        password_field=findViewById(R.id.password);
        confirm_password_field=findViewById(R.id.confirm_password);

        // Getting Current Instance of FireBase
        fAuth=FirebaseAuth.getInstance();

        /*if(fAuth.getCurrentUser()!=null)
        {
            Intent intent=new Intent(TeacherRegisterActivity.this,MainActivity.class);
            startActivity(intent);
            // TODO
        }*/

        // Register Button Functionality
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Getting data from all the fields
                // Converting all the fields to lowercase first before storing them
                String email=email_field.getText().toString().trim().toLowerCase();
                String password=password_field.getText().toString().trim().toLowerCase();
                String confirm_password=confirm_password_field.getText().toString().trim().toLowerCase();
                String username=username_field.getText().toString().trim().toLowerCase();

                // Checking if all the data is filled correctly
                if(email.isEmpty())
                {
                    Toast.makeText(TeacherRegisterActivity.this,"Email cannot be left empty",Toast.LENGTH_LONG).show();
                    return;
                }

                if(password.isEmpty())
                {
                    Toast.makeText(TeacherRegisterActivity.this,"Please Enter a valid Password",Toast.LENGTH_LONG).show();
                    return;
                }

                if(confirm_password.isEmpty())
                {
                    Toast.makeText(TeacherRegisterActivity.this,"Please confirm your password",Toast.LENGTH_LONG).show();
                    return;
                }

                if(username.isEmpty())
                {
                    Toast.makeText(TeacherRegisterActivity.this,"Please enter Username",Toast.LENGTH_LONG).show();
                    return;
                }

                // Checking if passwords match
                if(!password.equals(confirm_password))
                {
                    Toast.makeText(TeacherRegisterActivity.this,"Passwords dont match",Toast.LENGTH_LONG).show();
                    return;
                }

                // Creating the User
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(TeacherRegisterActivity.this,"User Registered Successfully",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(TeacherRegisterActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(TeacherRegisterActivity.this,"Error: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });

            }
        });


    }
}