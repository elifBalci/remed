package com.example.remed;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class DeleteMedicine extends AppCompatActivity {
    static String file;
    private Login login = new Login();
    private Register register = new Register();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_medicine);
    }

    public void deleteByName(View v) {
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
                    if (deleteMed(medName))
                        break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    public void deleteByBarcode(View v) {
        try {
            AssetManager am = getAssets();
            InputStream is = am.open("test_list.xls");
            Workbook wb = Workbook.getWorkbook(is);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();
            EditText etbar = (EditText) findViewById(R.id.etForBarAdd);
            String barcode = etbar.getText().toString();

            boolean found = false;

            for (int r = 0; r < row; r++) {
                Cell z = s.getCell(1, r);
                if (z.getContents().equals(barcode)) {
                    found = true;
                    String name = s.getCell(0, r).getContents();
                    name = name.substring(0, name.indexOf(" "));

                    deleteMed(name);
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    public void deleteByScan(View v) {
        scanCode();
    }


    private void scanCode() {
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

                if (code_value.contains("0108699")) {
                    code_value = code_value.substring(4, 17);
                }

                try {
                    AssetManager am = getAssets();
                    InputStream is = am.open("test_list.xls");
                    Workbook wb = Workbook.getWorkbook(is);
                    Sheet s = wb.getSheet(0);
                    int row = s.getRows();

                    boolean found = false;

                    for (int r = 0; r < row; r++) {
                        Cell z = s.getCell(1, r);
                        if (z.getContents().equals(code_value)) {
                            found = true;
                            //Toast.makeText(getApplicationContext(), "Medicine found!", Toast.LENGTH_LONG).show();
                            String name = s.getCell(0, r).getContents();
                            name = name.substring(0, name.indexOf(" "));

                            deleteMed(name);
                            break;
                        }
                    }
                } catch (Exception ignored) {
                }
            } else {
                Toast.makeText(this, "No result!", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean deleteMed(String medName) {
        if (medExists(medName)) {
            try {

                File myFile = new File(file);
                ArrayList<String> lines = new ArrayList<>();
                final Scanner reader = new Scanner(new FileInputStream(myFile), "UTF-8");
                while(reader.hasNextLine()) {
                    lines.add(reader.nextLine());
                }
                reader.close();
                //find line
                for (int i=0; i<lines.size(); i++) {

                    if(!lines.get(i).contains("\n")){
                        String sb = lines.get(i) + "\n";
                        lines.set(i,sb);
                    }
                }
                for (int i=0; i<lines.size(); i++) {
                    Log.i("med", lines.get(i));

                    if(!lines.get(i).contains("\n")){
                        String sb = lines.get(i) + "\n";
                        lines.set(i,sb);
                    }
                    if (lines.get(i).contains(medName)) {
                        lines.remove(i);
                        break;
                    }
                }
                final BufferedWriter writer = new BufferedWriter(new FileWriter(myFile, false));
                for(final String line : lines)
                    writer.write(line);
                writer.flush();
                writer.close();


                Toast.makeText(getBaseContext(), "Medicine deleted!", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(getBaseContext(), "Medicine does not exists!", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    private boolean medExists(String medNametoAdd) {
        FileReader fr = null;   //reads the file
        try {

            if (login.isLogin()) {

                file = login.listName();

            } else {

                file = register.listName1();

            }
            file = "/data/data/com.example.remed/files/" + file + ".txt";

            fr = new FileReader(file);

            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            String medName = null;
            while ((medName = br.readLine()) != null) {
                if (medNametoAdd.equals(medName))
                    return true;
            }
            fr.close();    //closes the stream and release the resources
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
