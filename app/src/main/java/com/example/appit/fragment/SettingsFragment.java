//package com.example.appit.fragment;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.preference.ListPreference;
//import androidx.preference.Preference;
//import androidx.preference.PreferenceFragmentCompat;
//
//import android.preference.PreferenceFragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.appit.MyWindowView;
//import com.example.appit.R;
//
//public class SettingsFragment extends Fragment /*implements View.OnTouchListener*/ {
//
//    private static final String TAG = "SettingsFragment";
//
//    private View trayLayout;
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        trayLayout = LayoutInflater.from(getContext()).inflate(R.layout.settings_tray, null);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_settings, container, false);
//        view.findViewById(R.id.btn_open).setOnTouchListener(btnOnTouch);
//        view.setOnTouchListener(btnOnTouch);
//        return view;
//    }
//
//    private View.OnTouchListener btnOnTouch = new View.OnTouchListener() {
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
////                SettingsTray.getInstance(getActivity()).setSlideView();
//                int x = (int) event.getX();
//                int y = (int) event.getY();
//               MyWindowView windowView = MyWindowView.getInstance(getActivity());
//
//               if (windowView.isTouchInSideWindow(x, y)) {
//                   return true;
//               }
//
//               windowView.animate();
//
////               if (!windowView.isWindowOpen()) {
////                   windowView.openWindow();
////               } else {
////                   windowView.closeWindow();
////               }
//            }
//            return true;
//        }
//    };
//
////    @Override
////    public boolean onTouch(View v, MotionEvent event) {
////        if (event.getAction() == MotionEvent.ACTION_UP) {
////            // check for out side tray view
////        }
////        return true;
////    }
//}