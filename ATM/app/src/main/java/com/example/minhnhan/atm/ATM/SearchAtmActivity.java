package com.example.minhnhan.atm.ATM;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.com.minhnhan.adapters.BankAdapter;
import com.com.minhnhan.adapters.DistAndCityAdapter;
import com.com.minhnhan.models.AdressDetail;
import com.com.minhnhan.models.AtmDetail;
import com.com.minhnhan.models.Bank;
import com.example.minhnhan.atm.R;
import com.minhnhan.sever.DataManager;

import java.util.ArrayList;

public class SearchAtmActivity extends AppCompatActivity {
    private AdressDetail adressAtm;
    private ArrayList<String> dist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_atm);
        final BankAdapter bankAdapter = new BankAdapter(this, DataManager.getInstance().getBankDetail("bank").bankDetail);
        final Spinner bankSpinner = (Spinner) findViewById(R.id.bank_spinner);
        assert bankSpinner != null;
        bankSpinner.setAdapter(bankAdapter);

        //spinner city
        adressAtm = new AdressDetail();
        final DistAndCityAdapter cityAdapter = new DistAndCityAdapter(this, adressAtm.getCityList());
        final Spinner citySpinner = (Spinner) findViewById(R.id.city_spinner);
        citySpinner.setAdapter(cityAdapter);

        //spinner district
        final DistAndCityAdapter distAdapter = new DistAndCityAdapter(this, adressAtm.getHcm().getDist());
        final Spinner distSpinner = (Spinner) findViewById(R.id.dist_spinner);
        distSpinner.setAdapter(distAdapter);

        //Update district spinner when re-select city spinner
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                distAdapter.setdata(adressAtm.getDisFromCity(citySpinner.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Search atm
        ImageButton imgbtnSearch = (ImageButton) findViewById(R.id.imgbtn_search);
        imgbtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bank bank = (Bank) bankSpinner.getSelectedItem();
                String city = citySpinner.getSelectedItem().toString();
                String dist = distSpinner.getSelectedItem().toString();
                AtmDetail data = DataManager.getInstance().getAtmDetail("atm");
                AtmDetail search = new AtmDetail();
                for (int i = 0; i < data.atmDetail.size(); i++) {
                    if (data.atmDetail.get(i).bankId == bank.id) {
                        if (data.atmDetail.get(i).city.equals(city)){
                            if (data.atmDetail.get(i).district.endsWith(dist)){
                                search.atmDetail.add(data.atmDetail.get(i));
                            }
                        }
                    }
                }
                DataManager.getInstance().setSearchDetail("search", search);
                Intent i = new Intent(SearchAtmActivity.this, MapsActivity.class);
                //status search
                MapsActivity.Status=1;
                startActivity(i);
                finish();
            }
        });

    }
}
