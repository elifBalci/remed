package com.example.remed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeeMedList extends AppCompatActivity {

    private Login login = new Login();
    private Register register = new Register();

    private RecyclerView mRecyclerView;
    private List<Object> viewItems = new ArrayList<>();

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_med_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

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

        try
        {

            String list_name;
            if(login.isLogin()){

                list_name = login.listName();

            }

            else{

                list_name = register.listName1();

            }
            list_name = "/data/data/com.example.remed/files/" + list_name + ".txt" ;

            File file=new File(list_name);    //creates a new file instance
            FileReader fr=new FileReader(file);   //reads the file
            BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream
            StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters
            String mediName=null;
            while((mediName=br.readLine())!=null)
            {
                Medicines medicines = new Medicines(mediName);
                viewItems.add(medicines);
            }

            fr.close();    //closes the stream and release the resources
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }
}
