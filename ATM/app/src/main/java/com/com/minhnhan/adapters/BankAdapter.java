package com.com.minhnhan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.com.minhnhan.models.Bank;
import com.example.minhnhan.atm.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by MinhNhan on 27/05/2016.
 */
public class BankAdapter extends BaseAdapter {

    public ArrayList<Bank> data;
    private Context context;
    Bank item;
    private LayoutInflater mInflater;

    public BankAdapter(Context context, ArrayList<Bank> data) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.search_row, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.bank_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        item = data.get(position);
        holder.name.setText(item.name);
        return convertView;
    }

    public void addMore(ArrayList<Bank> data) {
        if (this.data == null)
            this.data = data;
        else
            this.data.addAll(data);
        // yeu cau adapter refresh lai view
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private TextView name;
    }

    public void setdata(ArrayList<Bank> temp) {
        data = temp;
        notifyDataSetChanged();
    }

}