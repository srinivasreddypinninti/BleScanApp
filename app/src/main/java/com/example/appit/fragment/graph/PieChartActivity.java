/*
package com.example.appit.fragment.graph;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.appit.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class PieChartActivity extends AppCompatActivity {

    TextView tvR, tvPython, tvCPP, tvJava;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        // Link those objects with their
        // respective id's that
        // we have given in .XML file
        tvR = findViewById(R.id.tvR);
        tvPython = findViewById(R.id.tvPython);
        tvCPP = findViewById(R.id.tvCPP);
        tvJava = findViewById(R.id.tvJava);
        pieChart = findViewById(R.id.piechart);


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("score", "70");
            jsonObject.put("positives", "Cameras are installed (+10),  CO sensor installed (+10), Door lock installed (+20)");
            jsonObject.put("Negatives", "Panel is connected to unsecure wifi (-10), Device software is not up-to-date (-20)");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Creating a method setData()
        // to set the text in text view and pie chart
        setData(jsonObject);
    }

    private void setData(JSONObject jsonObject)
    {

        // Set the percentage of language used
        try {
            String score = jsonObject.getString("score");
            String positives = jsonObject.getString("positives");
            String negatives = jsonObject.getString("Negatives");

            String[] pos = positives.split(",");


//            "Cameras are installed (+10)"
            String noOfCams = pos[0];
            int start = noOfCams.length() - 3;
            int end = noOfCams.length() - 1;
            String camsCount = noOfCams.substring(start, end);

            String noOfSen = pos[1];
            int s = noOfCams.length() - 3;
            int e = noOfCams.length() - 1;
            String senCount = noOfCams.substring(s, e);

            String noOfDl = pos[2];
            int ds = noOfCams.length() - 3;
            int de = noOfCams.length() - 1;
            String dlCount = noOfCams.substring(s, e);


            tvR.setText(camsCount);
            tvPython.setText(senCount);
            tvCPP.setText(dlCount);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Set the data and color to the pie chart
        pieChart.addPieSlice(
                new PieModel(
                        "R",
                        Integer.parseInt(tvR.getText().toString()),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "Python",
                        Integer.parseInt(tvPython.getText().toString()),
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "C++",
                        Integer.parseInt(tvCPP.getText().toString()),
                        Color.parseColor("#EF5350")));
        pieChart.addPieSlice(
                new PieModel(
                        "Java",
                        Integer.parseInt(tvJava.getText().toString()),
                        Color.parseColor("#29B6F6")));

        // To animate the pie chart
        pieChart.startAnimation();



    }
}*/
