package com.example.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HighScore extends Activity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Score");


    ListView listScore;
    private TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score);

        back = findViewById(R.id.back_btn);

        Anhxa();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HighScore.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //cau hinh listview
        final ArrayList<String> mang = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                mang);
        listScore.setAdapter(adapter);

        // Bat su kien
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mang.clear(); // Xóa danh sách cũ trước khi cập nhật mới
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.hasChild("name") && snapshot.hasChild("score")) {
                        String name = snapshot.child("name").getValue().toString();
                        String score = snapshot.child("score").getValue().toString();
                        String item = name + ": " + score;
                        mang.add(item);
                    }
                }

//                // Sắp xếp danh sách theo điểm số
                Collections.sort(mang, new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        // Sắp xếp theo điểm số giảm dần
                        int score1 = Integer.parseInt(s1.split(":")[1].trim());
                        int score2 = Integer.parseInt(s2.split(":")[1].trim());
                        return Integer.compare(score2, score1);
                    }
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi
            }
        });


    }


    public void Anhxa(){
        listScore = (ListView)  findViewById(R.id.listView);
    }
}
