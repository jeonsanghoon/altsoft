package com.altsoft.Framework.DataInfo;

import android.widget.Toast;

import com.altsoft.Framework.Global;

import java.util.regex.Pattern;

public class ValidityCheck {
    //이메일형식체크
    public Boolean Email(String email) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(Global.getCurrentActivity(), "이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }

    ///모바일폰 유효성 검사
    public Boolean MobilePhone(String phoneNum) {

        if (!Pattern.matches("^01(?:0|1|[6-9]) - (?:\\d{3}|\\d{4}) - \\d{4}$", phoneNum)) {
            Toast.makeText(Global.getCurrentActivity(), "올바른 핸드폰 번호가 아닙니다.", Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }
    ///비밀번호 유효성 검사
    public Boolean Password(String pw)
    {
        if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$", pw))
        {
            Toast.makeText(Global.getCurrentActivity(),"비밀번호 형식(최소 8자리에 숫자, 문자, 특수문자 각각 1개 이상 포함)을 지켜주세요.",Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }

    public Boolean PasswordConfirm(String pw, String pw2)
    {

        if(!pw.equals(pw2))
        {
            Toast.makeText(Global.getCurrentActivity(),"비밀번호번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(pw.length()>0 && pw.equals(pw2))
        {
            Toast.makeText(Global.getCurrentActivity(),"비밀번호번호가 정상적으로 입력되었습니다.",Toast.LENGTH_SHORT).show();
            return true;
        }
        return true;
    }


    public  boolean isOnline() {
        CheckConnect cc = new CheckConnect("http://clients3.google.com/generate_204");
        cc.start();
        try{
            cc.join();
            return cc.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
