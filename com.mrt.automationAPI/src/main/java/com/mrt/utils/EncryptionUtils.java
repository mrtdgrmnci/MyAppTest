package com.mrt.utils;

import org.apache.commons.codec.binary.Base64;

public class EncryptionUtils {

    public static String encrypt (String property){
        String encodedStr =null;
        encodedStr= Base64.encodeBase64String(property.getBytes());

        return encodedStr;
    }

    public static String decrypt(String property){
        String decodedStr=null;
        if ((org.apache.commons.lang.StringUtils.isNotBlank(property)) && (Base64.isBase64(property)))
            decodedStr=new String(Base64.decodeBase64(property));
        return decodedStr;
    }

}
