package com.example.remed;

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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


public class AddMedicine extends AppCompatActivity  {

    private FileOutputStream fileout;
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

                    File myFile = new File("/data/data/com.example.remed/files/listfile.txt");
                    myFile.createNewFile();

                    try {

                        fileout=new FileOutputStream(myFile,true);
                        OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                        outputWriter.append(medName);
                        outputWriter.append('\n');
                        outputWriter.close();

                        Toast.makeText(getBaseContext(), "File saved successfully!", Toast.LENGTH_SHORT).show();
                        fileout.close();

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                }
            }

            if (!found) {
                Toast.makeText(getApplicationContext(), "Medicine not found!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
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

                    File myFile = new File("/data/data/com.example.remed/files/listfile.txt");
                    myFile.createNewFile();

                    try {

                        OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                        outputWriter.append(name);
                        outputWriter.append('\n');
                        outputWriter.close();

                        Toast.makeText(getBaseContext(), "File saved successfully!", Toast.LENGTH_SHORT).show();
                        fileout.close();

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

            if(!found){
                Toast.makeText(getApplicationContext(), "Medicine not found!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e){
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
                            //eklenecek
                        }
                    }

                    if(!found){
                        Toast.makeText(getApplicationContext(), "Medicine not found!", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e){
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
