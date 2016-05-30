package com.com.minhnhan.models;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by MinhNhan on 27/04/2016.
 * Parse the Json from server to AtmDetail List
 */
public class BankDetail {

    public ArrayList<Bank> bankDetail;
    public BankDetail(){
        bankDetail = new ArrayList<Bank>();
    }
    public BankDetail(JSONArray object) {
        try {
            bankDetail = new ArrayList<Bank>();

                for (int i = 0; i < object.length(); i++) {
                    Bank bank = new Bank(
                            object.getJSONObject(i));
                    bankDetail.add(bank);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}