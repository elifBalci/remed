package com.example.remed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AfterLoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_page);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String user_name = bundle.getString("username");
        }
    }
}
