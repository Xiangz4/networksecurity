package com.xyf;

import RSA.RSAUtils;
import Transport.message;
import com.xyf.annotation.RpcReference;
import com.xyf.config.NettyClientConfig;
import com.xyf.netty.NettyClientHandler;
import com.xyf.netty.NettyClientServer;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.sound.midi.Track;
import java.io.IOException;

@Component
public class clientController {
    public static message message;
    public message getMessage(){
        return message;
    }


    @RpcReference(version = "version1",group = "test1")
    private decodeService decodeService;

    public void send() throws Exception {
        //启动服务端
        NettyClientServer.start(decodeService);
    }
}
