package com.example.teacherfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BarChartActivity extends AppCompatActivity {

    BarChart barChart;
    ArrayList<BarEntry> barEntriesArrayList;
    ArrayList<String > lableName;

    FirebaseFirestore firebaseFirestore;

    String title,email;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        barChart=findViewById(R.id.barChart);

        barEntriesArrayList = new ArrayList<>();
        lableName  = new ArrayList<>();


        linearLayout=findViewById(R.id.linear_layout);

        firebaseFirestore=FirebaseFirestore.getInstance();

        // Retrieving the extras
        title=getIntent().getStringExtra("title");
        email=getIntent().getStringExtra("email");


        readData(new FirestoreCallBack() {
            @Override
            public void onCallBack(final int[] wrong_answer) {

                CollectionReference collectionReference=firebaseFirestore.collection("teachers").document(email)
                        .collection("quizzes").document(title).collection("questions");
                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            String[] questions=new String[wrong_answer.length];
                            QuerySnapshot queryDocumentSnapshots=task.getResult();
                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                            // For index of the question array
                            int i=0;
                            for(DocumentSnapshot ds:list)
                            {
                                questions[i]=ds.getString("question");
                                i++;
                            }

                            for(int j=0;j<wrong_answer.length;j++)
                            {
                                barEntriesArrayList.add(new BarEntry(j,wrong_answer[j]));
                                lableName.add(questions[j]);
                            }
                            BarDataSet barDataSet = new BarDataSet(barEntriesArrayList,"Quiz Statistics");
                            barDataSet.setColors(ColorTemplate.LIBERTY_COLORS);

                            Description description = new Description();
                            description.setText("Questions");
                            barChart.setDescription(description);

                            BarData barData = new BarData(barDataSet);
                            barChart.setData(barData);

                            XAxis xAxis=barChart.getXAxis();


                            //xAxis.setValueFormatter(new IndexAxisValueFormatter(lableName));
                            xAxis.setDrawGridLines(false);
                            xAxis.setDrawAxisLine(false);
                            xAxis.setGranularity(1f);
                            xAxis.setLabelCount(lableName.size());
                            xAxis.setLabelRotationAngle(270);
                            barChart.animateY(2000);
                            barChart.invalidate();

                            for(int j=0;j<lableName.size();j++)
                            {
                                TextView textView=new TextView(BarChartActivity.this);
                                textView.setText(j+1+" : "+lableName.get(j));
                                linearLayout.addView(textView);
                            }


                        }
                    }
                });

            }
        });

    }

    /*private void legend(String[] s)
    {
        for(int i=0;i<s.length;i++)
        {
            TextView textView=new TextView(BarChartActivity.this);
            textView.setText();
        }
    }*/

    // The data which is needed
    // 1) How many students went wrong in which particular question of the quiz
            // This can be retrieved from the teachers collection and students_attempted sub collection. Problem is storing it in an appropriate DS
            // For the BC i need to put in only numbers and labels as bar entries
            // For the total i will also need the total number of questions

    // 2) Question names for the legend


    private void readData(final FirestoreCallBack firestoreCallBack)
    {
        CollectionReference collectionReference=firebaseFirestore.collection("teachers").document(email)
                .collection("quizzes").document(title).collection("students_attempted");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {

                    // Have to code for empty condition..
                    if(task.getResult().isEmpty())
                    {
                        Toast.makeText(BarChartActivity.this,"No Statistics available because the quiz has not been attempted",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(BarChartActivity.this,LoggedInActivity.class);
                        startActivity(intent);
                    }

                    QuerySnapshot queryDocumentSnapshots=task.getResult();
                    List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                    Map<String,Object> map=list.get(0).getData();
                    int[] wrong_answer=new int[map.size()];
                    for(DocumentSnapshot ds:list)
                    {
                        String[] temp=new String[map.size()];
                        for(int i=0;i<temp.length;i++)
                        {
                            temp[i]=ds.getString(String.valueOf(i));
                            if(temp[i].equals("incorrect"))
                            {
                                wrong_answer[i]=wrong_answer[i]+1;
                            }
                        }
                    }
                    firestoreCallBack.onCallBack(wrong_answer);
                }
                else
                {
                    Toast.makeText(BarChartActivity.this,"Error",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    // Creating an interface to handle synchronous behaviour of Firestore
    private interface FirestoreCallBack
    {
        void onCallBack(int[] wrong_answer);
    }

}