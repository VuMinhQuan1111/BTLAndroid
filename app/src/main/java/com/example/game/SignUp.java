package com.example.game;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    private EditText edtgmail,edtpassword,edtname;
    private EditText fullname,edtpassword2;
    private Button btnsignup;
    private TextView layoutSignin;
    private CheckBox check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        check = findViewById(R.id.hienmk);
        edtgmail = findViewById(R.id.GmailSU);
        edtpassword = findViewById(R.id.PasswordSU);
        edtname=findViewById(R.id.Nameus);
        //edtpassword2 = findViewById(R.id.PasswordSU2);
        btnsignup = findViewById(R.id.SignUp);
        layoutSignin = findViewById(R.id.layout_signin);

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(check.isChecked()){
                    edtpassword.setTransformationMethod(null);
                }else{
                    edtpassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
        layoutSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });

        initListener();
    }
    private void initListener() {
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtname.getText().toString();
                String gmail = edtgmail.getText().toString();
                String password = edtpassword.getText().toString();
                if (TextUtils.isEmpty(edtgmail.getText().toString()))
                {
                    Toast.makeText(SignUp.this,
                            "không được để trống gmail",
                            Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(edtpassword.getText().toString())) {
                    Toast.makeText(SignUp.this,
                            "không được để trống mật khẩu",
                            Toast.LENGTH_SHORT).show();
                }

//                else if(TextUtils.isEmpty(edtpassword2.getText().toString())) {
//                    Toast.makeText(SignUp.this,
//                            "không được để trống xác nhận mật khẩu",
//                            Toast.LENGTH_SHORT).show();
//                }else if(!edtpassword2.getText().toString().equals(edtpassword.getText().toString())) {
//                    Toast.makeText(SignUp.this,
//                            "Xác nhận mật khẩu phải giống mật khẩu",
//                            Toast.LENGTH_SHORT).show();
//                }
                else
                {
                    //onClickSignUp();
                    Toast.makeText(SignUp.this,"Thành công",Toast.LENGTH_LONG).show();
                }
//                boolean check = validateInfo(name, gmail, password);
//                if(check == true){
//                    //Toast.makeText(SignUp.this,"Thành công",Toast.LENGTH_LONG).show();
//                    onClickSignUp();
//                }else{
//                    Toast.makeText(SignUp.this,"Đăng nhập thất bại",Toast.LENGTH_LONG).show();
//                }
            }
        });
    }

//    private boolean validateInfo(String name, String gmail, String password){
//        if(name.length() == 0){
//            edtname.requestFocus();
//            Toast.makeText(SignUp.this,"Tên không để trống",Toast.LENGTH_LONG).show();
//            return false;
//        }
//
//        else if(gmail.length() == 0){
//            edtgmail.requestFocus();
//            Toast.makeText(SignUp.this,"Gmail không để trống",Toast.LENGTH_LONG).show();
//            return false;
//        }
//        else if(!gmail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
//            edtgmail.requestFocus();
//            Toast.makeText(SignUp.this,"Gmail không hợp lệ",Toast.LENGTH_LONG).show();
//            return false;
//        }else if(password.length() == 0){
//            edtpassword.requestFocus();
//            Toast.makeText(SignUp.this,"Password không để trống",Toast.LENGTH_LONG).show();
//            return false;
//        }else if(!password.matches("(.*[0-9].*)")){
//            edtpassword.requestFocus();
//            Toast.makeText(SignUp.this,"Password cần 1 số",Toast.LENGTH_LONG).show();
//            return false;
//        }
//        else if(!password.matches("(.*[a-z].*)")){
//            edtpassword.requestFocus();
//            Toast.makeText(SignUp.this,"Password cần 1 chữ thường",Toast.LENGTH_LONG).show();
//            return false;
//        }else if(!password.matches("(.*[A-Z].*)")){
//            edtpassword.requestFocus();
//            Toast.makeText(SignUp.this,"Password cần 1 chữ hoa",Toast.LENGTH_LONG).show();
//            return false;
//        }else if(!password.matches("(.*[!@#$%^&*()_+=\\[{\\]};].*)")){
//            edtpassword.requestFocus();
//            Toast.makeText(SignUp.this,"Password cần 1 ký tự đặc biệt",Toast.LENGTH_LONG).show();
//            return false;
//        }
////        else if(!password.matches("^0\\d{9}$")){
////            edtpassword.requestFocus();
////            Toast.makeText(SignUp.this,"Số điện thoại chưa đúng",Toast.LENGTH_LONG).show();
////            return false;
////        }
//            return true;
//    }


    private void onClickSignUp() {
        String strGmail = edtgmail.getText().toString().trim();
        String strPassword = edtpassword.getText().toString().trim();
        String strname=edtname.getText().toString().trim();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
//        Toast.makeText(SignUp.this, "currentUser" + currentUser, Toast.LENGTH_LONG).show();
//        Log.e("test", "1"+ currentUser);
        auth.createUserWithEmailAndPassword(strGmail, strPassword)
                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            String userId = user.getUid();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRefe = database.getReference("Score");

                            String userIdSc = myRefe.push().getKey();
                            // creating user object
                            ScoreUser usersc = new ScoreUser(userId,strname,0);

                            // pushing user to 'users' node using the userId
                            myRefe.child(userIdSc).setValue(usersc);

                            Log.e("test", "1");
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUp.this, "Đăng ký thành công",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUp.this, "Đăng ký thất bại",
                                    Toast.LENGTH_SHORT).show();
                            Log.e("test", "2" + task.getException());

                        }
                    }
                });
    }
}
