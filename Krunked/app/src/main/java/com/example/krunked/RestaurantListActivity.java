package com.example.krunked;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RestaurantListActivity extends AppCompatActivity {
    private ArrayList<Pub> mPubs = new ArrayList<>(); // list of pubs

    private FirebaseDatabase databaseRef; // reference to firebase database

    private DatabaseReference pubTableRef; // reference to the restaurant/pub table

    private RestaurantListRecyclerAdapter mAdapter; // the adapter of the recycler view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        databaseRef = FirebaseDatabase.getInstance(); // set up the database reference

        initRecyclerView(); // set up the recycler view (list)

        initPubData(); // get the pub data from the database
    }

    // Set up the data
    private void initPubData(){
        pubTableRef = databaseRef.getReference("Restaurant Table");
        pubTableRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPubs.clear(); // remove any previously stored pubs
                for (DataSnapshot pubSnapshot: dataSnapshot.getChildren()) { // loop through all pubs in the list
                    //set up the pub
                    String name = pubSnapshot.child("Name").getValue(String.class);
                    String loc = pubSnapshot.child("Location").getValue(String.class);
                    String sun = pubSnapshot.child("Sun_key").getValue(Long.class).toString();
                    String mon = pubSnapshot.child("Mon_key").getValue(Long.class).toString();
                    String tue = pubSnapshot.child("Tue_key").getValue(Long.class).toString();
                    String wed = pubSnapshot.child("Wed_key").getValue(Long.class).toString();
                    String thu = pubSnapshot.child("Thu_key").getValue(Long.class).toString();
                    String fri = pubSnapshot.child("Fri_key").getValue(Long.class).toString();
                    String sat = pubSnapshot.child("Sat_key").getValue(Long.class).toString();
                    Pub pub = new Pub(name, loc, sun, mon, tue, wed, thu, fri, sat);
                    mPubs.add(pub); // add the pub to the list of pubs
                }
                mAdapter.notifyDataSetChanged(); // update the displayed list of pubs
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ERR", "Failed to read value.", error.toException());
            }
        });

    }



    // Set up the RecyclerView
    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.restaurantListRecyclerView); // get the recyclerview reference
        mAdapter = new RestaurantListRecyclerAdapter(this, mPubs); // create the adapter
        recyclerView.setAdapter(mAdapter); // set the recyclerview adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // set the layout manager

    }

    // When the user clicks the back button, bring them to the main activity
    public void onBackBtnClick(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
