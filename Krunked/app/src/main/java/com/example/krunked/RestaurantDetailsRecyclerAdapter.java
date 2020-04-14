package com.example.krunked;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RestaurantDetailsRecyclerAdapter extends RecyclerView.Adapter<RestaurantDetailsRecyclerAdapter.ViewHolder>{
    private ArrayList<Drink> mDrinks = new ArrayList<>();
    private Context mContext;

    // constructor
    public RestaurantDetailsRecyclerAdapter(Context mContext, ArrayList<Drink> drinks) {
        mDrinks = drinks;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drink_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    // on bind
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.drinkNameText.setText(mDrinks.get(position).getDrinkName()); // set up the drink names
        holder.drinkPriceText.setText(mDrinks.get(position).getDrinkPrice()); // set up the drink prices
    }

    @Override
    public int getItemCount() {
        return mDrinks.size();
    } // the size of the list is how many drinks there are

    // set up the each item
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView drinkNameText;
        TextView drinkPriceText;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            drinkNameText = itemView.findViewById(R.id.drink_NameTextView);
            drinkPriceText = itemView.findViewById(R.id.drink_PriceTextView);
            parentLayout = itemView.findViewById(R.id.drink_Layout);

        }
    }
}
