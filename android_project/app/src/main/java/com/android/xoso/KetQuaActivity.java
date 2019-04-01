package com.android.xoso;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class KetQuaActivity extends AppCompatActivity {

    private WebView mWebView;
    private String mien;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_bac:
                    mien = "bac";
                    mWebView.loadUrl("http://10.14.0.52/api.php?mien="+mien);
                    return true;
                case R.id.navigation_trung:
                    mien = "trung";
                    mWebView.loadUrl("http://10.14.0.52/api.php?mien="+mien);
                    return true;
                case R.id.navigation_nam:
                    mien = "nam";
                    mWebView.loadUrl("http://10.14.0.52/api.php?mien="+mien);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ket_qua);
        mien = "bac";
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mWebView = findViewById(R.id.webview);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        WebSettings websettings = mWebView.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setDomStorageEnabled(true);
        websettings.setUserAgentString("Mozilla/5.0 (Linux; Android 4.1.2; C1905 Build/15.1.C.2.8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.114 Mobile Safari/537.36");

        mWebView.loadUrl("http://10.14.0.52/api.php?mien="+mien);

    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),
                getString(R.string.datepicker));
    }

    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (year_string+ month_string + day_string);

        Toast.makeText(this, "Date: " + dateMessage, Toast.LENGTH_SHORT).show();

        mWebView.loadUrl("http://10.14.0.52/api.php?mien="+mien+"&ngay="+dateMessage);
    }

}
