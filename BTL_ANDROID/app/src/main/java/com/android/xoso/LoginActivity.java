package com.android.xoso;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.xoso.user.ProfileActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText edt_login_username;
    private EditText edt_login_password;
    private Button btn_login;
    private Button btn_register_if_no_account;
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_login_username = findViewById(R.id.edt_login_username);
        edt_login_password = findViewById(R.id.edt_login_password);

        btn_login = findViewById(R.id.btn_login);
        btn_register_if_no_account = findViewById(R.id.btn_register_if_no_account);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edt_login_username.getText().toString();
                String password = edt_login_password.getText().toString();

                if(username.equals("") || password.equals("")){
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast = Toast.makeText(view.getContext(), "Ban phải nhập đủ tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    new LoginAsync().execute(username, password);
                }

            }
        });

        btn_register_if_no_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    class LoginAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL(Config.url + "/api/login.php");
                urlConnection = (HttpURLConnection) url.openConnection();


                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes("username=" + params[0] + "&password=" + params[1]);
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

            if (!result.equals("not_success")) {
                SharedPreferences prefs = getSharedPreferences("xoso", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("user_token", result);
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                intent.putExtra("tab", "");
                startActivity(intent);
                finish();

            } else {
                if(toast!=null){
                    toast.cancel();
                }
                toast = Toast.makeText(getBaseContext(), "Tên đăng nhập hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT);
                toast.show();
            }
            //Log.v("result_login", result);
        }
    }
}
