package com.android.xoso.Lottery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.xoso.Config;
import com.android.xoso.R;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;


public class MienBacFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<KQMB> mKqmbData;
    private MienBacAdapter mienBacAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mien_bac, container, false);

        recyclerView = view.findViewById(R.id.mb_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mKqmbData = new ArrayList<>();
        mienBacAdapter = new MienBacAdapter(view.getContext(), mKqmbData);
        recyclerView.setAdapter(mienBacAdapter);


        new GetData().execute("today");
        return view;
    }

    public void changeDate(String d) {
        new GetData().execute(d);
    }

    public void initializeData(String jsonData) {

        //Log.v("ketqua", jsonData);

        String[] listTenGiai = {"Đặc Biệt", "Giải Nhất", "Giải Nhì", "Giải Ba", "Giải Tư", "Giải Năm", "Giải Sáu", "Giải Bảy"};


        try {
            Gson gson = new Gson();
            Result result = gson.fromJson(jsonData, Result.class);
            getActivity().setTitle(result.title);

            if (result == null) {
                result = new Result();
                result.giaidb = "";
                result.giainhat = "";
                result.giainhi = "";
                result.giaiba = "";
                result.giaitu = "";
                result.giainam = "";
                result.giaisau = "";
                result.giaibay = "";
            }

            List<String> listKQGiai = new ArrayList<>();
            listKQGiai.add(result.giaidb);
            listKQGiai.add(result.giainhat);
            listKQGiai.add(result.giainhi);
            listKQGiai.add(result.giaiba);
            listKQGiai.add(result.giaitu);
            listKQGiai.add(result.giainam);
            listKQGiai.add(result.giaisau);
            listKQGiai.add(result.giaibay);


            mKqmbData.clear();

            for (int i = 0; i < listTenGiai.length; i++) {
                mKqmbData.add(new KQMB(listTenGiai[i], listKQGiai.get(i)));
            }
            mienBacAdapter.notifyDataSetChanged();

        } catch (Exception e) {

        }
    }


    @SuppressLint("StaticFieldLeak")
    class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {


            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL(Config.url + "/api/json.php?kq=mb&date=" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int code = urlConnection.getResponseCode();


                if (code == 200) {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    if (in != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                        String line = "";

                        while ((line = bufferedReader.readLine()) != null)
                            result += line;
                    }
                    in.close();
                }


                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }


            Log.v("ketqua ", result);

            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("ketqua ", result);
            initializeData(result);
        }
    }

}
