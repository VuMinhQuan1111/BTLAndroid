package com.example.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    //nut tim kiem
    Button btnTimKiemTheoTen, btnTimKiemTheoDiemChinhXac, btnTimKiemTheoKhoangDiem;

    //nut sap xep
    Button btnSapXepNguoiChoiTheoTenAZ, btnSapXepNguoiChoiTheoTenZA;
    //nut top 5
    Button btnTop5NguoiChoiDiemCaoNhat, btnTop5NguoiChoiCoDiemThapNhat;
    //nut top 1
    Button btnNguoiChoiDiemCaoNhat, btnNguoiChoiDiemThapNhat;

    EditText editTextTimKiemTheoTen, editTextTimKiemTheoDiemChinhXac, editTextDiemTu, editTextDiemDen;
    //nut sap xep diem
    Button btnSapXepDiemCaoThap, btnSapXepDiemThapCao;


    final ArrayList<String> mang = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score);
        Anhxa();
        back = findViewById(R.id.back_btn);
        //nut tim kiem
        btnTimKiemTheoTen = findViewById(R.id.btnTimKiemTheoTen);
        btnTimKiemTheoDiemChinhXac = findViewById(R.id.btnTimKiemTheoDiemChinhXac);
        btnTimKiemTheoKhoangDiem = findViewById(R.id.btnTimKiemTheoKhoangDiem);

        //nut sap xep
        btnSapXepNguoiChoiTheoTenAZ = findViewById(R.id.btnSapXepNguoiChoiTheoTenAZ);
        btnSapXepNguoiChoiTheoTenZA = findViewById(R.id.btnSapXepNguoiChoiTheoTenZA);

        //nút top 5
        btnTop5NguoiChoiDiemCaoNhat = findViewById(R.id.btnTop5NguoiChoiDiemCaoNhat);
        btnTop5NguoiChoiCoDiemThapNhat = findViewById(R.id.btnTop5NguoiChoiDiemThapNhat);

        //nut top 1
        btnNguoiChoiDiemCaoNhat = findViewById(R.id.btnNguoiChoiCaoDiemNhat);
        btnNguoiChoiDiemThapNhat = findViewById(R.id.btnNguoiChoiThapDiemNhat);
        //edit text
        editTextTimKiemTheoTen = findViewById(R.id.editTextTimKiemTheoTen);
        editTextTimKiemTheoDiemChinhXac = findViewById(R.id.editTextTimkiemTheoDiemChinhXac);
        editTextDiemTu = findViewById(R.id.editTextDiemTu);
        editTextDiemDen = findViewById(R.id.editTextDiemDen);

        //nut sap xep diem
        btnSapXepDiemCaoThap = findViewById(R.id.btnSapXepTheoDiemCaoThap) ;
        btnSapXepDiemThapCao = findViewById(R.id.btnSapXepTheoDiemThapCao);





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HighScore.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //nút tìm kiếm theo tên người dùng
        btnTimKiemTheoTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = editTextTimKiemTheoTen.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    searchByName(searchText);
                } else {
                    // Nếu ô tìm kiếm trống, hiển thị toàn bộ danh sách
                    updateListView(mang);
                }
            }
        });

        //nút tìm kiếm theo điểm người dùng
        btnTimKiemTheoDiemChinhXac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchScoreText = editTextTimKiemTheoDiemChinhXac.getText().toString().trim();
                if (!searchScoreText.isEmpty()) {
                    searchByScore(searchScoreText);
                } else {
                    // Nếu ô tìm kiếm trống, hiển thị toàn bộ danh sách
                    updateListView(mang);
                }
            }
        });
        //nút tìm kiếm theo khoảng điểm người dùng
        btnTimKiemTheoKhoangDiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String diemTuText = editTextDiemTu.getText().toString().trim();
                String diemDenText = editTextDiemDen.getText().toString().trim();

                if (!diemTuText.isEmpty() && !diemDenText.isEmpty()) {
                    searchByScoreRange(diemTuText, diemDenText);
                } else {
                    // Nếu ô tìm kiếm trống, hiển thị toàn bộ danh sách
                    updateListView(mang);
                }
            }
        });


        //nút sắp xếp người chơi theo tên từ a - z
        btnSapXepNguoiChoiTheoTenAZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortPlayerNamesAZ();
            }
        });

        //nút sắp xếp người chơi theo tên từ z - a
        btnSapXepNguoiChoiTheoTenZA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortPlayerNamesZA();
            }
        });
        //nut top 5 người choi điểm cao nhất
        btnTop5NguoiChoiDiemCaoNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTop5Players();
            }
        });

        //nut top 5 người chơi điểm thấp nhất
        btnTop5NguoiChoiCoDiemThapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTop5PlayersWithLowestScore();
            }
        });
        //nut top 1 người chơi có điểm cao nhất
        btnNguoiChoiDiemCaoNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlayerWithHighestScore();
            }
        });

        //nut top 1 người chơi có điểm thấp nhất
        btnNguoiChoiDiemThapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlayerWithLowestScore();
            }
        });

        //nut sap xep diem tu cao xuong thap
        btnSapXepDiemCaoThap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortPlayerScoresHighToLow();
            }
        });
        //sap xep diem thap cao
        btnSapXepDiemThapCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortPlayerScoresLowToHigh();
            }
        });
        //cau hinh list view

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
                        String item = name + " : " + score;
                        tempList.add(item);
                    }
                }

                // Sort the temporary list based on scores
                Collections.sort(tempList, new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        int score1 = Integer.parseInt(s1.split(" : ")[1].trim());
                        int score2 = Integer.parseInt(s2.split(" : ")[1].trim());
                        return Integer.compare(score2, score1);
                    }
                });

                // Populate the final list with sorted items and display the rank
                for (int i = 0; i < tempList.size(); i++) {
                    String item = (i + 1) + ". " + tempList.get(i);
                    mang.add(item);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
    //Tìm kiếm theo tên người dùng
    private void searchByName(String name) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mang.clear();
                ArrayList<String> tempList = new ArrayList<>(); // Temporary list for sorting
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.hasChild("name") && snapshot.hasChild("score")) {
                        String userName = snapshot.child("name").getValue().toString();
                        if (userName.toLowerCase().contains(name.toLowerCase())) {
                            // Nếu tìm thấy tên trong danh sách, thêm vào danh sách tạm thời
                            String score = snapshot.child("score").getValue().toString();
                            String item = userName + " : " + score;
                            tempList.add(item);
                        }
                    }
                }

                // Cập nhật danh sách hiển thị với nội dung của tempList
                mang.addAll(tempList);

                // Cập nhật ListView để hiển thị kết quả tìm kiếm
                updateListView(mang);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi
            }
        });
    }


    private void updateListView(ArrayList<String> dataList) {
        // Cập nhật danh sách hiển thị
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                dataList);
        listScore.setAdapter(adapter);
    }

    //Tìm kiếm theo điểm người dùng
    private void searchByScore(String score) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mang.clear();
                ArrayList<String> tempList = new ArrayList<>(); // Temporary list for sorting
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.hasChild("name") && snapshot.hasChild("score")) {
                        String userScore = snapshot.child("score").getValue().toString();
                        if (userScore.equals(score)) {
                            // Nếu tìm thấy điểm trong danh sách, thêm vào danh sách tạm thời
                            String userName = snapshot.child("name").getValue().toString();
                            String item = userName + " : " + userScore;
                            tempList.add(item);
                        }
                    }
                }

                // Cập nhật danh sách hiển thị với nội dung của tempList
                mang.addAll(tempList);

                // Cập nhật ListView để hiển thị kết quả tìm kiếm
                updateListView(mang);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi
            }
        });
    }

    //tìm kiếm theo khoảng đieerm người dùng
    private void searchByScoreRange(String diemTu, String diemDen) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mang.clear();
                ArrayList<String> tempList = new ArrayList<>(); // Temporary list for sorting
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.hasChild("name") && snapshot.hasChild("score")) {
                        String userScore = snapshot.child("score").getValue().toString();
                        int diemTuValue = Integer.parseInt(diemTu);
                        int diemDenValue = Integer.parseInt(diemDen);
                        int userScoreValue = Integer.parseInt(userScore);

                        if (userScoreValue >= diemTuValue && userScoreValue <= diemDenValue) {
                            // Nếu tìm thấy điểm trong khoảng, thêm vào danh sách tạm thời
                            String userName = snapshot.child("name").getValue().toString();
                            String item = userName + " : " + userScore;
                            tempList.add(item);
                        }
                    }
                }

                // Cập nhật danh sách hiển thị với nội dung của tempList
                mang.addAll(tempList);

                // Cập nhật ListView để hiển thị kết quả tìm kiếm
                updateListView(mang);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi
            }
        });
    }

    //sap xep nguoi choi theo ten a-z
    private void sortPlayerNamesAZ() {
        Collections.sort(mang, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                // Sử dụng regex để tìm và so sánh tên người chơi
                String name1 = s1.replaceAll("[^a-zA-Z]", "").trim();
                String name2 = s2.replaceAll("[^a-zA-Z]", "").trim();
                return name1.compareToIgnoreCase(name2);
            }
        });

        updateListView(mang);
    }

    //sap xep nguoi choi theo ten z-a
    private void sortPlayerNamesZA() {
        Collections.sort(mang, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                // Sử dụng regex để tìm và so sánh tên người chơi
                String name1 = s1.replaceAll("[^a-zA-Z]", "").trim();
                String name2 = s2.replaceAll("[^a-zA-Z]", "").trim();
                return name2.compareToIgnoreCase(name1); // Đổi chỗ name1 và name2 để sắp xếp từ Z đến A
            }
        });

        updateListView(mang);
    }

    //code hiển thị top 5 người chơi có điểm cao nhất
    // Hàm hiển thị top 5 người chơi có điểm cao nhất
    private void showTop5Players() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mang.clear();
                ArrayList<String> tempList = new ArrayList<>(); // Danh sách tạm thời để lưu top 5

                // Thêm tất cả người chơi vào danh sách tạm thời
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.hasChild("name") && snapshot.hasChild("score")) {
                        String name = snapshot.child("name").getValue().toString();
                        String score = snapshot.child("score").getValue().toString();
                        String item = name + " : " + score;
                        tempList.add(item);
                    }
                }

                // Sắp xếp danh sách tạm thời theo điểm giảm dần
                Collections.sort(tempList, new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        int score1 = Integer.parseInt(s1.split(" : ")[1].trim());
                        int score2 = Integer.parseInt(s2.split(" : ")[1].trim());
                        return Integer.compare(score2, score1);
                    }
                });

                // Hiển thị chỉ top 5 người chơi
                int count = Math.min(tempList.size(), 5);
                for (int i = 0; i < count; i++) {
                    String item = (i + 1) + ". " + tempList.get(i);
                    mang.add(item);
                }

                // Cập nhật danh sách hiển thị
                updateListView(mang);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi
            }
        });
    }

    //code top 5 người điểm chơi thấp nhất
    private void showTop5PlayersWithLowestScore() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mang.clear();
                ArrayList<String> tempList = new ArrayList<>(); // Danh sách tạm thời để lưu top 5

                // Thêm tất cả người chơi vào danh sách tạm thời
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.hasChild("name") && snapshot.hasChild("score")) {
                        String name = snapshot.child("name").getValue().toString();
                        String score = snapshot.child("score").getValue().toString();
                        String item = name + " : " + score;
                        tempList.add(item);
                    }
                }

                // Sắp xếp danh sách tạm thời theo điểm tăng dần
                Collections.sort(tempList, new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        int score1 = Integer.parseInt(s1.split(" : ")[1].trim());
                        int score2 = Integer.parseInt(s2.split(" : ")[1].trim());
                        return Integer.compare(score1, score2);
                    }
                });

                // Hiển thị chỉ top 5 người chơi
                int count = Math.min(tempList.size(), 5);
                for (int i = 0; i < count; i++) {
                    String item = (i + 1) + ". " + tempList.get(i);
                    mang.add(item);
                }

                // Cập nhật danh sách hiển thị
                updateListView(mang);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi
            }
        });
    }

    // Hàm hiển thị người chơi có điểm cao nhất
    private void showPlayerWithHighestScore() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mang.clear();

                String highestScorePlayer = ""; // Biến lưu thông tin người chơi có điểm cao nhất
                int highestScore = Integer.MIN_VALUE; // Biến lưu điểm cao nhất

                // Tìm người chơi có điểm cao nhất
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.hasChild("name") && snapshot.hasChild("score")) {
                        String name = snapshot.child("name").getValue().toString();
                        String scoreString = snapshot.child("score").getValue().toString();
                        int score = Integer.parseInt(scoreString);

                        if (score > highestScore) {
                            highestScore = score;
                            highestScorePlayer = name + " : " + scoreString;
                        }
                    }
                }

                // Hiển thị thông tin người chơi có điểm cao nhất
                if (!highestScorePlayer.isEmpty()) {
                    mang.add(highestScorePlayer);
                }

                // Cập nhật danh sách hiển thị
                updateListView(mang);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi
            }
        });
    }

    // Hàm hiển thị người chơi có điểm thấp nhất
    private void showPlayerWithLowestScore() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mang.clear();

                String lowestScorePlayer = ""; // Biến lưu thông tin người chơi có điểm thấp nhất
                int lowestScore = Integer.MAX_VALUE; // Biến lưu điểm thấp nhất

                // Tìm người chơi có điểm thấp nhất
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.hasChild("name") && snapshot.hasChild("score")) {
                        String name = snapshot.child("name").getValue().toString();
                        String scoreString = snapshot.child("score").getValue().toString();
                        int score = Integer.parseInt(scoreString);

                        if (score < lowestScore) {
                            lowestScore = score;
                            lowestScorePlayer = name + " : " + scoreString;
                        }
                    }
                }

                // Hiển thị thông tin người chơi có điểm thấp nhất
                if (!lowestScorePlayer.isEmpty()) {
                    mang.add(lowestScorePlayer);
                }

                // Cập nhật danh sách hiển thị
                updateListView(mang);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi
            }
        });
    }

    // Phương thức sắp xếp điểm từ cao xuống thấp
    private void sortPlayerScoresHighToLow() {
        Collections.sort(mang, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                int score1 = Integer.parseInt(s1.split(" : ")[1].trim());
                int score2 = Integer.parseInt(s2.split(" : ")[1].trim());
                return Integer.compare(score2, score1);
            }
        });

        updateListView(mang);
    }

    // Phương thức sắp xếp điểm từ thấp lên cao
    private void sortPlayerScoresLowToHigh() {
        Collections.sort(mang, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                int score1 = Integer.parseInt(s1.split(" : ")[1].trim());
                int score2 = Integer.parseInt(s2.split(" : ")[1].trim());
                return Integer.compare(score1, score2);
            }
        });

        updateListView(mang);
    }
    public void Anhxa() {
        listScore = findViewById(R.id.listView);
    }
}