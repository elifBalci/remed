package com.example.remed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(getBaseContext(), AfterLoginPage.class);
        i.putExtra("username","buse");
        startActivity(i);
        finish();// Eğer sayfa açıldıktan sonra bu sayfaya tekrar Back butonu ile dönülmesini istemiyorsak finish() metodu ile Activty'i sonlandırıyoruz.

    }

}
