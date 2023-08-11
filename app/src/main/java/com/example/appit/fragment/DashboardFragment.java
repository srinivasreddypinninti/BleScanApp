//package com.example.appit.fragment;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.GridView;
//import android.widget.Toast;
//
//import com.example.appit.MainPrefActivity;
//import com.example.appit.R;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Random;
//
//
//public class DashboardFragment extends Fragment {
//
//    private static final String TAG = "DashboardFragment";
//
//    private GridView grid;
//    CustomGrid adapter;
//    MainPrefActivity mainPrefActivity;
//
//    public static DashboardFragment newInstance(String param1, String param2) {
//        DashboardFragment fragment = new DashboardFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//        }
//        setHasOptionsMenu(true);
//
//
//        InventoryTest inventoryTest = new InventoryTest();
//        try {
//            inventoryTest.startAction();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
////        try {
////            findFact();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//    }
//
//    /*private void findFact() throws InterruptedException {
//        List<Long> inputNumbers = Arrays.asList(100000000L, 3435L, 35435L, 2324L, 4656L, 23L, 5556L);
//
//        List<FactorialThread> threads = new ArrayList<>();
//
//        for (long inputNumber : inputNumbers) {
//            threads.add(new FactorialThread(inputNumber));
//        }
//
//        for (Thread thread: threads) {
//            thread.setDaemon(true);
//            thread.start();
//        }
//
//        for (Thread thread : threads) {
//            thread.join(2000);
//        }
//
//        for (int i=0; i<inputNumbers.size(); i++) {
//            FactorialThread thread = threads.get(i);
//            if (thread.isFinished()) {
//                Log.d(TAG, "factorial of " + inputNumbers.get(i) + " is " + thread.getResult());
//            } else {
//                Log.d(TAG, "calculation for number  " + inputNumbers.get(i) + " is still in progress");
//            }
//        }
//    }*/
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        adapter = new CustomGrid(getContext(), getCards());
//        grid=(GridView)v.findViewById(R.id.grid);
//        grid.setAdapter(adapter);
//        /*grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Toast.makeText(this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
//
//            }
//        });*/
//        return v;
//    }
//
//    private LinkedList<Card> getCards() {
//        return  new LinkedList<Card>() {{
//            add(new Card("Card1", true));
//            add(new Card("Card2", true));
//            add(new Card("Card3", true));
//        }};
//
//    }
//
//    public void updateDisplayCards(ArrayList<Card> cards) {
//        Log.d(TAG, "updateDisplayCards: "+cards.size());
//        adapter.addCardToList(cards);
//    }
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        mainPrefActivity = (MainPrefActivity) context;
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.external_cards, menu);
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Log.d(TAG, "onOptionsItemSelected: " +item.getItemId());
//        if (item.getItemId() == R.id.cards) {
//            mainPrefActivity.launchCardsFragment();
//
//        }
//        return true;
//    }
//}