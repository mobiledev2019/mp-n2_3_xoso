package com.android.xoso.user;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.xoso.Config;
import com.android.xoso.LoginActivity;
import com.android.xoso.R;

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
public class PlayFragment extends Fragment {


    public PlayFragment() {
        // Required empty public constructor
    }

    Toast toast;
    EditText edt_number;
    EditText edt_point;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play, container, false);

        Button btn_play = view.findViewById(R.id.btn_play);
        edt_number = view.findViewById(R.id.edt_number);
        edt_point = view.findViewById(R.id.edt_point);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edt_number.getText().toString().equals("") || edt_point.getText().toString().equals("")) {
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(getContext(), "Bạn phải nhập đủ thông tin!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {


                    SharedPreferences prefs = getContext().getSharedPreferences("xoso", MODE_PRIVATE);
                    String user_token = prefs.getString("user_token", null);
                    if (user_token != null) {
                        new PlayAsync().execute(user_token, edt_number.getText().toString(), edt_point.getText().toString());
                    }

                }

            }
        });

        return view;
    }

    class PlayAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL(Config.url + "/api/play.php");
                urlConnection = (HttpURLConnection) url.openConnection();


                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes("token=" + params[0] + "&number=" + params[1] + "&point=" + params[2]);
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

            if (result.equals("require_login")) {
                SharedPreferences prefs = getContext().getSharedPreferences("xoso", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("user_token", "");
                editor.commit();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

            } else if (result.equals("ok")) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getContext(), "Chọn số thành công!", Toast.LENGTH_SHORT);
                toast.show();

                edt_number.setText("");
                edt_point.setText("");

            } else if (result.equals("error")) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getContext(), "Có lỗi xảy ra!", Toast.LENGTH_SHORT);
                toast.show();
            } else if (result.equals("not_enough_point")) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getContext(), "Tài khoản bạn không đủ điểm!", Toast.LENGTH_SHORT);
                toast.show();
            }
            //Log.v("result_login", result);
        }
    }


}
