package com.minhnhan.sever;

import com.com.minhnhan.models.Atm;
import com.com.minhnhan.models.AtmDetail;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MinhNhan on 27/04/2016.
 * Restore all data off program from server
 */
public class DataManager {
    private static DataManager instance;
    private ArrayList<Atm> atm;
    private HashMap<String, AtmDetail> atmDetails;
    private String wayJson;

    public static DataManager getInstance() {
        if (instance == null)
            instance = new DataManager();
        return instance;

    }

    public void setAtmDetail(String atmId, AtmDetail atmDetail) {
        if (atmDetails == null)
            atmDetails = new HashMap<String, AtmDetail>();
        atmDetails.put(atmId, atmDetail);
    }

    public AtmDetail getAtmDetail(String atmId) {
        if (atmDetails == null)
            return null;
        return atmDetails.containsKey(atmId) ? atmDetails.get(atmId)
                : null;
    }

    public void setWayJson(String json) {
        wayJson = json;
    }
    public String getWayJson() {
        return wayJson;
    }
}