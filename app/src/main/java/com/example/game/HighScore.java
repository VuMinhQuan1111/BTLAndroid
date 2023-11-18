package com.example.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
    private Button bonus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score);
        Anhxa();
        back = findViewById(R.id.back_btn);
        bonus = findViewById(R.id.btnBonus);
        bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HighScore.this, Bonus.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HighScore.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Cấu hình listview
        final ArrayList<String> mang = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                mang);
        listScore.setAdapter(adapter);

        // Bắt sự kiện
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mang.clear(); // Clear the old list before updating

                ArrayList<String> tempList = new ArrayList<>(); // Temporary list for sorting
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.hasChild("name") && snapshot.hasChild("score")) {
                        String name = snapshot.child("name").getValue().toString();
                        String score = snapshot.child("score").getValue().toString();
                        String item = name + " - " + score;
                        tempList.add(item);
                    }
                }

                // Sort the temporary list based on scores
                Collections.sort(tempList, new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        int score1 = Integer.parseInt(s1.split(" - ")[1].trim());
                        int score2 = Integer.parseInt(s2.split(" - ")[1].trim());
                        return Integer.compare(score2, score1);
                    }
                });

                // Populate the final list with sorted items and display the rank
                for (int i = 0; i < tempList.size(); i++) {
                    String item = (i + 1) + ": " + tempList.get(i);
                    mang.add(item);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void Anhxa() {
        listScore = findViewById(R.id.listView);
    }
}
