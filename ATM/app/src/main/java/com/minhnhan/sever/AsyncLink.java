package com.minhnhan.sever;

/**
 * Created by MinhNhan on 27/04/2016.
 * post request to server to get AtmDetail Json
 */

import android.os.AsyncTask;

import com.com.minhnhan.models.AtmDetail;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class AsyncLink extends AsyncTask<String, String, String> {
    private AsyncListener listener;

    public AsyncLink(AsyncListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return DataServices.getRequest(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            AtmDetail dataAtm = new AtmDetail(new JSONArray(result));
            DataManager.getInstance().setAtmDetail("0", dataAtm);
            if (listener != null)
                listener.onAsyncComplete();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}