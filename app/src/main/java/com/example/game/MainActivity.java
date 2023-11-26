package com.example.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends Activity {

    private Button highscore, user, play, medal, cre;
    private int second = 0;
    public boolean running;
    private boolean isMute;

    private TextView score, username;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Score");
    //nut hiển thị số lần chơi
    Button btnDemSoLanDaChoi;

    //text hiển thị số lần chơi
    TextView SoLanChoi;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //running = true;
        highscore = findViewById(R.id.highScore);
        user = findViewById(R.id.userProfile);
        play = findViewById(R.id.play);
        score = findViewById(R.id.textViewscore);
        username = findViewById(R.id.ussername);
        medal = findViewById(R.id.medal);
        cre = findViewById(R.id.credit);

        //nut dem so lan da choi
        btnDemSoLanDaChoi = findViewById(R.id.btnDemSoLanDaChoi);
        //text hiển thị số lần chơi
        SoLanChoi = findViewById(R.id.txtSoLanChoi);


        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        isMute = prefs.getBoolean("isMute", false);

        final ImageView volumeCtrl = findViewById(R.id.volumeCtrl);

        if (isMute)
            volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp);
        else
            volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp);


        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isMute = !isMute;
                if (isMute)
                    volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp);
                else
                    volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isMute", isMute);
                editor.apply();

            }
        });

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot snapshott : snapshot.getChildren()) {
                        //Score scorer = snapshott.getValue(Score.class);
                        ScoreUser usersc = snapshott.getValue(ScoreUser.class);
                        if (usersc.idus.equals(firebaseUser.getUid())) {
                            username.setText(usersc.name + "");
                            score.setText("" + usersc.score);
                            break;
                        } else {

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        highscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HighScore.class);
                startActivity(intent);
            }
        });
        medal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Bonus.class);
                startActivity(intent);
            }
        });


        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, User.class);
                startActivity(intent);
            }
        });


        //nut đếm số lần đã chơi game
        btnDemSoLanDaChoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy số lần chơi từ SharedPreferences và hiển thị
                int soLanChoi = prefs.getInt("soLanChoi", 0);
                SoLanChoi.setText("Số lần chơi: " + soLanChoi);
            }
        });

        // Nhấn vào chơi game
        //Nhấn vào chơi game

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khi nhấn nút play, tăng số lần chơi lên 1 và cập nhật vào SharedPreferences
                int soLanChoi = prefs.getInt("soLanChoi", 0) + 1;
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("soLanChoi", soLanChoi);
                editor.apply();

                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
    }
}
//        cách hiển thị số lần chơi game 2
//                play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Khi nhấn nút play, tăng số lần chơi lên 1 và cập nhật vào SharedPreferences
//                int soLanChoi = prefs.getInt("soLanChoi", 0) + 1;
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putInt("soLanChoi", soLanChoi);
//                editor.apply();
//
//                // Hiển thị số lần chơi
//                SoLanChoi.setText("Số lần chơi: " + soLanChoi);
//                startActivity(new Intent(MainActivity.this, GameActivity.class));
//            }
//        });
//
//        cre.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, credit.class));
//            }
//        });



        //runTimer();

//    }

//    public void runTimer(){
//        //create text view
//        final TextView timeView = findViewById(R.id.time_view);
//
//        //create Handler
//        final Handler handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                int hours = second / 3600;
//                int minutes = (second % 3600)/60;
//                int secs = second % 60;
//
//                String time = String.format(Locale.getDefault(),
//                        "%d:%02d:%02d",hours,
//                        minutes, secs);
//
//                timeView.setText(time);
//                if(running){
//                    second++;
//                }
//                handler.postDelayed(this,1000);
//
//            }
//        });
//    }
//}
