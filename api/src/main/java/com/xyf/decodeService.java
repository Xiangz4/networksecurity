package com.xyf;

public interface decodeService {
    //按照给定名字的算法加密
    String encrypt(String encrypt_name,String data,String key);

    //按照给定名字的算法解密
    String decipher(String decipher_name,String encrypt_data,String key);
}
