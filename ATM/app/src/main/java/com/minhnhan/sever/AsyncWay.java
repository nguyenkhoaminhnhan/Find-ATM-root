package com.minhnhan.sever;

import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by MinhNhan on 15/05/2016.
 * Post request to Google to get Json Path
 */
public class AsyncWay extends AsyncTask<String, String, String> {
    private AsyncListener listener;
    private String key;

    public AsyncWay(AsyncListener listener) {
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
        DataManager.getInstance().setWayJson(result);
        if (listener != null)
            listener.onAsyncComplete();
    }
}
