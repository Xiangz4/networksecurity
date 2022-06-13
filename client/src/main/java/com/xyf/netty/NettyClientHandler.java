package com.xyf.netty;

import DH.DHTest;
import RSA.RSAUtils;
import Transport.message;
import Transport.nettyTransport;
import Utils.fileReader;
import com.xyf.config.NettyClientConfig;
import com.xyf.decodeService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import md5.MD5Utils;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Objects;

/**
 *
 * Title: NettyClientHandler
 * Description: 客户端业务逻辑实现
 * @date 2017年10月8日
 */
public class NettyClientHandler extends  ChannelInboundHandlerAdapter {


    /** 客户端请求的心跳命令  */
    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("hb_request",
            CharsetUtil.UTF_8));

    /** 空闲次数 */
    private int idle_count = 1;

    /** 发送次数 */
    private int count = 1;

    /**循环次数 */
    private int fcount = 1;

    private static NettyClientConfig clientConfig;

    static {
        try {
            clientConfig = new NettyClientConfig(RSAUtils.genKeyPair());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private decodeService decodeService;



    public NettyClientHandler(decodeService decodeService){
        this.decodeService = decodeService;
    }

    /**
     * 建立连接时
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        nettyTransport object = new nettyTransport(clientConfig.getClient_factor(),
                clientConfig.getPublic_key());
        System.out.println("建立连接时："+new Date());
        ctx.writeAndFlush(object);
        ctx.fireChannelActive();
    }

    /**
     * 关闭连接时
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("关闭连接时："+new Date());
    }

    /**
     * 心跳请求处理
     * 每4秒发送一次心跳请求;
     *
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        System.out.println("循环请求的时间："+new Date()+"，次数"+fcount);
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            if (IdleState.WRITER_IDLE.equals(event.state())) {  //如果写通道处于空闲状态,就发送心跳命令
                if(idle_count <= 3){   //设置发送次数
                    idle_count++;
                    ctx.channel().writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
                }else{
                    System.out.println("不再发送心跳请求了！");
                }
                fcount++;
            }
        }
    }

    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof nettyTransport){
            System.out.println("第一次接收到服务端的信息"+msg);
            nettyTransport obj = (nettyTransport)msg;
            clientConfig.setServer_public_key(obj.getPublic_key());
            clientConfig.setClient_signature((int) (Math.pow(clientConfig.getClient_factor(),15) % obj.getFactor()));
            clientConfig.setClient_server_key((int)((long) Math.pow(clientConfig.getClient_factor(),clientConfig.getClient_signature()) % obj.getFactor()));
        }
        clientConfig.setData(fileReader.ReadFile("D:\\desktop\\tmp.txt"));

        String check_bit = RSAUtils.sign(Objects.requireNonNull(MD5Utils.MD5(clientConfig.getData())), clientConfig.getPrivate_key());
        clientConfig.setCheck_bit(check_bit);

        int key = clientConfig.getClient_server_key();
        String encrypt = decodeService.encrypt("rc4", clientConfig.getData(), key+"");
        message message = new message(encrypt, check_bit);
        ctx.writeAndFlush(message);
        count++;
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // ..
        if (cause instanceof Exception) {
            System.out.println("Exception handled.");
        }
    }
}

