package com.example.game;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private EditText edtgmail,edtpassword;
    private Button btnsignin, SignUp;
    //private ProgressDialog progressDialog ;
    private LinearLayout forgot;
    private CheckBox check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        check = findViewById(R.id.hienmk);
        edtgmail = findViewById(R.id.edit_gmail);
        edtpassword = findViewById(R.id.edit_password);
        btnsignin = findViewById(R.id.SignIn);
        forgot = findViewById(R.id.forgot_password);
        SignUp = findViewById(R.id.SignUp);

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


        //Bấm vào trang đăng ký
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp_layout();
            }
        });

        //Validate Đăng nhập
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtgmail.getText().toString()))
                {
                    Toast.makeText(SignIn.this,
                            "không được để trống gmail",
                            Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(edtpassword.getText().toString())) {
                    Toast.makeText(SignIn.this,
                            "không được để trống mật khẩu",
                            Toast.LENGTH_SHORT).show();
                }else{
                    onClickSignIn();
                }
            }
        });

        //Quên mật khẩu
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickForgotPassword();
            }
        });

    }

    //Đăng nhập
    private void onClickSignIn() {
        String strGmail = edtgmail.getText().toString().trim();
        String strPassword = edtpassword.getText().toString().trim();
        //progressDialog.show();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(strGmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //progressDialog.dismiss();
                            Intent intent = new Intent(SignIn.this, MainActivity.class);
                            startActivity(intent);
                            //finishAffinity();

                        } else {

                            Toast.makeText(SignIn.this, "Đăng nhập thất bại",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void SignUp_layout() {
        Intent intent = new Intent(SignIn.this, SignUp.class);
        startActivity(intent);
    }

    private void OnClickForgotPassword() {
        //progressDialog.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = "hakkymenchan@gmail.com";

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //progressDialog.dismiss();
                            Toast.makeText(SignIn.this,
                                    "Đã gửi Gmail",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SignIn.this,
                                    "Gửi thất bại",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
