package com.example.mynt.collectionsActivity.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynt.R;
import com.example.mynt.collectionsActivity.Dialog_Bottom_Sheet;
import com.example.mynt.collectionsActivity.interfaces.Interface_RecyclerView;
import com.example.mynt.collectionsActivity.models.Model_Coin;
import com.example.mynt.collectionsActivity.models.Model_Date;
import com.example.mynt.collectionsActivity.models.Model_User_Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Adapter_Coins extends RecyclerView.Adapter<Adapter_Coins.CoinViewHolder>{ //(Professor Sluiter, 2020).
    private final Interface_RecyclerView interfaceRecyclerView;//(Practical Coding, 2021)

    final ArrayList<Model_Coin> coinsList;
    final Context context;
    static String dateAcquired, dateValue;
    static String current = "No";
    final FragmentManager manager;

    public Adapter_Coins(ArrayList<Model_Coin> coinsList, Context context, Interface_RecyclerView interfaceRecyclerView,FragmentManager manager ) {
        this.coinsList = coinsList;
        this.context = context;
        current = "No";
        this.interfaceRecyclerView = interfaceRecyclerView;
        this.manager = manager;
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//(Professor Sluiter, 2020).
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_coins,parent,false);
        return new CoinViewHolder(view);
    }

    public String returnDay(String date)
    {
        Calendar calToday = Calendar.getInstance();
        Calendar calDay = Calendar.getInstance();


        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            calToday.set(Calendar.HOUR_OF_DAY,0);
            calDay.setTime(Objects.requireNonNull(sdf.parse(date)));

            if(calDay.get(Calendar.YEAR) == calToday.get(Calendar.YEAR))
            {
                if(calDay.get(Calendar.DAY_OF_YEAR) == calToday.get(Calendar.DAY_OF_YEAR))
                {
                    return "TODAY";
                }
                int yesterday = calToday.get(Calendar.DAY_OF_YEAR)-1;
                if(calDay.get(Calendar.DAY_OF_YEAR) == yesterday )
                {
                    return "YESTERDAY";
                }
                int thisWeek = calToday.get(Calendar.WEEK_OF_YEAR);
                if(calDay.get(Calendar.WEEK_OF_YEAR) == thisWeek)
                {
                    return "THIS WEEK";
                }
                 if(calDay.get(Calendar.WEEK_OF_YEAR) == (thisWeek-1))
                {
                    return "LAST WEEK";
                }
                int thisMonth = calToday.get(Calendar.MONTH);
                if(calDay.get(Calendar.MONTH) == thisMonth)
                {
                    return "THIS MONTH";
                }else
                {
                    Model_Date model_date = new Model_Date();//(Shabbir Dhangot,2016)
                    return model_date.getMonthFormat(calDay.get(Calendar.MONTH),true);//(Code With Cal, 2020)
                }
            }
            return String.valueOf(calDay.get(Calendar.YEAR));
        } catch (ParseException e) {
            e.printStackTrace();
            return "Error";
        }
    }
    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder holder, int position) {//(Professor Sluiter, 2020).
        holder.name.setText(coinsList.get(position).getValue());

        //Need to get actual year here

        holder.year.setText(String.valueOf(coinsList.get(position).getYear()));
        //holder.coinImage.setBackgroundResource(coinsList.get(position).getImageId());
        try{
            Bitmap bmp = BitmapFactory.decodeByteArray(coinsList.get(position).getImageId(), 0, coinsList.get(position).getImageId().length);
            holder.coinImage.setImageBitmap(bmp);
            //holder.coinImage.setImageDrawable(Drawable.createFromPath(file.toString()));
        }
        catch(Exception ignored){
        }
        Model_Date model_date = new Model_Date();//(Shabbir Dhangot,2016)
        String convertedDate = model_date.convertDateString(coinsList.get(position).getDateAcquired(),true);//(Code With Cal, 2020)
        holder.date.setText(convertedDate);
        holder.country.setText("South Africa");

        dateValue = coinsList.get(position).getDateAcquired();

        if(current.equals("No"))
        {
            dateAcquired = returnDay(dateValue);
            current = dateAcquired;
            holder.acquired.setText(dateAcquired);
            holder.coinSeparator.setVisibility(View.GONE);
        }else
        {
            dateAcquired = returnDay(coinsList.get(position).getDateAcquired());
            if(current.equals(dateAcquired))//Same returnDay as previous
            {
                holder.acquired.setVisibility(View.GONE);

                holder.daySeparator.setVisibility(View.GONE);

            }else
            {
                current = dateAcquired;
                holder.acquired.setText(dateAcquired);
                holder.coinSeparator.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return coinsList.size();
    }//(Professor Sluiter, 2020).

    public class CoinViewHolder extends RecyclerView.ViewHolder{
        final ImageView coinImage;
        final TextView year;
        final TextView name;
        final TextView date;
        final TextView country;
        final TextView acquired;
        final ImageView daySeparator;
        final ImageView coinSeparator;
        final ImageButton moreOptions;

        public CoinViewHolder(@NonNull View itemView) {
            super(itemView);
            coinImage = itemView.findViewById(R.id.imageview_current_coin);
            year = itemView.findViewById(R.id.textview_coin_year);
            name = itemView.findViewById(R.id.textview_coin_name);
            country = itemView.findViewById(R.id.textview_coin_country);
            date = itemView.findViewById(R.id.textview_coin_acquired_date);
            acquired = itemView.findViewById(R.id.textview_coinDate);
            daySeparator = itemView.findViewById(R.id.coin_date_separator);
            coinSeparator = itemView.findViewById(R.id.coin_separator);
            moreOptions = itemView.findViewById(R.id.meatball_coinList);

            moreOptions.setOnClickListener(v -> {
                int pos = getAbsoluteAdapterPosition();
                //coinsList.get(pos).getValue();
                Log.d("meatClicked", "onClick: "+coinsList.get(pos).getValue());
                Log.d("meatClicked", "onClick: "+coinsList.get(pos).getYear());
                Model_User_Data.array_list_bottomSheet = new ArrayList<>();
                for (int i = 0; i<2;i++)
                    Model_User_Data.array_list_bottomSheet.add("1");

                Model_User_Data.mode = 3;
                Model_User_Data.model_coin = coinsList.get(pos);
                Dialog_Bottom_Sheet bottom_sheet = new Dialog_Bottom_Sheet();
                bottom_sheet.show(manager,"mySheet");
            });

            itemView.setOnClickListener(v -> {//(Practical Coding, 2021).
                if(interfaceRecyclerView != null)
                {
                    int pos = getAbsoluteAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION){
                        interfaceRecyclerView.onItemClick(pos,coinImage);//(Practical Coding, 2021)
                    }
                }
            });
        }
    }


}
