package com.example.appit.fragment.ble;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appit.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeScanActivity extends AppCompatActivity implements View.OnClickListener {
    Button scanBtn;
    TextView tvQrText, messageFormat, tvSerial, tvMac, tvIpei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scan);

        // referencing and initializing
        // the button and textviews
        scanBtn = findViewById(R.id.scanBtn);
        tvQrText = findViewById(R.id.textContent);
        messageFormat = findViewById(R.id.textFormat);
        tvSerial = findViewById(R.id.tv_serialNo);
        tvMac = findViewById(R.id.tv_mac);
        tvIpei = findViewById(R.id.tv_ipei);

        // adding listener to the button
        scanBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // we need to create the object
        // of IntentIntegrator class
        // which is the class of QR library
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {

                tvQrText.setVisibility(View.VISIBLE);
                tvSerial.setVisibility(View.VISIBLE);
                tvMac.setVisibility(View.VISIBLE);
                tvIpei.setVisibility(View.VISIBLE);
                // if the intentResult is not null we'll set
                // the content and format of scan message
                //"1|1|MYMODELNO|2|MYSERIAL|3|MYIPEI|4|BT MAC|9"
                String qrString = intentResult.getContents();
                String[] qrArr = qrString.split("\\|");
                //4,6,8

                tvQrText.setText(qrString);
//                messageFormat.setText(intentResult.getFormatName());
                tvSerial.setText("Serial No : "+qrArr[4]);
                tvIpei.setText("IPEI : "+qrArr[6]);
                tvMac.setText("MAC : "+qrArr[8]);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}