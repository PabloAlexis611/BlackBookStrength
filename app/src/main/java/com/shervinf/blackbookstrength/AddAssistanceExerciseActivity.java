package com.shervinf.blackbookstrength;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AddAssistanceExerciseActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String mainLiftType, collectionName;
    private DocumentReference userDocumentRef = db.collection("users").document(userID);
    private MainLiftAdapter mainLiftAdapter;
    private CustomSearchBar searchBar;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assistance_exercise);
        Intent intent = getIntent();
        mainLiftType = intent.getStringExtra("mainLiftType");
        collectionName = intent.getStringExtra("collectionName");
        toolbarSetup(mainLiftType);
        initList();



        fabSetup();
        recyclerViewSetup();
    }









    //Method that displays back button in toolbar and ends this activity when button is clicked.
    public void toolbarSetup(String type) {
        Toolbar mToolbar = findViewById(R.id.addAssistanceExerciseToolbar);
        TextView mToolbarTextView = findViewById(R.id.addAssistanceExerciseToolbarTextView);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbarTextView.setText(type + " Assitance Exercises");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Your code
                finish();
            }
        });
    }




    private void initList() {
        arrayList = new ArrayList<>();
        arrayList.add("Abs1");
        arrayList.add("Abs2");
        arrayList.add("Abs3");
        arrayList.add("Abs4");
        arrayList.add("Abs5");
        arrayList.add("Abs6");
    }



    private void fabSetup(){
        FloatingActionButton floatingActionButton;
        floatingActionButton = findViewById(R.id.add_assistance_exercise_action_button);
        if (floatingActionButton!=null){
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customAssistanceDialogBuilder();
                    }
                });
            }
    }





    public void customAssistanceDialogBuilder(){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_assistance_exercise_input_dialog, null);

        searchBar = new CustomSearchBar(AddAssistanceExerciseActivity.this, dialogView.findViewById(R.id.homeSearchBar));
        searchBar.hideDivider();
        searchBar.setList(arrayList);
        final EditText reps = dialogView.findViewById(R.id.reps);
        final EditText sets = dialogView.findViewById(R.id.sets);
        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmit);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 5;i++){
                    final int finalI = i;
                    userDocumentRef.collection(collectionName + i)
                            .orderBy("priority",Query.Direction.DESCENDING)
                            .limit(1)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    List<MainLiftPOJO> mainLiftPOJOS = new ArrayList<>();
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        mainLiftPOJOS.add(doc.toObject(MainLiftPOJO.class));
                                    }

                                    if (mainLiftPOJOS.get(0).getPriority().equals(6) || mainLiftPOJOS.isEmpty() ){
                                        userDocumentRef.collection(collectionName + finalI).add(new MainLiftPOJO(searchBar.getText(),"",sets.getText().toString()," x " + reps.getText().toString() +" Reps",7));
                                    }
                                    else if (mainLiftPOJOS.get(0).getPriority() > 6){
                                        userDocumentRef.collection(collectionName + finalI).add(new MainLiftPOJO(searchBar.getText(),"",sets.getText().toString()," x " + reps.getText().toString() +" Reps",mainLiftPOJOS.get(0).getPriority() + 1));
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }








    //Method that find recycler view by the id and displays it.
    public void recyclerViewSetup(){
        Query query = userDocumentRef.collection(collectionName+1).whereGreaterThan("priority",6).orderBy("priority",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MainLiftPOJO> options = new FirestoreRecyclerOptions.Builder<MainLiftPOJO>()
                .setQuery(query, MainLiftPOJO.class)
                .build();
        mainLiftAdapter = new MainLiftAdapter(options);
        RecyclerView mRecyclerView = findViewById(R.id.addAssistanceExerciseRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator( new DefaultItemAnimator());
        mRecyclerView.setAdapter(mainLiftAdapter);
    }





    @Override
    public void onStart() {
        super.onStart();
        mainLiftAdapter.startListening();
    }




    @Override
    public void onStop() {
        super.onStop();
        mainLiftAdapter.stopListening();
    }

}