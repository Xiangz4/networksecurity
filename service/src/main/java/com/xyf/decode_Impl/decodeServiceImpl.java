package com.xyf.decode_Impl;

import RC4.RC4Utils;
import com.xyf.annotation.RpcService;
import com.xyf.decodeService;

import lombok.extern.slf4j.Slf4j;
import md5.MD5Utils;

import java.util.Locale;


@Slf4j
public class decodeServiceImpl implements decodeService {
    static {
        System.out.println("decode_impl被创建");
    }


    @Override
    public String encrypt(String encrypt_name,String data,String key) {
        encrypt_name = encrypt_name.toLowerCase();
        System.out.println(encrypt_name);
        String encrypt_data = null;

        if (encrypt_name.equals("md5")){
            encrypt_data = MD5Utils.MD5(data);
            return encrypt_data;
        }

        if (encrypt_name.equals("rc4")){
            System.out.println("使用RC4进行加密");
            encrypt_data = RC4Utils.encry_RC4_string(data, key);
            return encrypt_data;
        }
        System.out.println("不支持"+encrypt_name+"加密算法");

        return encrypt_data;

    }

    @Override
    public String decipher(String decipher_name,String encrypt_data,String key){
        decipher_name = decipher_name.toLowerCase();
        if (decipher_name.equals("md5")){
            MD5Utils.MD5("");
            return "";
        }
        if (decipher_name.equals("rc4")){

            return "";
        }
        System.out.println("不支持"+decipher_name+"解密算法");
        return null;
    }
}
