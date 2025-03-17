package com.example.carpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.carpool.LocalDB.ProfileDataEntity;
import com.example.carpool.LocalDB.ProfileViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class signupActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button btn;
    ProgressBar progressBar;
    EditText editTextEmail, editTextPassword, editTextPhoneNumber, editTextName;
    Intent intent;

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent toDashboardIntent = new Intent(this, LoginActivity.class);
//            startActivity(toDashboardIntent);
//            finish();
//        }
//    }
    private ProfileViewModel mProfileViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        btn =findViewById(R.id.Submit);
        progressBar = findViewById(R.id.progressBar);
        editTextName = findViewById(R.id.editTextTextName);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        editTextPhoneNumber = findViewById(R.id.editTextPhone);
        intent = new Intent(this, LoginActivity.class);
        mProfileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name, email, password, phoneNumber;
                name = String.valueOf(editTextName.getText());
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                phoneNumber = String.valueOf(editTextPhoneNumber.getText());

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(signupActivity.this,"Enter your name",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if(TextUtils.isEmpty(email) ){
                    Toast.makeText(signupActivity.this,"Enter email",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if(!email.endsWith("@eng.asu.edu.eg")){
                    Toast.makeText(signupActivity.this,"Enter your official email",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(signupActivity.this,"Enter password",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if(TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(signupActivity.this,"Enter your phone number",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                String riderEmail = "rider_"+email;
                mAuth.createUserWithEmailAndPassword(riderEmail, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(signupActivity.this,"Account Created",Toast.LENGTH_SHORT);
                                    ProfileDataEntity newUser = new ProfileDataEntity(name,phoneNumber,riderEmail);
                                    mProfileViewModel.insert(newUser);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(signupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });
    }


}