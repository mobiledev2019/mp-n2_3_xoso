package com.android.xoso;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.xoso.user.ProfileActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private ArrayList<String> letters = new ArrayList<>();
    private ArrayList<Integer> icons = new ArrayList<>();
    SharedPreferences.Editor editor;
    GridViewAdapter viewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        letters.addAll(Arrays.asList("Kết quả", "Cá nhân", "Chọn số", "Lịch sử"));
        icons.addAll(Arrays.asList(R.drawable.lottery, R.drawable.profile, R.drawable.play_button, R.drawable.history_button));

        SharedPreferences prefs = getSharedPreferences("xoso", MODE_PRIVATE);
        editor = prefs.edit();
        String user_token = prefs.getString("user_token", null);
        if (user_token != null) {
            letters.add("Đăng xuất");
            icons.add(R.drawable.logout_button);
        }


        viewAdapter = new GridViewAdapter(MainActivity.this, icons, letters);

        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(viewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, LotteryActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, ProfileActivity.class);
                        intent.putExtra("tab", "info");
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, ProfileActivity.class);
                        intent.putExtra("tab", "play");
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, ProfileActivity.class);
                        intent.putExtra("tab", "history");
                        startActivity(intent);
                        break;
                    case 4:
                        editor.remove("user_token");
                        editor.commit();
                        Toast.makeText(getBaseContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                        letters.remove(4);
                        icons.remove(4);
                        viewAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });


    }
}
