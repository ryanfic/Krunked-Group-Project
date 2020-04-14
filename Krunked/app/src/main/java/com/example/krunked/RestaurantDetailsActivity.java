package com.example.krunked;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.krunked.RestaurantListRecyclerAdapter.MONDAY_KEY;
import static com.example.krunked.RestaurantListRecyclerAdapter.RESTAURANT_LOCATION;
import static com.example.krunked.RestaurantListRecyclerAdapter.RESTAURANT_NAME;
import static com.example.krunked.RestaurantListRecyclerAdapter.SHARED_PREF;
import static com.example.krunked.RestaurantListRecyclerAdapter.SUNDAY_KEY;

public class RestaurantDetailsActivity extends AppCompatActivity {

    public static final String CURRENT_KEY = "Current Key";
    public static final String CURRENT_DAY = "Current Day";

    //variables
    private ArrayList<Drink> mDrinks = new ArrayList<>();

    private String mPubName;
    private String mPubLoc;

    private int mCurDay;

    private String mCurKey;
    private String mSunKey;
    private String mMonKey;
    private String mTueKey;
    private String mWedKey;
    private String mThuKey;
    private String mFriKey;
    private String mSatKey;

    private DatabaseReference drinkTableRef;

    private RestaurantDetailsRecyclerAdapter mAdapter;

    private TextView mPubNameTxt;
    private TextView mPubLocTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        mPubNameTxt = findViewById(R.id.detail_RestaurantName);
        mPubLocTxt = findViewById(R.id.detail_RestaurantLocation);

        retrieveData(); // get the data from storage

        mPubNameTxt.setText(mPubName); // display the pub name
        mPubLocTxt.setText(mPubLoc); // display the pub location

        initRecyclerView(); // set up the list

