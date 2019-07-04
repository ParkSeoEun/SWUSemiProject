package com.example.swusemiproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.swusemiproject.Database.DB;
import com.example.swusemiproject.model.MemberModel;

import java.io.File;
import java.io.IOException;

public class RegActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName(); // 클래스명 획득
    private static final int REQ_TAKE_PHOTO = 1;
    private static final int REQ_TAKE_ALBUM = 2;

    // 텍스트 입력 객체
    EditText editRegName, editRegId, editRegPwd, editRegPwdCheck;
    // Button 객체
    Button btnRegSave, btnRegCamera;
    // ImageView 객체
    ImageView imgRegPic;

    // Uri 객체
    Uri mImageUri = null;
    Uri mProviderUri = null;
    Uri mAlbumUri = null;
    String mCurrentImageFilePath = null;

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
        editRegId = findViewById(R.id.editRegId);
        editRegName = findViewById(R.id.editRegName);
        editRegPwd = findViewById(R.id.editRegPwd);
        editRegPwdCheck = findViewById(R.id.editRegPwdCheck);
        // ImageView 객체 획득
        imgRegPic = findViewById(R.id.imgRegPic);

        // 회원가입 버튼 이벤트
        btnRegSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "id" + editRegId.getText().toString());
                Log.d(TAG, "name"+editRegName.getText().toString());
                Log.d(TAG, "pwd"+editRegPwd.getText().toString());

                // 입력한 비밀번호와 비밀번호 확인이 같지 않을 때
                if(!(editRegPwd.getText().toString().equals(editRegPwdCheck.getText().toString()))) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 맞지 않습니다."
                            , Toast.LENGTH_SHORT).show();

                    editRegId.setText("");
                    editRegName.setText("");
                    editRegPwd.setText("");
                    editRegPwdCheck.setText("");
                    imgRegPic.setImageResource(R.drawable.ic_launcher_background);

                } else { // 입력한 비밀번호와 비밀번호 확인이 같을 때
                    MemberModel member = new MemberModel();

                    member.setId(editRegId.getText().toString());
                    member.setName(editRegName.getText().toString());
                    member.setPwd(editRegPwd.getText().toString());
                    member.setImgId(editRegId.getText().toString());

                    db.setMember(member);
                }
            }
        });
        // 사진찍기 버튼 이벤트
        btnRegCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });
    }
   // 카메라 어플리케이션 호출
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager())!=null) {
            File imageFile = createFileName(); // 저장할 파일
            if(imageFile != null) {
                Uri providerUri = FileProvider.getUriForFile(this, getPackageName()
                        ,imageFile);
                mProviderUri = providerUri;
                intent.putExtra(MediaStore.EXTRA_OUTPUT, providerUri);

                startActivityForResult(intent, REQ_TAKE_PHOTO);
            }
        }
    }

    // 앨범 어플리케이션 호출
    private void getAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQ_TAKE_ALBUM);
    }
    // 카메라, 앨범 등의 처리결과
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        DB db = DB.getInstance(this);

        switch(requestCode) {
            case REQ_TAKE_PHOTO:
                if(requestCode == Activity.RESULT_OK) {
                    gallaryAddPic();

                    imgRegPic.setImageURI(mProviderUri); //사진 촬영한 이미지 설정
                }
                break;
            case REQ_TAKE_ALBUM:
                if(resultCode == Activity.RESULT_OK) {
                    File albumFile = createFileName();
                    mImageUri = data.getData();
                    mAlbumUri = Uri.fromFile(albumFile);

                    imgRegPic.setImageURI(mImageUri); // 앨범에서 선택한 이미지 설정
                }
                break;
        } // End switch
    }

    // 생선한 파일을 갤러리에 추가
    private void gallaryAddPic() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        File file = new File(mCurrentImageFilePath);
        Uri contentUri = Uri.fromFile(file);
        intent.setData(contentUri);
        sendBroadcast(intent);

        Toast.makeText(this,"앨범에 사진이 추가되었습니다.", Toast.LENGTH_SHORT).show();
    }

    // 이미지 파일 생성
    private File createFileName() {
        String fileName = editRegId.getText().toString() + ".jpg";

        File myDir = new File(Environment.getExternalStorageDirectory() + "/Pictures",
                "member");
        if(!myDir.exists()) {
            myDir.mkdir();
        }

        File imageFile = new File(myDir, fileName);
        mCurrentImageFilePath = imageFile.getAbsolutePath();

        return imageFile;
    }
}

