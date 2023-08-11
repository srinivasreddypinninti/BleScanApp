//package com.example.appit.fragment;
//
//import android.app.Activity;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.content.Context;
//import android.os.Bundle;
//import android.preference.CheckBoxPreference;
//import android.preference.Preference;
//import android.preference.PreferenceFragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.appit.MainPrefActivity;
//import com.example.appit.R;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Random;
//
//public class CardsPreFragment extends PreferenceFragment {
//
//    private static final String TAG = "CardsPreFragment";
//
//    private HashMap<String, Boolean> selectedPref = new HashMap<>();
//    CardSelectionListener selectionListener;
//
//    public interface CardSelectionListener {
//         void onCardSelection(ArrayList<Card> cards);
//    }
//
//    CardSelectionListener cardSelectionListener;
//
//    public void setCardSelectionListener(CardSelectionListener cardSelectionListener) {
//        this.cardSelectionListener = cardSelectionListener;
//    }
//
//    public static CardsPreFragment newInstance() {
//        CardsPreFragment fragment = new CardsPreFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//
//        setHasOptionsMenu(true);
//
//        // Load the preferences from an XML resource
//        addPreferencesFromResource(R.xml.settings);
//        CheckBoxPreference zwavePref = (CheckBoxPreference) findPreference("pref_zwave");
//        zwavePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            public boolean onPreferenceClick(Preference preference) {
//                // open browser or intent here
//                Log.d(TAG, "onPreferenceClick: "+preference.getKey());
//                CheckBoxPreference cbf = (CheckBoxPreference) preference;
//                selectedPref.put("Zwave", cbf.isChecked());
//                return true;
//            }
//        });
//
//        Preference prefPowerG = findPreference("pref_powerG");
//        prefPowerG.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            public boolean onPreferenceClick(Preference preference) {
//                // open browser or intent here
//                Log.d(TAG, "onPreferenceClick: "+preference.getKey());
//                CheckBoxPreference cbf = (CheckBoxPreference) preference;
//                selectedPref.put("PowerG", cbf.isChecked());
//                return true;
//            }
//        });
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = super.onCreateView(inflater, container, savedInstanceState);
//        view.setBackgroundColor(getResources().getColor(android.R.color.white));
//
//        return view;
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//         selectionListener = (CardSelectionListener) activity;
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.pref_op, menu);
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.d(TAG, "onOptionsItemSelected: ");
//        FragmentManager manager = getActivity().getFragmentManager();
//        FragmentTransaction trans = manager.beginTransaction();
//        trans.remove(this);
//        trans.commit();
//        manager.popBackStack();
//
//        ArrayList<Card> cards = new ArrayList<>();
//
//        for (Map.Entry entry : selectedPref.entrySet()) {
//            String name = String.valueOf(entry.getKey());
//            boolean isActive = (Boolean) entry.getValue();
//            cards.add(new Card(name, isActive));
//        }
//
//            selectionListener.onCardSelection(cards);
//        return true;
//    }
//}
