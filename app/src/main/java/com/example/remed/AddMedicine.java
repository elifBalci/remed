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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


public class AddMedicine extends AppCompatActivity  {
    private File file = new File("/data/data/com.example.remed/files/listfile.txt");
    private FileOutputStream fileout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_medicine_page);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            for (int r = 0; r < row; r++) {
                Cell z = s.getCell(0, r);
                if (z.getContents().contains(medName.concat(" "))) {
                    if(saveMed(medName))
                        break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    public void addByBarcode (View v) {
        try{
            AssetManager am = getAssets();
            InputStream is = am.open("test_list.xls");
            Workbook wb = Workbook.getWorkbook(is);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();
            EditText  etbar = (EditText) findViewById(R.id.etForBarAdd);
            String barcode = etbar.getText().toString();

            boolean found = false;

            for(int r = 0; r < row; r++){
                Cell z = s.getCell(1, r);
                if(z.getContents().equals(barcode)){
                    found = true;
                    String name = s.getCell(0, r).getContents();
                    name = name.substring(0, name.indexOf(" "));

                    saveMed(name);
                    break;
                }
            }
        } catch (Exception ignored){
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
                            //Toast.makeText(getApplicationContext(), "Medicine found!", Toast.LENGTH_LONG).show();
                            String name = s.getCell(0, r).getContents();
                            name = name.substring(0, name.indexOf(" "));

                            saveMed(name);
                            break;
                        }
                    }
                } catch (Exception ignored){
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

    private boolean saveMed(String medName){
        if (medAlreadyExists(medName)){
            Toast.makeText(getBaseContext(), "Medicine already exists!", Toast.LENGTH_SHORT).show();
            return true;
        }
        try {
            fileout=new FileOutputStream(file,true);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.append(medName);
            outputWriter.append('\n');
            outputWriter.close();

            Toast.makeText(getBaseContext(), "Medicine added successfully!", Toast.LENGTH_SHORT).show();
            fileout.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    private boolean medAlreadyExists(String medNametoAdd){
        FileReader fr= null;   //reads the file
        try {
            fr = new FileReader(file);

            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            String medName = null;
            while((medName = br.readLine())!= null) {
                if(medNametoAdd.equals(medName))
                    return true;
            }
            fr.close();    //closes the stream and release the resources
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
