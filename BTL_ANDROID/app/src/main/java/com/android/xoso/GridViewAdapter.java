package com.android.xoso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> icons;
    private ArrayList<String> letters;
    private LayoutInflater inflater;

    public GridViewAdapter(Context context, ArrayList<Integer> icons, ArrayList<String> letters) {
        this.context = context;
        this.icons = icons;
        this.letters = letters;
    }

    @Override
    public int getCount() {
        return letters.size();
    }

    @Override
    public Object getItem(int position) {
        return letters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;
        if (gridView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.custom_grid_view, null);
        }

        ImageView icon = gridView.findViewById(R.id.imageView);
        TextView letter = gridView.findViewById(R.id.textView);
        icon.setImageResource(icons.get(position));
        letter.setText(letters.get(position));

        return gridView;
    }
}
