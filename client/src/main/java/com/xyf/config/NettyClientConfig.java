package com.xyf.config;

import DH.DHTest;
import RSA.RSAUtils;
import Utils.fileReader;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import md5.MD5Utils;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class NettyClientConfig {
    private Map<Integer, String> keyMap;
    private String     server_public_key;
    private int     client_server_key;

    private int     client_factor;
    private int     client_signature;

    private String  check_bit;
    private String  data;


    public int getClient_server_key() {
        return this.client_server_key;
    }

    public NettyClientConfig(Map<Integer,String> map){
        keyMap = map;
        client_factor = DHTest.getRandomPrime();
    }

    public String getPublic_key(){
        return keyMap.get(0);
    }

    public String getPrivate_key(){
        return keyMap.get(1);
    }


    public void init(String filepath) throws Exception {
        this.data  = fileReader.ReadFile(filepath);
//        this.client_factor = DHTest.getRandomPrime();
    }
}
