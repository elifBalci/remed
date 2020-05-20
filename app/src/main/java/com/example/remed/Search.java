package com.example.remed;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_medicine_page);
    }

    public void searchByName (View v) {
        try {
            AssetManager am = getAssets();
            InputStream is = am.open("test_list.xls");
            Workbook wb = Workbook.getWorkbook(is);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();
            EditText et = (EditText) findViewById(R.id.editText2);
            String medName = et.getText().toString().toUpperCase();

            WebView ourBrow = (WebView) findViewById(R.id.wv);
            ourBrow.getSettings().setJavaScriptEnabled(true);
            ourBrow.getSettings().setLoadWithOverviewMode(true);
            ourBrow.getSettings().setUseWideViewPort(true);

            boolean found = false;

            for (int r = 0; r < row; r++) {
                Cell z = s.getCell(0, r);
                if (z.getContents().contains(medName.concat(" "))) {
                    found = true;
                    Toast.makeText(getApplicationContext(), "Medicine found!", Toast.LENGTH_LONG).show();
                    ourBrow.loadUrl("http://www.ilacweb.com/" + medName);
                }
            }

            if (!found) {
                Toast.makeText(getApplicationContext(), "Medicine not found!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
        }
    }

        public void searchByBarcode (View v) {
            try{
                AssetManager am = getAssets();
                InputStream is = am.open("test_list.xls");
                Workbook wb = Workbook.getWorkbook(is);
                Sheet s = wb.getSheet(0);
                int row = s.getRows();
                EditText  et = (EditText) findViewById(R.id.editText);
                String barcode = et.getText().toString();

                WebView ourBrow=(WebView) findViewById(R.id.wv);
                ourBrow.getSettings().setJavaScriptEnabled(true);
                ourBrow.getSettings().setLoadWithOverviewMode(true);
                ourBrow.getSettings().setUseWideViewPort(true);

                boolean found = false;

                for(int r = 0; r < row; r++){
                    Cell z = s.getCell(1, r);
                    if(z.getContents().equals(barcode)){
                        found = true;
                        Toast.makeText(getApplicationContext(), "Medicine found!", Toast.LENGTH_LONG).show();
                        String name = s.getCell(0, r).getContents();
                        name = name.substring(0, name.indexOf(" "));
                        ourBrow.loadUrl("http://www.ilacweb.com/" + name);
                    }
                }

                if(!found){
                    Toast.makeText(getApplicationContext(), "Medicine not found!", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e){
            }
    }

    public void searchWithScan(View v){}

}