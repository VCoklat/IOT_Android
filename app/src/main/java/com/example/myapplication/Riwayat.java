package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Riwayat extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DataAdapter adapter;
    private ArrayList<Data> dataArrayList;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat);
        getJSON();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }


    private void showEmployee(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            final MySQLiteHelper db = new MySQLiteHelper(this);
            dataArrayList = new ArrayList<>();
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(konfigurasi.TAG_ID);
                String durasi = jo.getString(konfigurasi.TAG_TERAKHIR);
                String harga = db.getSetting(1);
                String power = db.getSetting(0);
                Integer name= Integer.parseInt(harga)*Integer.parseInt(power)/60*Integer.parseInt(id);

                dataArrayList.add(new Data(id, name.toString(), durasi));
                //addData();
            }


            adapter = new DataAdapter(dataArrayList);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Riwayat.this);

            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Riwayat.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(konfigurasi.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();

    }

}
