package com.android.xoso.user;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.xoso.Config;
import com.android.xoso.History;
import com.android.xoso.LoginActivity;
import com.android.xoso.R;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {


    public HistoryFragment() {
        // Required empty public constructor
    }

    private ListView lv_history;
    private ArrayList<History> histories;
    private HistoryAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        SharedPreferences prefs = view.getContext().getSharedPreferences("xoso", MODE_PRIVATE);
        String user_token = prefs.getString("user_token", null);
        if (user_token != null) {

            new GetHistory().execute(user_token);

        } else {
            Intent intent_login = new Intent(view.getContext(), LoginActivity.class);
            startActivity(intent_login);
            getActivity().finish();
        }

        lv_history = view.findViewById(R.id.lv_history);


        histories = new ArrayList<>();

        adapter = new HistoryAdapter(getContext(), histories);

        lv_history.setAdapter(adapter);


        return view;
    }

    public void callGetHistory(){
        SharedPreferences prefs = getContext().getSharedPreferences("xoso", MODE_PRIVATE);
        String user_token = prefs.getString("user_token", null);
        if (user_token != null) {

            new GetHistory().execute(user_token);

        } else {
            Intent intent_login = new Intent(getContext(), LoginActivity.class);
            startActivity(intent_login);
            getActivity().finish();
        }
    }


    @SuppressLint("StaticFieldLeak")
    class GetHistory extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL(Config.url + "/api/get_history.php");
                urlConnection = (HttpURLConnection) url.openConnection();


                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes("token=" + params[0]);
                wr.flush();
                wr.close();


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
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (!result.equals("require_login")) {


                Gson gson = new Gson();
                History[] his = gson.fromJson(result, History[].class);
                histories.clear();
                for (History h : his) {

                    histories.add(h);
                }

                adapter.notifyDataSetChanged();

                //Log.v("historyssss", histories[0].getNumber());


            } else {
                SharedPreferences prefs = getContext().getSharedPreferences("xoso", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("user_token");
                editor.commit();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }
    }

}
