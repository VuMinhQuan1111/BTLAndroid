package com.example.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.action_home);

        Button play =findViewById(R.id.play);

        //nhấp chuột vào 3 icon ở dưới cùng
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_home:
                        return true;

                    case R.id.action_score:
                        startActivity(new Intent(getApplicationContext()
                                ,HighScore.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.action_user:
                        startActivity(new Intent(getApplicationContext()
                                ,User.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

        //Nhấn vào chơi game

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

    }
}