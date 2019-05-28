package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Pengaturan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengaturan);
        final MySQLiteHelper db = new MySQLiteHelper(this);

        String power = db.getSetting(0);
        final EditText spesifikasi = findViewById(R.id.spesifikasi_perangkat);
        spesifikasi.setText(power);

        String harga = db.getSetting(1);
        final EditText harga_ = findViewById(R.id.harga);
        harga_.setText(harga);

        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.updateBook(harga_.getText().toString(),spesifikasi.getText().toString());
                AlertDialog alertDialog = new AlertDialog.Builder(Pengaturan.this).create();
                alertDialog.setTitle("Settings Saved");
                alertDialog.setMessage("Data is saved");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                startActivity(new Intent(Pengaturan.this, Pengaturan.class));
                            }
                        });
                alertDialog.show();

            }
        });
    }

}
