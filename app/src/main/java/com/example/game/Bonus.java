
package com.example.game;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Bonus extends Activity {
    private int score = 0;
    private int scoreBonus = 0;
    private TextView txtScore;
    private TextView txtScoreBonus;
    private Button btnNhanThuong;
    private boolean check = false;
    private TextView back;
    private boolean danhan = false;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Score");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bonus);
        txtScore = findViewById(R.id.txtScore);
        txtScoreBonus = findViewById(R.id.txtScoreBonus);
        back = findViewById(R.id.txtBack);
        btnNhanThuong = findViewById(R.id.btnNhanThuong);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bonus.this, MainActivity.class);
                startActivity(intent);
            }
        });


            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ScoreUser userScore = dataSnapshot.getValue(ScoreUser.class);
                            if (userScore.idus.equals(firebaseUser.getUid())) {
                                // Lấy điểm từ Firebase và hiển thị trên TextView
                                score = userScore.score;
                                txtScore.setText("Điểm hiện tại: " + score);
                                check = true;
                                if (score >= 30 && score<70) {
                                    scoreBonus += 5;
                                    txtScoreBonus.setText("Điểm thưởng: " + scoreBonus);
                                }
                                else if (score>=70 && score <1000){
                                    scoreBonus+=10;
                                    txtScoreBonus.setText("Điểm thưởng: " + scoreBonus);
                                }
                                else if (score >= 1000){
                                    scoreBonus += 50;
                                    txtScoreBonus.setText("Điểm thưởng: " + scoreBonus);
                                }else {
                                    btnNhanThuong.setVisibility(View.GONE);
                                    txtScoreBonus.setVisibility(View.GONE);
                                 }
                                break;
                            } else {
                                check = false;
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });





        btnNhanThuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!danhan) {
                    score = score + scoreBonus;
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot snapshott : snapshot.getChildren()) {
                                    ScoreUser usersc = snapshott.getValue(ScoreUser.class);
                                    if (usersc.idus.equals(firebaseUser.getUid())) {
                                        if (score > usersc.score) {
                                            usersc.setScore(score);
                                            // Thực hiện cập nhật
                                            myRef.child(snapshott.getKey()).setValue(usersc);
                                            txtScore.setText("Điểm hiện tại: "+ score);
                                        }
                                        check = true;
                                        // Ẩn nút và điểm thưởng sau khi nhận thành công
                                        btnNhanThuong.setVisibility(View.GONE);
                                        txtScoreBonus.setVisibility(View.GONE);

                                        if (check) {
                                            danhan = true;
                                            thongBaoNhanThanhCong("Nhận điểm thưởng thành công!");

                                        }
                                        break;

                                    } else {
                                        check = false;
                                    }

                                }

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
            }

        });


    }

    // Hàm hiển thị Toast
    private void thongBaoNhanThanhCong( String message) {
        Toast.makeText(Bonus.this, message, Toast.LENGTH_SHORT).show();
    }

}
