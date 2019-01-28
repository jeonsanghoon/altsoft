package com.altsoft.Framework.DataInfo;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;


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

        try {
            md.update(data.getBytes("UTF-8"), 0, data.length()
); // "세이프123"을 SHA-1으로 변환할 예정!
        } catch (UnsupportedEncodingException e) {
            return "";
        }
        byte bytes[] = md.digest();
        // Another way to make HEX, my previous post was only the method like your solution
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) // This is your byte[] result..
        {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public enum enSecType
    {
        SHA1, SHA256
    }
}
