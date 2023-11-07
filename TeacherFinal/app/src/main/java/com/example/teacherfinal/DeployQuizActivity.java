package com.example.teacherfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DeployQuizActivity extends AppCompatActivity {

    private RecyclerView mFirestoreList;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deploy_quiz);

        // Retrieving the extras
        final String email=getIntent().getStringExtra("email");

        mFirestoreList=findViewById(R.id.firestore_list);
        firebaseFirestore=FirebaseFirestore.getInstance();

        // Query
        Query query=firebaseFirestore.collection("references").whereEqualTo("name",email);

        // Recycler Options
        FirestoreRecyclerOptions<ReferenceModel> options=new FirestoreRecyclerOptions.Builder<ReferenceModel>().
                setQuery(query,ReferenceModel.class).build();

        adapter= new FirestoreRecyclerAdapter<ReferenceModel, ReferenceViewHolder>(options) {
            @NonNull
            @Override
            public ReferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single,parent,false);
                return new ReferenceViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ReferenceViewHolder holder, int position, @NonNull ReferenceModel model) {

                holder.list_title.setText(model.getTitle());
                holder.list_code.setText(model.getCode());
                /*holder.list_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(DeployQuizActivity.this,ListStudentActivity.class);
                        intent.putExtra("email",email);
                        intent.putExtra("title",holder.list_title.getText());
                        startActivity(intent);
                    }
                });*/
            }
        };

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);

    }

    private class ReferenceViewHolder extends RecyclerView.ViewHolder{

        private TextView list_title;
        private TextView list_code;

        public ReferenceViewHolder(@NonNull View itemView) {
            super(itemView);

            list_title=itemView.findViewById(R.id.list_title);
            list_code=itemView.findViewById(R.id.list_code);
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