package com.example.remed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class AddMedicine extends AppCompatActivity  {
    protected static String filename = "medicineList";
    private FileOutputStream fOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_medicine_page);
    }

    public void addByName (View v) {
        try {
            AssetManager am = getAssets();
            InputStream is = am.open("test_list.xls");
            Workbook wb = Workbook.getWorkbook(is);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();
            EditText et = (EditText) findViewById(R.id.etForNameAdd);
            String medName = et.getText().toString().toUpperCase();

            boolean found = false;

            for (int r = 0; r < row; r++) {
                Cell z = s.getCell(0, r);
                if (z.getContents().contains(medName.concat(" "))) {
                    found = true;
                    Toast.makeText(getApplicationContext(), "Medicine found!", Toast.LENGTH_LONG).show();
                    saveMedicine(medName);
                    //eklenecek
                }
            }

            if (!found) {
                Toast.makeText(getApplicationContext(), "Medicine not found!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addByBarcode (View v) {
        try{
            AssetManager am = getAssets();
            InputStream is = am.open("test_list.xls");
            Workbook wb = Workbook.getWorkbook(is);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();
            EditText  et = (EditText) findViewById(R.id.etForBarAdd);
            String barcode = et.getText().toString();

            boolean found = false;

            for(int r = 0; r < row; r++){
                Cell z = s.getCell(1, r);
                if(z.getContents().equals(barcode)){
                    found = true;
                    Toast.makeText(getApplicationContext(), "Medicine found!", Toast.LENGTH_LONG).show();
                    String name = s.getCell(0, r).getContents();
                    name = name.substring(0, name.indexOf(" "));
                    //ourBrow.loadUrl("http://www.ilacweb.com/" + name);
                }
            }

            if(!found){
                Toast.makeText(getApplicationContext(), "Medicine not found!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addByScan(View v){
        scanCode();
    }

    private void scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    private void saveMedicine(String s){
        try {
            fOut = openFileOutput(filename, Context.MODE_PRIVATE);
            fOut.write(s.getBytes());
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Scanning Result: ");
                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanCode();
                    }
                }).setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();

                String code_value = result.getContents();

                if(code_value.contains("0108699")){
                    code_value = code_value.substring(4, 17);
                }

                try{
                    AssetManager am = getAssets();
                    InputStream is = am.open("test_list.xls");
                    Workbook wb = Workbook.getWorkbook(is);
                    Sheet s = wb.getSheet(0);
                    int row = s.getRows();

                    boolean found = false;

                    for(int r = 0; r < row; r++){
                        Cell z = s.getCell(1, r);
                        if(z.getContents().equals(code_value)){
                            found = true;
                            Toast.makeText(getApplicationContext(), "Medicine found!", Toast.LENGTH_LONG).show();
                            String name = s.getCell(0, r).getContents();
                            name = name.substring(0, name.indexOf(" "));
                            //saveMedicine(name);
                            //eklenecek
                        }
                    }

                    if(!found){
                        Toast.makeText(getApplicationContext(), "Medicine not found!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(this, "No result!", Toast.LENGTH_LONG).show();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}