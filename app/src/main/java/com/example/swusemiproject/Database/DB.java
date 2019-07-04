package com.example.swusemiproject.Database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.swusemiproject.model.MemberModel;
//import com.example.swusemiproject.model.MyItem;
import com.example.swusemiproject.model.Memo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DB {
    public final static String TBL_MEMO = "MEMO";

    private static DB inst;
    private static SharedPreferences sf = null; // 저장 객체

   private static List<Memo> memos = null; // 원본 데이터

    private DB() {}

    public static DB getInstance(Context context) {

        if(memos == null) {
            memos = new ArrayList<>();
        }

        if(sf == null) {
            sf = context.getSharedPreferences("DB", Activity.MODE_PRIVATE);
        }

        if(inst == null) {
            inst = new DB();
        }

        return inst;
    }
    // 메모 선두에 저장
    public void addMemo(Memo memo) {
        memos.add(0,memo);
    }
    // memo 획득
    public Memo getMemo(int index) {
        return memos.get(index);
    }
    // memo 변경
    public void setMemo(int index, Memo memo) {
        memos.set(index, memo);
    }
    // memo 삭제
    public void removeMemo(int index) {
        memos.remove(index);
    }
    // memeos를 SharedPreferences에 저장
    public void saveMemos() {
        // 객체를 문자열(Json)으로 변경
        String memoString = new GsonBuilder().serializeNulls().create().toJson(memos);

        // 저장
        SharedPreferences.Editor editor = sf.edit();
        editor.putString(TBL_MEMO, memoString); // key, value 형식으로 저장
        editor.commit();
    }

    // memos 획득
    public List<Memo> loadMemos() {
        // SharedPreferences의 memos 정보를 문자열로 획득
        String memoString = sf.getString(TBL_MEMO, "");
        if(!memoString.isEmpty()) {
            Memo[] memoArray = new Gson().fromJson(memoString, Memo[].class);

            // 배열을 ArrayList형태로 반환
            memos = new ArrayList<>(Arrays.asList(memoArray));
        }

        return memos;
    }

    // 회원 저장
    public void setMember(MemberModel member) {
        // member객체를 Json 형태의 문자열로 변환
        String memberString = new GsonBuilder().serializeNulls().create().toJson(member);
        Log.d("DB", "memberString"+memberString);

        // 저장
        SharedPreferences.Editor editor = sf.edit();
        editor.putString(member.getId(), memberString); // key, value 형식으로 저장
        editor.commit();
    }
    // 회원 조회
    public MemberModel getMember(String id) {
        MemberModel member = null;

        // id를 이용해 회원정보 획득
        String memberString = sf.getString(id,"");
        if (memberString !=null && memberString.length() > 0) {
            member = new Gson().fromJson(memberString, MemberModel.class);
        } else {
            Log.e("DB", "member null");
        }

        return member;
    }
    // 로그인 시 비밀번호 체크
    public boolean checkMemberLogin(String id, String pwd) {
        boolean isTrue = false;

        // 아이디나 비밀번호가 입력되지 않을 때 실패
        if(id.isEmpty() || pwd.isEmpty()) {
            return isTrue;
        }
        // 회원 획득
        MemberModel member = getMember(id);
        if(member == null) {
            return isTrue; // id에 맞는 회원이 없을 시 실패
        }

        if(member.getPwd().equals(pwd)) {
            isTrue = true; // 성공
        }
        return isTrue;
    }
}
