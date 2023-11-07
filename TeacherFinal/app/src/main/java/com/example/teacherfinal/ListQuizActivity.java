package com.example.teacherfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ListQuizActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;

    // For extras
    String email;

    private RecyclerView mFirestoreList;
    FirestoreRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_quiz);

        mFirestoreList=findViewById(R.id.quiz_list_view);


        //Retrieving extras
        email=getIntent().getStringExtra("email");

        firebaseFirestore=FirebaseFirestore.getInstance();

        // Query
        Query query=firebaseFirestore.collection("references").whereEqualTo("name",email);


        // Recycler Options
        FirestoreRecyclerOptions<QuizTitleModel> options=new FirestoreRecyclerOptions.Builder<QuizTitleModel>().
                setQuery(query,QuizTitleModel.class).build();


        adapter=new FirestoreRecyclerAdapter<QuizTitleModel,QuizTitleViewHolder>(options) {

            @NonNull
            @Override
            public QuizTitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_quiz_item,parent,false);
                return new QuizTitleViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final QuizTitleViewHolder holder, int position, @NonNull QuizTitleModel model) {
                holder.quiz_title.setText(model.getTitle());
                holder.quiz_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(ListQuizActivity.this,BarChartActivity.class);
                        intent.putExtra("title",holder.quiz_title.getText());
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                });
            }
        };

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);

    }


    private class QuizTitleViewHolder extends RecyclerView.ViewHolder{

        private TextView quiz_title;

        public QuizTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            quiz_title=itemView.findViewById(R.id.quiz_name);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


}