package com.example.appit.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appit.R;

public class BitCoinPriceFragment extends Fragment {

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bitcoin, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.ll_bitcoin_list);
        showData(linearLayout, inflater);
        return view;
    }

    private void showData(LinearLayout parent, LayoutInflater inflater) {


        PriceContainer priceContainer = new PriceContainer();
        PricesUpdater pricesUpdater = new PricesUpdater(priceContainer);

        handler.postDelayed(() -> {

            if (priceContainer.getLock().tryLock()) {

                try {
                    LinearLayout layoutBtc = (LinearLayout) inflater.inflate(R.layout.bitcoin_item, null, false);
                    TextView tvBtc = layoutBtc.findViewById(R.id.tv_coin_name);
                    tvBtc.setText("BTC");

                    TextView tvBtcPrice = layoutBtc.findViewById(R.id.tv_coin_price);
                    tvBtcPrice.setText(priceContainer.getBitcoinPrice() + "");
                    parent.addView(layoutBtc);

                    LinearLayout layoutEth = (LinearLayout) inflater.inflate(R.layout.bitcoin_item, null, false);
                    TextView tvEth = layoutEth.findViewById(R.id.tv_coin_name);
                    tvEth.setText("ETH");

                    TextView tvEthPrice = layoutEth.findViewById(R.id.tv_coin_price);
                    tvEthPrice.setText(priceContainer.getEtherPrice() + "");
                    parent.addView(layoutEth);

                } finally {

                    priceContainer.getLock().unlock();
                }
            }

//            handler.postDelayed();

        }, 3000);

        pricesUpdater.start();

    }
}
