package com.android.xoso.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.xoso.Config;
import com.android.xoso.Info;
import com.android.xoso.LoginActivity;
import com.android.xoso.Lottery.Result;
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

public class ProfileActivity extends AppCompatActivity {

    PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = this.getIntent();

        String tab = "";
        try {
            tab = intent.getStringExtra("tab");
        } catch (Exception ex) {

        }

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Thông tin"));
        tabLayout.addTab(tabLayout.newTab().setText("Chọn số"));
        tabLayout.addTab(tabLayout.newTab().setText("Lịch sử"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Log.v("tab", tab);

        if (tab.equals("play")) {
            viewPager.setCurrentItem(1);
        } else if (tab.equals("history")) {
            viewPager.setCurrentItem(2);
        } else {
            viewPager.setCurrentItem(0);
        }


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    InfoFragment fragment = (InfoFragment) adapter.getItem(tab.getPosition());
                    fragment.callGetInfo();
                } else if (tab.getPosition() == 2) {
                    HistoryFragment fragment = (HistoryFragment) adapter.getItem(tab.getPosition());
                    fragment.callGetHistory();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        SharedPreferences prefs = getSharedPreferences("xoso", MODE_PRIVATE);
        String user_token = prefs.getString("user_token", null);
        if (user_token != null) {

            new GetInfo().execute(user_token);

        } else {
            Intent intent_login = new Intent(this, LoginActivity.class);
            startActivity(intent_login);
            finish();
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

            if (result.equals("require_login")) {
                SharedPreferences prefs = getSharedPreferences("xoso", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("user_token");
                editor.commit();

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
