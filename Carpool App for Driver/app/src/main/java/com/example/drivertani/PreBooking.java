package com.example.drivertani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PreBooking extends AppCompatActivity {

    Button btn;
    EditText editTextPhoneNumber, editTextName;
    Intent toBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_booking);
        btn = findViewById(R.id.next);
        editTextName = findViewById(R.id.BookingName);
        editTextPhoneNumber = findViewById(R.id.BookingPhone);

        toBooking = getIntent();
        String email = toBooking.getStringExtra("email");
        toBooking = new Intent(this, BookingActivity.class);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String phone = editTextPhoneNumber.getText().toString();
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(PreBooking.this,"Enter your name",Toast.LENGTH_SHORT).show();

                    return;
                }
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(PreBooking.this,"Enter your phone number",Toast.LENGTH_SHORT).show();
                    return;
                }
                toBooking.putExtra("email",email);
                toBooking.putExtra("name",name);
                toBooking.putExtra("phone",phone);
                startActivity(toBooking);
                finish();
            }
        });
    }
}