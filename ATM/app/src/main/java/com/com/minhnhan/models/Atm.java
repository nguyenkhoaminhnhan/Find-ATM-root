package com.com.minhnhan.models;

import android.util.Base64;

import org.json.JSONObject;

/**
 * Created by MinhNhan on 27/04/2016.
 * Parse AtmDetail JsonObject to Atm
 */
public class Atm {
    public final String ID  = "Id";
    public final String BANKID  = "BankId";
    public final String NAME = "Name";
    public final String LAT  = "Lat";
    public final String LNG = "Lng";
    public final String DISTRICT  = "District";
    private final String CITY = "City";

    public long id;
    public long bankId;
    public String name;
    public double lat;
    public double lng;
    public String district;
    public String city;

    public Atm(JSONObject object) {
        try {
            if (object.has(ID))
                id = Long.parseLong(object.getString(ID));
            if (object.has(BANKID))
                bankId = Long.parseLong(object.getString(BANKID));
            if (object.has(NAME))
                name = object.getString(NAME);
            if (object.has(LAT))
                lat = Double.parseDouble(object.getString(LAT));
            if (object.has(LNG))
                lng = Double.parseDouble(object.getString(LNG));
            if (object.has(DISTRICT))
                district = object.getString(DISTRICT);
            if (object.has(CITY))
                city = object.getString(CITY);
        } catch (Exception e) {
        }
    }

    private String decodeUrl(String decodeUrl) {
        byte[] bytes = Base64.decode(decodeUrl.substring(11), Base64.DEFAULT);
        return new String(bytes);
    }
}
