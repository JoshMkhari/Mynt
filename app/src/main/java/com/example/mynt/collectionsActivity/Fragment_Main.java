package com.example.mynt.collectionsActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mynt.R;
import com.example.mynt.collectionsActivity.Activity_Collections;
import com.example.mynt.collectionsActivity.models.Model_User;
import com.example.mynt.collectionsActivity.models.Model_User_Data;
import com.example.mynt.dataAccessLayer.Database_Lite;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Fragment_Main extends Fragment {

    private ImageButton addButton;
    private Model_User user;//(Section, 2021)
    private View main;
    private Bundle bundle;
    private ImageButton searchButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        main = inflater.inflate(R.layout.fragment_main, container, false);
        user = Model_User_Data.currentUser;
        assert getArguments() != null;
        user.setUserID(getArguments().getInt("User"));
        addButton = main.findViewById(R.id.image_button_add_coin_main);
        TextView userName,userTitle;

        searchButton = main.findViewById(R.id.main_search_button);

        userName = main.findViewById(R.id.text_view_user_main);
        userTitle = main.findViewById(R.id.text_view_user_current);

        searchCoin();

        if(user.getEmail().equals("DefaultUser"))
        {
            userTitle.setVisibility(View.INVISIBLE);
            userName.setVisibility(View.INVISIBLE);
        }else
        {
            userName.setText(user.getEmail());
        }


        AddCoin();

        return main;
    }

    private void AddCoin(){

        addButton.setOnClickListener(v -> {
            bundle = new Bundle();//(valerybodak,2020)
            bundle.putInt("User", user.getUserID());
            Navigation.findNavController(main).navigate(R.id.action_fragment_home_main_to_fragment_Add, bundle);

        });
    }

    private void searchCoin()
    {
        searchButton.setOnClickListener(v -> {
            Navigation.findNavController(main).navigate(R.id.action_fragment_home_main_to_fragment_Search, bundle);
        });
    }

    }



