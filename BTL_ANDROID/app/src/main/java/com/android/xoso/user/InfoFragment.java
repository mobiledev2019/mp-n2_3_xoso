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
import android.widget.TextView;

import com.android.xoso.Config;
import com.android.xoso.Info;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    public TextView tv_username;
    public TextView tv_point;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);


        tv_username = view.findViewById(R.id.tv_username);
        tv_point = view.findViewById(R.id.tv_point);

        SharedPreferences prefs = view.getContext().getSharedPreferences("xoso", MODE_PRIVATE);
        String user_token = prefs.getString("user_token", null);
        if (user_token != null) {

            new GetInfo().execute(user_token);

        } else {
            Intent intent_login = new Intent(view.getContext(), LoginActivity.class);
            startActivity(intent_login);
            getActivity().finish();
        }

        return view;
    }


    public void callGetInfo() {
        SharedPreferences prefs = getContext().getSharedPreferences("xoso", MODE_PRIVATE);
        String user_token = prefs.getString("user_token", null);
        if (user_token != null) {

            new GetInfo().execute(user_token);

        } else {
            Intent intent_login = new Intent(getContext(), LoginActivity.class);
            startActivity(intent_login);
            getActivity().finish();
        }
    }


    @SuppressLint("StaticFieldLeak")
    class GetInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL(Config.url + "/api/get_info.php");
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
                Info info = gson.fromJson(result, Info.class);

                tv_username.setText("Tên đăng nhập: " + info.getUsername());
                tv_point.setText("Điểm: " + info.getPoint());

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
