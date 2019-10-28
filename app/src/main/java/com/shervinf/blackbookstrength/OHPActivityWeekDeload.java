package com.shervinf.blackbookstrength;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class OHPActivityWeekDeload extends AppCompatActivity {
    private ArrayList<MainLiftPOJO> mArrayList = new ArrayList<>();
    private MainLiftAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ohp);

        //Calling methods
        toolbarSetup();
        recyclerViewSetup();
        prepareData();
    }




    //Method that creates back navigation button and finishes this activity when pressed.
    public void toolbarSetup() {
        Toolbar mToolbar = findViewById(R.id.ohpToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Your code
                finish();
            }
        });
    }




    //Method that adds data into object array list type SettingsPOJO and display it in recycler view.
    private void prepareData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DocumentReference docRef = db.collection("users").document(userID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("BlackBookStrength", "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    UserPOJO newUser = snapshot.toObject(UserPOJO.class);
                    Double ohpMax = newUser.getOhpMax();
                    MainLiftPOJO Lift;
                    Lift = new MainLiftPOJO(ohpMax * MainLiftPOJO.PERCENT_40,"lbs", 40, "% x 5 REPS");
                    mArrayList.add(Lift);
                    Lift = new MainLiftPOJO(ohpMax * MainLiftPOJO.PERCENT_50,"lbs",50, "% x 5 REPS");
                    mArrayList.add(Lift);
                    Lift = new MainLiftPOJO(ohpMax * MainLiftPOJO.PERCENT_60,"lbs",60, "% x 5 REPS");
                    mArrayList.add(Lift);
                    Lift = new MainLiftPOJO(ohpMax * MainLiftPOJO.PERCENT_40,"lbs",40, "% x 5 REPS");
                    mArrayList.add(Lift);
                    Lift = new MainLiftPOJO(ohpMax * MainLiftPOJO.PERCENT_50,"lbs",50, "% x 5 REPS");
                    mArrayList.add(Lift);
                    Lift = new MainLiftPOJO(ohpMax * MainLiftPOJO.PERCENT_60,"lbs",60, "% x 5 REPS");
                    mArrayList.add(Lift);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.d("BlackBookStrength", "Current data: null");
                }
            }
        });
    }




    //Method that find recycler view by the id and displays it.
    public void recyclerViewSetup(){
        RecyclerView mRecyclerView1;
        mRecyclerView1 = findViewById(R.id.ohpRecyclerView);
        mAdapter = new MainLiftAdapter(mArrayList, new OnMainLiftClickListener() {
            @Override
            public void onMainLiftViewItemClicked(int position, int id) {
            }
        });
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView1.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView1.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView1.setAdapter(mAdapter);
        Log.d("debugMode", "The application stopped after SquatActivityWeek1.java");
    }
}
