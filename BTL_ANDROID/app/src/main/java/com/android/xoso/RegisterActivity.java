package com.android.xoso;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.xoso.user.ProfileActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_login_if_has_account, btn_register_account;
    private EditText edt_register_username, edt_register_password, edt_register_confirm_password;
    Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_register_username = findViewById(R.id.edt_register_username);
        edt_register_password = findViewById(R.id.edt_register_password);
        edt_register_confirm_password = findViewById(R.id.edt_register_confirm_password);


        btn_register_account = findViewById(R.id.btn_register_account);

        btn_register_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username  = edt_register_username.getText().toString();
                String password  = edt_register_password.getText().toString();
                String cf_password  = edt_register_confirm_password.getText().toString();

                if(username.equals("") || password.equals("") || cf_password.equals("")){
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(getBaseContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT);
                    toast.show();
                } else if(!password.equals(cf_password)){
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(getBaseContext(), "Xác nhận mật khẩu không chính xác", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    new RegisterAsync().execute(username, password);
                }




            }
        });


        btn_login_if_has_account = findViewById(R.id.btn_login_if_has_account);
        btn_login_if_has_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    class RegisterAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL(Config.url + "/api/register.php");
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

            if (result.equals("user_exist")) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getBaseContext(), "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT);
                toast.show();
            } else if (result.equals("error")) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getBaseContext(), "Có lỗi xảy ra!", Toast.LENGTH_SHORT);
                toast.show();
            } else if (result.equals("ok")) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getBaseContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}
