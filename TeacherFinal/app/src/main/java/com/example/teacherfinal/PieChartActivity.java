package com.example.teacherfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PieChartActivity extends AppCompatActivity {

    PieChart pieChart;

    FirebaseFirestore firebaseFirestore;

    String title,email;

    List<String> temp=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        pieChart= findViewById(R.id.piechart);


        // Retrieving the extras
        title=getIntent().getStringExtra("title");
        email=getIntent().getStringExtra("email");

        firebaseFirestore=FirebaseFirestore.getInstance();

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);
        // This is for the rotation of the pie chart
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues=new ArrayList<>();

        readData(new FirestoreCallBack() {
            @Override
            public void onCallBack(final int[] answer_wrong) {

                CollectionReference collectionReference=firebaseFirestore.collection("teachers").document(email)
                        .collection("quizzes").document(title).collection("questions");
                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            String[] questions=new String[answer_wrong.length];
                            QuerySnapshot queryDocumentSnapshots=task.getResult();
                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                            // For index of the question array
                            int i=0;
                            for(DocumentSnapshot ds:list)
                            {
                                questions[i]=ds.getString("question");
                                i++;
                            }

                        }
                    }
                });


            }
        });


    }

    private void trialMethod(int i)
    {
        Log.d("hey", "trialMethod: "+String.valueOf(i));
    }

    private void readData(final FirestoreCallBack firestoreCallBack)
    {
        CollectionReference collectionReference=firebaseFirestore.collection("teachers").document(email)
                .collection("quizzes").document(title).collection("students_attempted");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    QuerySnapshot queryDocumentSnapshots=task.getResult();
                    List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                    Map<String,Object> map=list.get(0).getData();
                    int[] right_wrong=new int[map.size()];
                    for(DocumentSnapshot ds:list)
                    {
                        String[] temp=new String[map.size()];
                        for(int i=0;i<temp.length;i++)
                        {
                            temp[i]=ds.getString(String.valueOf(i));
                            if(temp[i].equals("incorrect"))
                            {
                                right_wrong[i]=right_wrong[i]+1;
                            }

                        }
                    }
                    //Log.d("hey", "onComplete: "+right_wrong[0]);
                    firestoreCallBack.onCallBack(right_wrong);
                }
                else
                {
                    Toast.makeText(PieChartActivity.this,"Error",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Creating an interface to handle synchronous behaviour of Firestore
    private interface FirestoreCallBack
    {
        void onCallBack(int[] answer_wrong);
    }

}