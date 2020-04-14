package com.example.krunked;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static com.example.krunked.RestaurantDetailsActivity.CURRENT_DAY;
import static com.example.krunked.RestaurantDetailsActivity.CURRENT_KEY;


public class RestaurantListRecyclerAdapter extends RecyclerView.Adapter<RestaurantListRecyclerAdapter.ViewHolder> { // the generic is the ViewHolder that will be shown in the recycler view
    private static final String TAG = "RestaurantListRecyclerAdapter";
    public static final String SHARED_PREF = "Shared Preferences";
    public static final String RESTAURANT_NAME = "Restaurant Name";
    public static final String RESTAURANT_LOCATION = "Restaurant Location";
    public static final String SUNDAY_KEY = "Sunday Key";
    public static final String MONDAY_KEY = "Monday Key";
    public static final String TUESDAY_KEY = "Tuesday Key";
    public static final String WEDNESDAY_KEY = "Wednesday Key";
    public static final String THURSDAY_KEY = "Thursday Key";
    public static final String FRIDAY_KEY = "Friday Key";
    public static final String SATURDAY_KEY = "Saturday Key";

    private ArrayList<Pub> mPubs = new ArrayList<>(); //list of pubs

    private Context mContext;


    // constructor
    public RestaurantListRecyclerAdapter(Context context, ArrayList<Pub> pubs) {
        mPubs = pubs;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) // Get the context from the parent
                .inflate(R.layout.restaurant_listitem, parent, false); // make the thing from the restaurant_listitem
        ViewHolder holder = new ViewHolder(view); // make the viewholder from the info that was inflated
        return holder;
    }

    // on bind
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.pubNameText.setText(mPubs.get(position).getName());
        holder.pubLocText.setText(mPubs.get(position).getLocation());
        holder.pubNameText.setOnClickListener(new View.OnClickListener(){ // On click listener for the pub name
            @Override
            public void onClick(View view){ // stuff to do when the pub name is clicked
                onNameTextClick(position);
            }
        });

        //TO DO LATER- INTEGRATE MAPS
        /*holder.pubLocText.setOnClickListener(new View.OnClickListener(){ // on click listener for the pub location
            @Override
            public void onClick(View view){ // stuff to do when pub location text is clicked

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mPubs.size(); // the number of items is equal to the number of items in the pub name list
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{ // the thing that holds the info for the single list item
        TextView pubNameText; // the name of the pub
        TextView pubLocText; // the location of the pub
        ConstraintLayout parentLayout; // the layout that holds the list item

        public ViewHolder(View itemView){
            super(itemView);
            pubNameText = itemView.findViewById(R.id.pubNameText);
            pubLocText = itemView.findViewById(R.id.pubLocationText);
            parentLayout = itemView.findViewById(R.id.restaurant_Layout);
        }
    }

    //When you click on the name of the pub, take the user to the details page about that pub
    public void onNameTextClick(int position){
        Intent intent = new Intent(mContext,RestaurantDetailsActivity.class);

        // Get the info of the clicked on restaurant
        String pubName = mPubs.get(position).getName();
        String pubLoc = mPubs.get(position).getLocation();
        String sun = mPubs.get(position).getSunKey();
        String mon = mPubs.get(position).getMonKey();
        String tue = mPubs.get(position).getTueKey();
        String wed = mPubs.get(position).getWedKey();
        String thu = mPubs.get(position).getThuKey();
        String fri = mPubs.get(position).getFriKey();
        String sat = mPubs.get(position).getSatKey();

        // Store the info in shared preferences
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(RESTAURANT_NAME, pubName);
        edit.putString(RESTAURANT_LOCATION, pubLoc);
        edit.putString(SUNDAY_KEY, sun);
        edit.putString(MONDAY_KEY, mon);
        edit.putString(TUESDAY_KEY, tue);
        edit.putString(WEDNESDAY_KEY, wed);
        edit.putString(THURSDAY_KEY, thu);
        edit.putString(FRIDAY_KEY, fri);
        edit.putString(SATURDAY_KEY, sat);

        edit.commit();

        determineCurKey(position);

        mContext.startActivity(intent); // start the details activity
    }

    // Get what the current day is
    private void determineCurKey(int position) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();


        Calendar calendar = Calendar.getInstance();

        int curDay = calendar.get(Calendar.DAY_OF_WEEK); // if there is a stored value for current day, use that, if not, use the day of the week

        String curKey = "";

        switch (curDay) {
            case Calendar.SUNDAY:
                // Current day is Sunday
                curKey = mPubs.get(position).getSunKey();
                break;
            case Calendar.MONDAY:
                // Current day is Monday
                curKey = mPubs.get(position).getMonKey();
                break;
            case Calendar.TUESDAY:
                // Current day is Tuesday
                curKey = mPubs.get(position).getTueKey();
                break;
            case Calendar.WEDNESDAY:
                // Current day is Wednesday
                curKey = mPubs.get(position).getWedKey();
                break;
            case Calendar.THURSDAY:
                // Current day is Thursday
                curKey = mPubs.get(position).getThuKey();
                break;
            case Calendar.FRIDAY:
                // Current day is Friday
                curKey = mPubs.get(position).getFriKey();
                break;
            case Calendar.SATURDAY:
                // Current day is Saturday
                curKey = mPubs.get(position).getSatKey();
                break;
        }
        edit.putInt(CURRENT_DAY, curDay);
        edit.putString(CURRENT_KEY, curKey);

        edit.commit();
    }

}