package com.example.remed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SeeMedList extends AppCompatActivity {
    private String filename = AddMedicine.filename;
    private TextView showMed;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_med_list);
        showMed = findViewById(R.id.textView12);
    }

    public void seeList(View v) {

        try {
            FileInputStream fin = openFileInput(filename);
            int c;
            StringBuilder temp= new StringBuilder();
            while ((c = fin.read()) != -1) {
                temp.append((char) c);
            }
            fin.close();
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, temp, duration).show();
            System.out.println(temp);

            showMed.setText("");
            showMed.append(temp);


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void add(){
        try {
            FileOutputStream fOut;
            fOut = openFileOutput(filename,Context.MODE_PRIVATE);
            String str = "test data";
            fOut.write(str.getBytes());
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
