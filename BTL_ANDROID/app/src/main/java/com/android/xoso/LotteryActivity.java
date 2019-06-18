package com.android.xoso;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.xoso.Lottery.DatePickerFragment;
import com.android.xoso.Lottery.MienBacFragment;
import com.android.xoso.Lottery.MienNamFragment;
import com.android.xoso.Lottery.MienTrungFragment;

public class LotteryActivity extends AppCompatActivity {
    protected Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);
        setTitle("Kết quả xổ số");

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        selectedFragment = new MienBacFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.datePicker) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datepicker");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_mien_bac:
                    selectedFragment = new MienBacFragment();
                    break;
                case R.id.navigation_mien_trung:
                    selectedFragment = new MienTrungFragment();
                    break;
                case R.id.navigation_mien_nam:
                    selectedFragment = new MienNamFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month + 1);
        if (month_string.length() == 1) {
            month_string = "0" + month_string;
        }
        String day_string = Integer.toString(day);
        if (day_string.length() == 1) {
            day_string = "0" + day_string;
        }
        String year_string = Integer.toString(year);
        String dateMessage = (day_string + month_string + year_string);
        MienBacFragment s = (MienBacFragment) selectedFragment;
        s.changeDate(dateMessage);
    }
}
