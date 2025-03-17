package com.example.drivertani;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.drivertani.LocalDB.ProfileDataEntity;
import com.example.drivertani.LocalDB.ProfileViewModel;

public class MyProfileEdit extends AppCompatActivity {
    Button SubmitEditBtn;
    Intent profileIntent;
    EditText nameEdit;
    EditText phoneNumberEdit;

    private ProfileViewModel mProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_edit);

        SubmitEditBtn = (Button)findViewById(R.id.submitEdit);
        nameEdit = findViewById(R.id.userNameInput);
        phoneNumberEdit = findViewById(R.id.phoneNumberInput);

        profileIntent= getIntent();
        String email = profileIntent.getStringExtra("email");
        //profileIntent = new Intent(this, MyProfile.class);
        SubmitEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take data from edit text fields and Update data in my local DB
                String newName = nameEdit.getText().toString();
                String newPhoneNumber = phoneNumberEdit.getText().toString();
                if(TextUtils.isEmpty(newName)){
                    Toast.makeText(MyProfileEdit.this,"Enter your name",Toast.LENGTH_SHORT).show();

                    return;
                }

                if(TextUtils.isEmpty(newPhoneNumber)){
                    Toast.makeText(MyProfileEdit.this,"Enter your phone number",Toast.LENGTH_SHORT).show();
                    return;
                }
                mProfileViewModel = new ViewModelProvider(MyProfileEdit.this).get(ProfileViewModel.class);
                mProfileViewModel.getAllUsersProfileData().observe(MyProfileEdit.this, users -> {
                    // Update the cached copy of the words in the adapter.
                    for(ProfileDataEntity p : users){
                        String pEmail = p.getEmail();
                        int size = users.size();
                        Log.d("size", ""+size);
                        Log.d("MyMail", email);
                        if(pEmail.equals(email)){
                            mProfileViewModel.deleteProfile(p);
                            ProfileDataEntity newUser = new ProfileDataEntity(newName, newPhoneNumber, email);
                            mProfileViewModel.insert(newUser);
                        }
                    }

                });
                //startActivity(profileIntent);
                finish();
            }
        });
    }
}