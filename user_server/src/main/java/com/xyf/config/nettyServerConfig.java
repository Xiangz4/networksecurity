package com.xyf.config;


import DH.DHTest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ToString
public class nettyServerConfig {
    private Map<Integer, String> keyMap;
    private String  client_public_key;
    private int     client_server_key;

    private int     server_factor;
    private int     server_signature;

    public nettyServerConfig(Map<Integer,String> map){
        keyMap = map;
        this.server_factor = DHTest.getRandomPrime();
    }
    public String getPublic_key(){
        return keyMap.get(0);
    }

    public String  getPrivate_key(){
        return keyMap.get(1);
    }


}