        initDrinkData(); // grab the drink data from the server
    }

    @Override
    protected void onResume() {
        super.onResume();

        retrieveData(); // grab the data from storage

        initDrinkData(); // grab the drink data from the server

    }

    @Override
    protected void onPause() {
        super.onPause();

        storeData(); // store the data
    }

    // Grab the drink data from the server
    private void initDrinkData(){

        drinkTableRef = FirebaseDatabase.getInstance().getReference("Drink Table");
        drinkTableRef.child(mCurKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDrinks.clear(); // clear any previous drinks
                for (DataSnapshot drinkSnapshot: dataSnapshot.getChildren()) { // loop through all pubs in the list
                    //set up the drink
                    String name = drinkSnapshot.child("Name").getValue(String.class);
                    String price = drinkSnapshot.child("Price").getValue(String.class);
                    Drink drink = new Drink(name, price);
                    mDrinks.add(drink); // add the drink to the list
                }
                mAdapter.notifyDataSetChanged(); // update the displayed list
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ERR", "Failed to read value.", error.toException());
            }
        });

        setBtnColor(); // set the button color for the current day
    }

    // Set up the recycler view (List)
    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.detail_RecyclerView);
        mAdapter = new RestaurantDetailsRecyclerAdapter(this,mDrinks);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Get the data from the shared preferences
    private void retrieveData(){
        SharedPreferences sh = getSharedPreferences(SHARED_PREF, MODE_PRIVATE); // shared preferences obtained

        mPubName = sh.getString(RESTAURANT_NAME, "Pub"); // get the pub name
        mPubLoc = sh.getString(RESTAURANT_LOCATION, "Location"); // get the pub location
        mSunKey = sh.getString(SUNDAY_KEY, ""); // get the key for sunday
        mMonKey = sh.getString(MONDAY_KEY, ""); // get the key for monday
        mTueKey = sh.getString(RestaurantListRecyclerAdapter.TUESDAY_KEY, ""); // get the tuesday key
        mWedKey = sh.getString(RestaurantListRecyclerAdapter.WEDNESDAY_KEY, ""); // get the key for wednesday
        mThuKey = sh.getString(RestaurantListRecyclerAdapter.THURSDAY_KEY, ""); // get the key for thursday
        mFriKey = sh.getString(RestaurantListRecyclerAdapter.FRIDAY_KEY, ""); // get the key for friday
        mSatKey = sh.getString(RestaurantListRecyclerAdapter.SATURDAY_KEY, ""); // get the key for saturday

        determineCurKey(); // figure out what the current key is (what is currently displayed)

    }

    // Figure out which day's drinks should be displayed
    private void determineCurKey(){
        SharedPreferences sh = getSharedPreferences(SHARED_PREF, MODE_PRIVATE); // shared preferences obtained
        Calendar calendar = Calendar.getInstance(); // calendar reference for determination of day

        mCurDay = sh.getInt(CURRENT_DAY,calendar.get(Calendar.DAY_OF_WEEK)); // if there is a stored value for current day, use that, if not, use the day of the week

        String curKey = "";

        switch (mCurDay) {
            case Calendar.SUNDAY:
                // Current day is Sunday
                curKey = mSunKey;
                break;
            case Calendar.MONDAY:
                // Current day is Monday
                curKey = mMonKey;
                break;
            case Calendar.TUESDAY:
                // Current day is Tuesday
                curKey = mTueKey;
                break;
            case Calendar.WEDNESDAY:
                // Current day is Wednesday
                curKey = mWedKey;
                break;
            case Calendar.THURSDAY:
                // Current day is Thursday
                curKey = mThuKey;
                break;
            case Calendar.FRIDAY:
                // Current day is Friday
                curKey = mFriKey;
                break;
            case Calendar.SATURDAY:
                // Current day is Saturday
                curKey = mSatKey;
                break;
        }

        mCurKey = sh.getString(CURRENT_KEY, curKey); // retrieve info stored in Current Key. If no data stored, use the current day of the week
    }

    //Store data in shared preferences
    private void storeData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(RestaurantListRecyclerAdapter.RESTAURANT_NAME, mPubName);
        edit.putString(RestaurantListRecyclerAdapter.RESTAURANT_LOCATION, mPubLoc);
        edit.putInt(CURRENT_DAY, mCurDay);
        edit.putString(CURRENT_KEY, mCurKey);
        edit.putString(RestaurantListRecyclerAdapter.SUNDAY_KEY, mSunKey);
        edit.putString(RestaurantListRecyclerAdapter.MONDAY_KEY, mMonKey);
        edit.putString(RestaurantListRecyclerAdapter.TUESDAY_KEY, mTueKey);
        edit.putString(RestaurantListRecyclerAdapter.WEDNESDAY_KEY, mWedKey);
        edit.putString(RestaurantListRecyclerAdapter.THURSDAY_KEY, mThuKey);
        edit.putString(RestaurantListRecyclerAdapter.FRIDAY_KEY, mFriKey);
        edit.putString(RestaurantListRecyclerAdapter.SATURDAY_KEY, mSatKey);

        edit.commit(); // commit it all!
    }

    //Show data for sunday's drinks
    public void showSunday(View view){
        if(mCurDay != Calendar.SUNDAY){ // if the day is not already set to sunday
            resetBtnColor(); // reset the other day's button color
            mCurDay = Calendar.SUNDAY; // set the current day
            mCurKey = mSunKey; // set the current key
            initDrinkData();  // get the drink data
        }
    }
    //Show data for monday's drinks
    public void showMonday(View view){
        if(mCurDay != Calendar.MONDAY){ // if the day is not already set to Monday
            resetBtnColor(); // reset the other day's button color
            mCurDay = Calendar.MONDAY; // set the current day
            mCurKey = mMonKey; // set the current key
            initDrinkData(); // get the drink data
        }
    }
    public void showTuesday(View view){
        if(mCurDay != Calendar.TUESDAY){ // if the day is not already set to tuesday
            resetBtnColor(); // reset the other day's button color
            mCurDay = Calendar.TUESDAY; // set the current day
            mCurKey = mTueKey;// set the current key
            initDrinkData(); // get the drink data
        }
    }
    public void showWednesday(View view){
        if(mCurDay != Calendar.WEDNESDAY){ // if the day is not already set to wednesday
            resetBtnColor(); // reset the other day's button color
            mCurDay = Calendar.WEDNESDAY; // set the current day
            mCurKey = mWedKey;// set the current key
            initDrinkData(); // get the drink data
        }
    }
    public void showThursday(View view){
        if(mCurDay != Calendar.THURSDAY){ // if the day is not already set to thursday
            resetBtnColor(); // reset the other day's button color
            mCurDay = Calendar.THURSDAY; // set the current day
            mCurKey = mThuKey;// set the current key
            initDrinkData(); // get the drink data
        }
    }
    public void showFriday(View view){
        if(mCurDay != Calendar.FRIDAY){ // if the day is not already set to friday
            resetBtnColor(); // reset the other day's button color
            mCurDay = Calendar.FRIDAY; // set the current day
            mCurKey = mFriKey;// set the current key
            initDrinkData(); // get the drink data
        }
    }
    public void showSaturday(View view){
        if(mCurDay != Calendar.SATURDAY){ // if the day is not already set to saturday
            resetBtnColor(); // reset the other day's button color
            mCurDay = Calendar.SATURDAY; // set the current day
            mCurKey = mSatKey;// set the current key
            initDrinkData(); // get the drink data
        }
    }

    //Set the color of the currently selected day to pink
    public void setBtnColor(){
        switch (mCurDay) {
            case Calendar.SUNDAY:
                // Current day is Sunday
                findViewById(R.id.sun_button).setBackgroundColor(getResources().getColor(R.color.pinkFontColor));//set sunday btn color
                break;
            case Calendar.MONDAY:
                // Current day is Monday
                findViewById(R.id.mon_button).setBackgroundColor(getResources().getColor(R.color.pinkFontColor));//set monday btn color
                break;
            case Calendar.TUESDAY:
                // Current day is Tuesday
                findViewById(R.id.tue_button).setBackgroundColor(getResources().getColor(R.color.pinkFontColor));//set tuesday btn color
                break;
            case Calendar.WEDNESDAY:
                // Current day is Wednesday
                findViewById(R.id.wed_button).setBackgroundColor(getResources().getColor(R.color.pinkFontColor));//set wednesday btn color
                break;
            case Calendar.THURSDAY:
                // Current day is Thursday
                findViewById(R.id.thu_button).setBackgroundColor(getResources().getColor(R.color.pinkFontColor));//set thursday btn color
                break;
            case Calendar.FRIDAY:
                // Current day is Friday
                findViewById(R.id.fri_button).setBackgroundColor(getResources().getColor(R.color.pinkFontColor));//set friday btn color
                break;
            case Calendar.SATURDAY:
                // Current day is Saturday
                findViewById(R.id.sat_button).setBackgroundColor(getResources().getColor(R.color.pinkFontColor));//set saturday btn color
                break;
        }
    }

    // reset the color of the currently selected day to blue
    public void resetBtnColor(){
        switch (mCurDay) {
            case Calendar.SUNDAY:
                // Current day is Sunday
                findViewById(R.id.sun_button).setBackgroundColor(getResources().getColor(R.color.blueFontColor));//Reset sunday btn color
                break;
            case Calendar.MONDAY:
                // Current day is Monday
                findViewById(R.id.mon_button).setBackgroundColor(getResources().getColor(R.color.blueFontColor));//Reset monday btn color
                break;
            case Calendar.TUESDAY:
                // Current day is Tuesday
                findViewById(R.id.tue_button).setBackgroundColor(getResources().getColor(R.color.blueFontColor));//Reset tuesday btn color
                break;
            case Calendar.WEDNESDAY:
                // Current day is Wednesday
                findViewById(R.id.wed_button).setBackgroundColor(getResources().getColor(R.color.blueFontColor));//Reset wednesday btn color
                break;
            case Calendar.THURSDAY:
                // Current day is Thursday
                findViewById(R.id.thu_button).setBackgroundColor(getResources().getColor(R.color.blueFontColor));//Reset thursday btn color
                break;
            case Calendar.FRIDAY:
                // Current day is Friday
                findViewById(R.id.fri_button).setBackgroundColor(getResources().getColor(R.color.blueFontColor));//Reset friday btn color
                break;
            case Calendar.SATURDAY:
                // Current day is Saturday
                findViewById(R.id.sat_button).setBackgroundColor(getResources().getColor(R.color.blueFontColor));//Reset saturday btn color
                break;
        }
    }

    // when back button clicked, bring user back to the restaurant list activity
    public void onBackBtnClick(View view){
        Intent intent = new Intent(this,RestaurantListActivity.class);
        startActivity(intent);
    }
}

