package com.example.minhnhan.atm.ATM;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.minhnhan.atm.R;
import com.google.android.gms.maps.model.Marker;
import com.minhnhan.sever.AsyncAtm;
import com.minhnhan.sever.AsyncListener;
import com.minhnhan.sever.CheckConnect;

public class SplashActivity extends AppCompatActivity {
    AsyncListener listener = new AsyncListener() {

        @Override
        public void onAsyncComplete() {
            Intent i = new Intent(SplashActivity.this, MapsActivity.class);
            startActivity(i);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // get atm list from server
        CheckConnect connect = new CheckConnect();
        if (connect.isNetworkAvailable(this)) {
            if (connect.isGPSEnabled(this)) {
                String link = "http://find-atm.apphb.com/v1/GetAtmList";
                AsyncAtm atm = new AsyncAtm(listener);
                atm.execute(link);
            } else {
                //Show message about GPS problem and exit this app
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Xin vui lòng kết nối GPS!");
                dlgAlert.setTitle("Không thể truy cập GPS!");
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss the dialog
                                finish();
                                System.exit(0);
                            }
                        });
                dlgAlert.create().show();
            }


        } else {
            //Show message about Internet problem and exit this app
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Xin vui lòng kết nối Internet!");
            dlgAlert.setTitle("Không thể truy cập Internet!");
            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                            finish();
                            System.exit(0);
                        }
                    });
            dlgAlert.create().show();
        }
    }
}
