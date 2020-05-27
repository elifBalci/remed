package com.example.remed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeeMedList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<Object> viewItems = new ArrayList<>();

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_med_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecyclerAdapter(this, viewItems);
        mRecyclerView.setAdapter(mAdapter);

        try {
            readDataFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readDataFromFile() throws IOException{

        FileInputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try
        {
            FileInputStream fin=new FileInputStream("/data/data/com.example.remed/files/listfile.txt");
            char ch = (char) fin.read();
            String mediName = null;

            while ( ( ch = (char) fin.read()) != -1) {

                while( ( ch = (char) fin.read()) != ' ' ){

                    mediName += ch;

                }
                Medicines medicines = new Medicines(mediName);
                viewItems.add(medicines);
                mediName = null;
            }

            fin.close();

        } catch(Exception e){
            System.out.println(e);
        }

    }
}
