package com.com.minhnhan.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MinhNhan on 27/04/2016.
 * Parse the Json from server to AtmDetail List
 */
public class AtmDetail  {

    public ArrayList<Atm> atmDetail;

    public AtmDetail(JSONArray object) {
        try {
                atmDetail = new ArrayList<Atm>();

                for (int i = 0; i < object.length(); i++) {
                    Atm atm = new Atm(
                            object.getJSONObject(i));
                    atmDetail.add(atm);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}