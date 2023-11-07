package com.example.teacherfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListStudentActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;

    // For extras
    String email,title;

    private RecyclerView mFirestoreList;
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_student);

        mFirestoreList=findViewById(R.id.student_list_view);
        //Retrieving extras
        email=getIntent().getStringExtra("email");
        title=getIntent().getStringExtra("title");

        firebaseFirestore=FirebaseFirestore.getInstance();

        // Query
        Query query=firebaseFirestore.collection("teachers").document(email).collection("quizzes")
                .document(title).collection("students_attempted");

        if(query.limit(1).get().getResult().isEmpty())
        {
            Toast.makeText(ListStudentActivity.this,"csdcdsc",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(ListStudentActivity.this,LoggedInActivity.class);
            startActivity(intent);
        }

    }
}