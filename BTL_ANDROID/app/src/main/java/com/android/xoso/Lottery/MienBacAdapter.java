package com.android.xoso.Lottery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.xoso.R;

import java.util.ArrayList;

public class MienBacAdapter extends RecyclerView.Adapter<MienBacAdapter.ViewHolder> {
    private ArrayList<KQMB> kqmbData;
    private Context context;

    public MienBacAdapter(Context context ,ArrayList<KQMB> kqmbData) {
        this.kqmbData = kqmbData;
        this.context = context;
    }

    @NonNull
    @Override
    public MienBacAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.kqmb,viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MienBacAdapter.ViewHolder viewHolder, int position) {
        KQMB currentKqmb = kqmbData.get(position);
        viewHolder.bindTo(currentKqmb);
    }

    @Override
    public int getItemCount() {
        return kqmbData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tenGiai;
        private TextView kqGiai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenGiai = itemView.findViewById(R.id.ten_giai_mb);
            kqGiai = itemView.findViewById(R.id.kq_giai_mb);
        }

        void bindTo(KQMB kqmb){
            tenGiai.setText(kqmb.getTenGiai());
            kqGiai.setText(kqmb.getKetqua());
        }
    }

}
