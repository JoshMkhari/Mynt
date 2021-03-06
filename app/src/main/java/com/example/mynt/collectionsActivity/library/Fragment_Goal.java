package com.example.mynt.collectionsActivity.library;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynt.R;
import com.example.mynt.collectionsActivity.Activity_Collections;
import com.example.mynt.collectionsActivity.models.Model_Collections;
import com.example.mynt.collectionsActivity.models.Model_Goals;
import com.example.mynt.collectionsActivity.models.Model_User;
import com.example.mynt.collectionsActivity.models.Model_User_Data;
import com.example.mynt.dataAccessLayer.Database_Lite;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class Fragment_Goal extends Fragment {

    private EditText target_Edittext;
    private String oldText;
    private Model_Goals model_goals;//(Section, 2021)
    private ImageButton setGoal_imageButton, back, subtract,add;
    private TextView target_textView;
    private TextView percentOfGoal_textView;
    private View goals;
    private Model_User model_user;//(Section, 2021)
    private float progress;
    private String currentText;
    private int currentTarget;
    private int task;
    private Database_Lite localDB;
    private Model_Collections model_collections;//(Section, 2021)
    private ArrayList<Model_Collections> allUserCollections;//(Section, 2021)
    private Intent home;
    private ProgressBar goalProgress_progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        goals = inflater.inflate(R.layout.fragment_goal, container, false);

        TextView collectionName_textView = goals.findViewById(R.id.GoalPageCollectionName_TextView);
        TextView numCoinsInCollection_textView = goals.findViewById(R.id.GoalsPageCoinsTotal_TextView);
        percentOfGoal_textView = goals.findViewById(R.id.GoalPagePercentage_TextView);
        target_textView = goals.findViewById(R.id.GoalsPageTarget_TextView);
        goalProgress_progressBar = goals.findViewById(R.id.GoalPageProgressBar);
        setGoal_imageButton = goals.findViewById(R.id.imageview_blockTitle_goal);
        back = goals.findViewById(R.id.GoalsPage_back);
        target_Edittext = goals.findViewById(R.id.GoalsPage_GoalValue);
        add = goals.findViewById(R.id.GoalsPage_add);
        subtract = goals.findViewById(R.id.GoalsPage_subtract);

        //GoalsPage_add
        //GoalsPage_subtract




        assert getArguments() != null;
        model_goals = new Model_Goals(getArguments().getString("Collection Name"),getArguments().getInt("Coins"),getArguments().getInt("Goal"));//(Section, 2021)
        task = getArguments().getInt("Task");
        model_user = Model_User_Data.currentUser;
        model_user.setUserID(getArguments().getInt("User"));


        String userID = model_user.getUserID() + " this";

        collectionName_textView.setText(model_goals.getCollectionName());
        numCoinsInCollection_textView.setText(String.valueOf(model_goals.getNumCoins()));
        target_Edittext.setText(String.valueOf(model_goals.getTarget()));
        String targetText = "Target: " + model_goals.getTarget();
        target_textView.setText(targetText);



        goalProgress_progressBar.setProgress(Math.round(progress));
        //model_goals = new Model_Goals(collectionName,numCoins,Integer.parseInt(target_Edittext.getText().toString()));
        //1000000 GoalsPage_add GoalsPage_subtract GoalsPage_GoalValue


        ReturnToHomePage();
        CreateGoal();
        CalculateGoalProgress(0);

        return goals;
    }

    private void ReturnToHomePage(){

        back.setOnClickListener(v -> Navigation.findNavController(goals).navigateUp());//(JHowzer,2018)
    }
    private void CreateGoal(){

        setGoal_imageButton.setOnClickListener(v -> {
            if(Integer.parseInt(target_Edittext.getText().toString())!=0)
            {
                localDB = new Database_Lite(getContext());//(freecodecamp,2020)
                model_collections = new Model_Collections(model_goals.getCollectionName(),Integer.parseInt(target_Edittext.getText().toString()));
                int collectionID = localDB.getAllCollections().size()+1;
                model_collections.setCollectionID(collectionID);
                Log.d("thisIsTask", "CreateGoal: " + task);
                //Add collection to database for user
                if(task==1 || task ==0)// Creating new Collection and assigning it to a coin
                {
                    Toast.makeText(getContext(), "Running new", Toast.LENGTH_SHORT).show();
                    //Get latest collection ID
                    localDB.addCollection(model_collections);
                    if(task==1)
                        localDB.addCollectionCoin(collectionID, Model_User_Data.model_coin.getCoinID(),localDB.getAllCoins().size());
                }
                else
                {
                    assert getArguments() != null;
                    model_collections.setCollectionID(getArguments().getInt("CollectionID"));
                    localDB.updateCollection(model_collections);//(geeksforgeeks, 2021)

                }

                Calendar cal = Calendar.getInstance();
                String lastSync = cal.getTime().toString();
                Model_User_Data.currentUser.setLastSync(lastSync);
                if(!Model_User_Data.currentUser.getEmail().equals("DefaultUser"))
                {
                    Model_User_Data.mergeData(getContext());
                    localDB.updateUserLastSync(Model_User_Data.currentUser);
                }

                home = new Intent(getContext(), Activity_Collections.class);
                home.putExtra("View","library");
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);
                //Additional User Feedback
                Toast.makeText(getContext(), "Goal for " +  model_goals.getCollectionName() + " has been created successfully." , Toast.LENGTH_SHORT).show();//(Alexander, 2016).

            }else
            {
                //Additional User Feedback
                Toast.makeText(getContext(), "Goal for " +  model_goals.getCollectionName() + " has not been created successfully." , Toast.LENGTH_SHORT).show();//(Alexander, 2016).
                Toast.makeText(getContext(), "Target number of coins cannot be 0.", Toast.LENGTH_SHORT).show();//(Alexander, 2016).
            }

        });



        target_Edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldText = target_Edittext.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentText = target_Edittext.getText().toString();
                if(currentText.length()==5 || currentText.length()<1)
                {
                    target_Edittext.setText(oldText);
                    Toast.makeText(getContext(), "Goal cannot be greater than 9999 or empty", Toast.LENGTH_SHORT).show();//(Alexander, 2016).

                }
                else
                {
                    String targetText = "Target: " + currentText;
                    target_textView.setText(targetText);
                    CalculateGoalProgress(Integer.parseInt(currentText));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        target_Edittext.setOnFocusChangeListener((v, hasFocus) -> {
            currentTarget = Integer.parseInt(target_Edittext.getText().toString());
            if (currentTarget==0)
            {
                target_Edittext.setText("1");
            }
        });

        subtract.setOnClickListener(v -> {
            currentTarget = Integer.parseInt(target_Edittext.getText().toString());
            if(currentTarget <0 || currentTarget ==0){
                currentTarget = 1;
                target_Edittext.setText(String.valueOf(currentTarget));

                //Additional User Feedback
                Toast.makeText(getContext(), "ERROR: Your target number of coins cannot 0 or be less than 0.", Toast.LENGTH_SHORT).show();//(Alexander, 2016).
            }else{

                currentTarget = Integer.parseInt(target_Edittext.getText().toString());
                currentTarget--;
                if(currentTarget==0)
                {
                    target_Edittext.setText(String.valueOf(1));
                }else
                target_Edittext.setText(String.valueOf(currentTarget));

            }
        });
        add.setOnClickListener(v -> {
            currentTarget = Integer.parseInt(target_Edittext.getText().toString());
            currentTarget++;
            target_Edittext.setText(String.valueOf(currentTarget));
        });
    }

    private void CalculateGoalProgress(int trueTarget){

        float target;
        if(trueTarget==0)
        {
            target = (float) model_goals.getTarget();

        }
         else
        {
            target = (float) trueTarget;
        }
        float coins = (float) model_goals.getNumCoins();
        progress =  coins / target *100;
        String percentage = String.valueOf(Math.round(progress)) + '%';
        percentOfGoal_textView.setText(percentage);
        goalProgress_progressBar.setProgress(Math.round(progress));


    }
}