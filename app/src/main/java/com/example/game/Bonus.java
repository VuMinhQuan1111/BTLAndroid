package com.example.game;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Bonus extends AppCompatActivity {

    private int score = 0;
    private int diemThuong = 0;
    private TextView txtScore;
    private TextView txtDiemThuong;
    private Button btnNhanThuong;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Score");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bonus);

        txtScore = findViewById(R.id.txtScore);
        txtDiemThuong = findViewById(R.id.txtDiemThuong);
        btnNhanThuong = findViewById(R.id.btnNhanThuong);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer userScore = snapshot.child("score").getValue(Integer.class);
                if (userScore != null) {
                    score = userScore;
                    updateDiem();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnNhanThuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (score >= 200) {
                    diemThuong += 5;
                    score += diemThuong;
                    updateDiem();
                    saveToFirebase();
                }
            }
        });
    }

    private void updateDiem() {
        txtScore.setText("Điểm hiện tại: " + score);
        txtDiemThuong.setText("Điểm thưởng: " + diemThuong);
    }

    private void saveToFirebase() {
        myRef.child("score").setValue(score);

    }
}
