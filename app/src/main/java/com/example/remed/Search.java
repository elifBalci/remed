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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class Search extends AppCompatActivity {

    private TextView text_qr_code_sonuc,txt_sonuc,txt_code_kind,txt_qr_code_kind_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_medicine_page);


        final Activity activity = this;
        txt_sonuc = (TextView) findViewById(R.id.txt_sonuc);
        text_qr_code_sonuc = (TextView) findViewById(R.id.qr_code_sonucu);
        txt_code_kind = (TextView) findViewById(R.id.txt_code_kind);
        txt_qr_code_kind_result = (TextView) findViewById(R.id.txt_qr_code_kind_result);

        Button scan_button = (Button) findViewById(R.id.button8);
        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bu activity içinde çalıştırıyoruz.
                IntentIntegrator integrator = new IntentIntegrator(activity);
                //Kütüphanede bir kaç kod tipi var biz hepsini tarayacak şekilde çalıştırdık.
                //integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                //şeklindede sadece qr code taratabilirsiniz.
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                //Kamera açıldığında aşağıda yazı gösterecek
                integrator.setPrompt("Scan");
                //telefonun kendi kamerasını kullandırıcaz
                integrator.setCameraId(0);
                //okuduğunda 'beep' sesi çıkarır
                integrator.setBeepEnabled(true);
                //okunan barkodun image dosyasını kaydediyor
                integrator.setBarcodeImageEnabled(false);
                //scan başlatılıyor
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Kütüphane okuduktan sonra bu metodla bize result döndürüyor.
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                text_qr_code_sonuc.setText("Kod Sonucu:");
                txt_sonuc.setText("Qr Code Bulunamadı.");
                txt_code_kind.setText("Kod Türü:");
                txt_qr_code_kind_result.setText("Bulunamadı.");
            } else {
                Log.d("MainActivity", "Scanned");
                text_qr_code_sonuc.setText("Kod Sonucu:");
                txt_sonuc.setText(result.getContents());
                txt_code_kind.setText("Kod Türü:");
                txt_qr_code_kind_result.setText(result.getFormatName());
            }
        }
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

}