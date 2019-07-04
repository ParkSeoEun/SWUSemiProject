package com.example.swusemiproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.swusemiproject.Database.DB;

import java.time.Instant;

public class MainActivity extends AppCompatActivity {

    EditText editId;
    EditText editPwd;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editId = findViewById(R.id.editId);
        editPwd = findViewById(R.id.editPwd);

        // 회원가입
        Button btnReg = findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegActivity.class));
            }
        });
        // 로그인
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = db.checkMemberLogin(editId.getText().toString()
                        , editPwd.getText().toString());
                if(check == false) {
                    Toast.makeText(getApplicationContext(), "로그인 실패"
                    ,Toast.LENGTH_SHORT).show();

                    editId.setText("");
                    editPwd.setText("");
                } else {
                    startActivity(new Intent(getApplicationContext(), AfterLogActivity.class));
                }
            }
        });
    }
}
