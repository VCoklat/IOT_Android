package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LineChart chart;
    TextView durasi;
    TextView total;
    TextView biaya;
    Button riwayat;
    Button pengaturan;
    int lama_durasi,harga;
    private String JSON_STRING;
    private String power;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chart = findViewById(R.id.chart);
        durasi = findViewById(R.id.durasi);
        total = findViewById(R.id.total_power);
        biaya = findViewById(R.id.total_biaya);
        riwayat = findViewById(R.id.riwayat);
        TextView input = findViewById(R.id.input_power);
        pengaturan = findViewById(R.id.pengaturan);
        Button reset = findViewById(R.id.reset);
        final MySQLiteHelper db = new MySQLiteHelper(this);

        power = db.getSetting(0);
        input.setText(power);

        harga = Integer.parseInt(db.getSetting(1));

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                class GetJSON extends AsyncTask<Void,Void,String> {

                    ProgressDialog loading;
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        loading = ProgressDialog.show(MainActivity.this,"Mereset Data","Mohon Tunggu...",false,false);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        loading.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle("Resetting Data");
                        alertDialog.setMessage("Data is reset");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                                    }
                                });
                        alertDialog.show();
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        RequestHandler rh = new RequestHandler();
                        String s = rh.sendGetRequest(konfigurasi.URL_RESET);
                        return s;
                    }
                }
                GetJSON gj = new GetJSON();
                gj.execute();
            }
        });
        riwayat.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           startActivity(new Intent(MainActivity.this, Riwayat.class));
                                       }
                                   });
        pengaturan.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              startActivity(new Intent(MainActivity.this, Pengaturan.class));
                                          }
                                      });
        getJSON();
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                chart();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(konfigurasi.URL_CHART);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();

    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here

        finish();
        startActivity(getIntent());
    }


    public void chart()
    {

        final ArrayList<String> months = new ArrayList<String>();
        ArrayList<Entry> entries = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            lama_durasi=0;
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String status = jo.getString(konfigurasi.TAG_STATUS);
                String jam = jo.getString(konfigurasi.TAG_JAM);
                months.add(jam);
                entries.add(new Entry(i, Integer.valueOf(status)));
                if(Integer.valueOf(status)==1) lama_durasi=lama_durasi+1;

            }
            durasi.setText("Durasi Penggunaan : "+lama_durasi+" Menit");
            total.setText("KWH "+lama_durasi*Integer.parseInt(power)/60);
            biaya.setText("Rp "+lama_durasi*Integer.parseInt(power)/60*harga+",00");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        LineDataSet dataSet = new LineDataSet(entries, "STATUS");
        dataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSet.setValueTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        //****
        // Controlling X axis
        XAxis xAxis = chart.getXAxis();
        // Set the xAxis position to bottom. Default is top
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //Customizing x axis value

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return months.get((int) value);
            }
        };
        xAxis.setGranularity(2f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        //***
        // Controlling right side of y axis
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setEnabled(false);

        //***
        // Controlling left side of y axis
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setGranularity(1f);
        final String[] yaxis  = new String[]{"OFF", "ON"};

        IAxisValueFormatter datakiri = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return yaxis[(int) value];
            }
        };
        yAxisLeft.setValueFormatter(datakiri);
        Description description = chart.getDescription();
        description.setText("Waktu");
        // Setting Data
        LineData data = new LineData(dataSet);
        chart.setData(data);
        chart.animateX(2500);
        //refresh
        chart.invalidate();
    }
}