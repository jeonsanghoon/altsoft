package com.altsoft.Framework.DataInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SecurityInfo {
    public String ConvertSha(String data, enSecType secType)
    {
        MessageDigest md = null; // 이 부분을 SHA-256, MD5로만 바꿔주면 된다.
        try {
            if(secType == enSecType.SHA1) md = MessageDigest.getInstance("SHA-1");
            else if(secType == enSecType.SHA256) md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        md.update(data.getBytes()); // "세이프123"을 SHA-1으로 변환할 예정!

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for(int i=0; i<byteData.length; i++) {
            sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public enum enSecType
    {
        SHA1, SHA256
    }
}
