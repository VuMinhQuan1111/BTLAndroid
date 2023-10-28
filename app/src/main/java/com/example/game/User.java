package com.example.game;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User extends Activity {

    private ImageView userAvatar;
    private TextView tvName;

    private TextView back;
    private TextView tvGmail;

    private Button SOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        back = findViewById(R.id.back_btn);

        initUi();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
//
//        bottomNavigationView.setSelectedItemId(R.id.action_user);
//
//        //nhấp chuột vào 3 icon ở dưới cùng
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch(item.getItemId()){
//
//                    case R.id.action_home:
//                        startActivity(new Intent(getApplicationContext()
//                                ,MainActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
//
//                    case R.id.action_score:
//                        startActivity(new Intent(getApplicationContext()
//                                ,HighScore.class));
//                        overridePendingTransition(0,0);
//                        return true;
//
//                    case R.id.action_user:
//                        return true;
//
//                }
//                return false;
//            }
//        });
        SignOut();
        showUserInfomation();
    }
    private void SignOut(){
        SOut = findViewById(R.id.sign_out);
        SOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(User.this, SignIn.class);
                startActivity(intent);
                finish();
            }

        });
    }

    private void initUi() {
        userAvatar = findViewById(R.id.user_avatar);
        tvName = findViewById(R.id.user_name);
        tvGmail = findViewById(R.id.user_gmail);
    }

    private void showUserInfomation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if(name == null){
            tvName.setVisibility(View.GONE);
        }else{
            tvName.setVisibility(View.VISIBLE);
            tvName.setText(name);
        }


        tvName.setText(name);
        tvGmail.setText(email);
        //Glide.with(this).load(photoUrl).error(R.drawable.avatar_default).into(userAvatar);

    }
}
