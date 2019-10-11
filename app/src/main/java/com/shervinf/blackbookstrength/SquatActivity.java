package com.shervinf.blackbookstrength;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;

public class SquatActivity extends AppCompatActivity {

    private ArrayList<MainLiftPOJO> mArrayList = new ArrayList<>();
    private MainLiftAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squat);

        recyclerViewSetup();
        startingLifts();

    }



    private void startingLifts() {
        MainLiftPOJO Lift = null;
        Lift = new MainLiftPOJO(200.00, "lbs", 40, "% x 3 REPS");
        mArrayList.add(Lift);
        Lift = new MainLiftPOJO(230.00, "lbs", 50, "% x 3 REPS");
        mArrayList.add(Lift);
        Lift = new MainLiftPOJO(260.00, "lbs", 60, "% x 3 REPS");
        mArrayList.add(Lift);
        Lift = new MainLiftPOJO(300.00, "lbs", 65, "% x 3 REPS");
        mArrayList.add(Lift);
        Lift = new MainLiftPOJO(330.00, "lbs", 75, "% x 3 REPS");
        mArrayList.add(Lift);
        Lift = new MainLiftPOJO(380.00, "lbs", 85, "% x 3 REPS");
        mArrayList.add(Lift);

        mAdapter.notifyDataSetChanged();
    }
    public void recyclerViewSetup(){
        RecyclerView mRecyclerView1;
        mRecyclerView1 = (RecyclerView) findViewById(R.id.squatRecyclerView);
        mAdapter = new MainLiftAdapter(mArrayList);
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView1.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView1.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView1.setAdapter(mAdapter);
        Log.d("debugMode", "The application stopped after SquatActivity.java");
    }
}