package com.example.carpool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.carpool.LocalDB.ProfileDataEntity;
import com.example.carpool.LocalDB.ProfileViewModel;

import kotlin.io.ConsoleKt;

public class MyProfile extends AppCompatActivity {
    String thisUserEmail;
    Button editBtn;
    Intent profileEditIntent;
    TextView nameText;
    TextView phoneNumberText;
    private ProfileViewModel mProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        editBtn = (Button)findViewById(R.id.editButton);
        profileEditIntent = getIntent();
        String email = profileEditIntent.getStringExtra("email");

        profileEditIntent = new Intent(this, MyProfileEdit.class);

        nameText = findViewById(R.id.savedUserName);
        phoneNumberText = findViewById(R.id.savedUserPhoneNumber);

        mProfileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        mProfileViewModel.getAllUsersProfileData().observe(this, users -> {
            // Update the cached copy of the words in the adapter.
            ProfileDataEntity myUser;
            for(ProfileDataEntity p : users){
                String pEmail = p.getEmail();
                int size = users.size();
                Log.d("size", ""+size);
                Log.d("MyMail", email);
                if(pEmail.equals(email)){

                    myUser = p;
                    nameText.setText(myUser.getName());
                    phoneNumberText.setText(myUser.getPhoneNumber());
                }
            }

        });

        //Take data from local DB and present then in the text fields
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileEditIntent.putExtra("email",email);
                startActivity(profileEditIntent);
                finish();
            }
        });
    }
}

