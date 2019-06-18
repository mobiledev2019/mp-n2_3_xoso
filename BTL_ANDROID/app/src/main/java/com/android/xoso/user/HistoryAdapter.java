package com.android.xoso.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.xoso.History;
import com.android.xoso.R;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {

    Context context;

    ArrayList<History> histories;

    public HistoryAdapter(Context context, ArrayList<History> histories) {
        this.context = context;
        this.histories = histories;
    }


    @Override
    public int getCount() {
        return histories.size();
    }

    @Override
    public Object getItem(int position) {
        return histories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.tv_history_status = convertView.findViewById(R.id.tv_history_status);
            viewHolder.tv_history_point = convertView.findViewById(R.id.tv_history_point);
            viewHolder.tv_history_number = convertView.findViewById(R.id.tv_history_number);
            viewHolder.tv_history_date = convertView.findViewById(R.id.tv_history_date);
            viewHolder.tv_history_id = convertView.findViewById(R.id.tv_history_id);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        History history = histories.get(position);
        viewHolder.tv_history_id.setText(history.getPlay_ID());
        viewHolder.tv_history_date.setText(history.getDate());
        viewHolder.tv_history_number.setText(history.getNumber());
        viewHolder.tv_history_point.setText(history.getPoint());

        if (history.getStatus().equals("0")) {

            viewHolder.tv_history_status.setText("Đang chờ");
        } else if (history.getStatus().equals("1")) {

            viewHolder.tv_history_status.setText("Trúng");
        } else if (history.getStatus().equals("2")) {

            viewHolder.tv_history_status.setText("Trượt");
        }


        return convertView;
    }

    public class ViewHolder {
        TextView tv_history_id, tv_history_number, tv_history_point, tv_history_date, tv_history_status;
    }
}