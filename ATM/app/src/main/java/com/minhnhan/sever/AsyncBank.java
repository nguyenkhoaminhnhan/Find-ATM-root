package com.minhnhan.sever;

import android.os.AsyncTask;

import com.com.minhnhan.models.AtmDetail;
import com.com.minhnhan.models.BankDetail;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by MinhNhan on 25/05/2016.
 */
public class AsyncBank  extends AsyncTask<String, String, String> {
    private AsyncListener listener;

    public AsyncBank(AsyncListener listener) {
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
            BankDetail dataBank = new BankDetail(new JSONArray(result));
            DataManager.getInstance().setBankDetail("bank", dataBank);
            if (listener != null)
                listener.onAsyncComplete();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}