package com.example.appit.fragment.multithtread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appit.R;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PriceUpdateActivity extends AppCompatActivity {


    private static final String TAG = "PriceUpdateActivity";
    TextView tvBtc;
    TextView tvBtcPrice;

    TextView tvEth;
    TextView tvEthPrice;

    TextView tvLtc;
    TextView tvLtcPrice;

    TextView tvBch;
    TextView tvBchPrice;

    TextView tvXrp;
    TextView tvXrpPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricr_update);

        LinearLayout bitcoinList = findViewById(R.id.bitcoin_list);


        LinearLayout btc = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.bitcoin_item, null);
        tvBtc = btc.findViewById(R.id.tv_coin_name);
        tvBtc.setText("BTC");
        tvBtcPrice = btc.findViewById(R.id.tv_coin_price);
        tvBtcPrice.setText("0");
        bitcoinList.addView(btc);

        LinearLayout eth = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.bitcoin_item, null);
        tvEth = eth.findViewById(R.id.tv_coin_name);
        tvEth.setText("ETH");
        tvEthPrice = eth.findViewById(R.id.tv_coin_price);
        tvBtcPrice.setText("0");
        bitcoinList.addView(eth);

        LinearLayout ltc = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.bitcoin_item, null);
        tvLtc = ltc.findViewById(R.id.tv_coin_name);
        tvLtc.setText("LTC");
        tvLtcPrice = ltc.findViewById(R.id.tv_coin_price);
        tvLtcPrice.setText("0");
        bitcoinList.addView(ltc);


        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        Lock writeLock = lock.writeLock();
        Lock readLock = lock.readLock();

        PricesContainer pricesContainer = new PricesContainer();
        PriceUpdaterThread priceUpdaterThread = new PriceUpdaterThread(pricesContainer);


        Handler handler = new Handler(getMainLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {

//                Log.d(TAG, "UI run: tryLock..."+pricesContainer.getLockObject().tryLock());
                if (pricesContainer.getLockObject().tryLock()) {

//                pricesContainer.getLockObject().lock();

                   try {
                       tvBtcPrice.setText(pricesContainer.getBitcoinPrice()+"");
                       tvEthPrice.setText(pricesContainer.getEtherPrice()+"");
                       tvLtcPrice.setText(pricesContainer.getLitecoinPrice()+"");
                   } finally {
                       Log.d(TAG, "UI run: release lock ");
                       pricesContainer.getLockObject().unlock();
                   }

                }

                handler.postDelayed(this, 3000);
            }
        });

        priceUpdaterThread.start();
    }


}