package com.example.swusemiproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.swusemiproject.Database.DB;
import com.example.swusemiproject.model.MemberModel;

public class RegActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName(); // 클래스명 획득

    // 텍스트 입력 객체
    EditText editRegName, editRegId, editRegPwd, editRegPwdCheck;
    // Button 객체
    Button btnRegSave, btnRegCamera;
    // ImageView 객체
    ImageView imgRegPic;
    // DB 객체
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        // 버튼 객체 획득
        btnRegCamera = findViewById(R.id.btnRegCamera); // RegActivity 에서 '사진찍기' 버튼
        btnRegSave = findViewById(R.id.btnRegSave); // RegActivity 에서 '회원가입' 버튼
        // EditText 객체 획득
        editRegId = findViewById(R.id.editId);
        editRegName = findViewById(R.id.editRegName);
        editRegPwd = findViewById(R.id.editRegPwd);
        editRegPwdCheck = findViewById(R.id.editRegPwdCheck);
        // ImageView 객체 획득
        imgRegPic = findViewById(R.id.imgRegPic);

        // 회원가입 버튼 이벤트
        btnRegSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "id" + editRegId.getText());
                Log.d(TAG, "name"+editRegName.getText());
                Log.d(TAG, "pwd"+editRegPwd.getText());

                // 입력한 비밀번호와 비밀번호 확인이 같지 않을 때
                if(!editRegPwd.getText().equals(editRegPwdCheck.getText())) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT);

                    editRegId.setText("");
                    editRegName.setText("");
                    editRegPwd.setText("");
                    editRegPwdCheck.setText("");
                } else { // 입력한 비밀번호와 비밀번호 확인이 같을 때
                    MemberModel member = new MemberModel();

                    member.setId(editRegId.getText().toString());
                    member.setName(editRegName.getText().toString());
                    member.setPwd(editRegPwd.getText().toString());

                    db.setMember(member);
                }
            }
        });
        // 사진찍기 버튼 이벤트
        btnRegCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
    }

    // 카메라 어플리케이션 호출
    private void takePicture() {
        startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
    }
}
