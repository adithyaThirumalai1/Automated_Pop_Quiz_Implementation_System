package com.example.teacherfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button login_button;
    TextView register, forgot_password;
    EditText email_field,password_field;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing UI Elements
        login_button=findViewById(R.id.login_button);
        register=findViewById(R.id.register);
        email_field=findViewById(R.id.email);
        password_field=findViewById(R.id.password);
        forgot_password=findViewById(R.id.forgot_password);

        fAuth=FirebaseAuth.getInstance();


        // Login button functionality
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Retrieving the extras
                final String email=email_field.getText().toString().trim().toLowerCase(); // // Converting email to lower case before passing it
                String password=password_field.getText().toString().trim();

                // Check if email is entered
                if(email.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Please enter a valid Email-Id",Toast.LENGTH_LONG).show();
                    return;
                }

                // Check if password is entered
                if(password.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Enter Password",Toast.LENGTH_LONG).show();
                    return;
                }


                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent=new Intent(MainActivity.this,LoggedInActivity.class);
                            intent.putExtra("email",email);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Email and Password do not match",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        // Register Button Functionality
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TeacherRegisterActivity.class);
                startActivity(intent);
            }
        });

        // Forgot password functionality
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail=new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(MainActivity.this);
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter email");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Extracting the email
                        String mail=resetMail.getText().toString().trim();
                        if(mail.isEmpty())
                        {
                            Toast.makeText(MainActivity.this,"Please enter a valid email",Toast.LENGTH_LONG).show();
                            return;
                        }
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Reset link sent to your Email",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the pop up
                        AlertDialog dialog1=passwordResetDialog.create();
                        dialog1.cancel();
                    }
                });

                AlertDialog alertDialog=passwordResetDialog.create();
                alertDialog.show();
            }
        });

    }
}