package com.example.remed;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_page);
    }

    public void order (View v) {
        try{
            AssetManager am = getAssets();
            InputStream is = am.open("test.xls");
            Workbook wb = Workbook.getWorkBook(is);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();
            int col = s.getColumns();
            String xx = "";

            for(int i = 0; i < row; i++){
                for (int c = 0; c < col; c++){
                    Cell z = s.getCell(c, i);
                    xx = xx + z.getContents();
                }
                xx = xx + "\n";
            }
            display(xx);
        } catch (Exception e){

        }
    }

    public void display (String value){
        TextView x = (TextView) findViewById(R.id.textview);
        x.setText(value);
    }
}