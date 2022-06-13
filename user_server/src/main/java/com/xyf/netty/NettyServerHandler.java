package com.xyf.netty;

import RC4.RC4Utils;
import RSA.RSAUtils;
import Transport.message;
import Transport.nettyTransport;
import com.xyf.config.nettyServerConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import md5.MD5Utils;


/**
 *
 * Title: HelloServerHandler
 * Description:  服务端业务逻辑
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    public nettyServerConfig serverConfig;
    /** 空闲次数 */
    private int idle_count =1;
    /** 发送次数 */
    private int count = 1;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        serverConfig = new nettyServerConfig(RSAUtils.genKeyPair());
        nettyTransport server_object = new nettyTransport(serverConfig.getServer_factor(),
                serverConfig.getPublic_key());
        ctx.writeAndFlush(server_object);
    }

    /**
     * 超时处理
     * 如果5秒没有接受客户端的心跳，就触发;
     * 如果超过两次，则直接关闭;
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            if (IdleState.READER_IDLE.equals(event.state())) {  //如果读通道处于空闲状态，说明没有接收到心跳命令
                System.out.println("已经5秒没有接收到客户端的信息了");
                if (idle_count > 2) {
                    System.out.println("关闭这个不活跃的channel");
                    ctx.channel().close();
                }
                idle_count++;
            }
        } else {
            super.userEventTriggered(ctx, obj);
        }
    }

    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("第"+count+"次"+",服务端接受的消息:"+msg);
        if (msg instanceof nettyTransport) {
            nettyTransport obj = (nettyTransport) msg;
            serverConfig.setClient_public_key(obj.getPublic_key());
            serverConfig.setServer_signature((int) (Math.pow(obj.getFactor(), 27) % obj.getFactor()));
            serverConfig.setClient_server_key((int) ((long) Math.pow(obj.getFactor(), serverConfig.getServer_signature()) % serverConfig.getServer_factor()));
            System.out.println(serverConfig.toString());
        }
        if (msg instanceof message){
            message obj = (message) msg;
            String clientmsg = RC4Utils.decry_RC4(obj.getData(), serverConfig.getClient_server_key() + "");
            if ( !RSAUtils.verify(MD5Utils.MD5(clientmsg),serverConfig.getClient_public_key(),obj.getCheck_bit())){
                System.out.println("数据被篡改");
            }
            System.out.println(clientmsg);
        }

//        String message = (String) msg;
//        if ("hb_request".equals(message)) {  //如果是心跳命令，则发送给客户端;否则什么都不做
//            ctx.write("服务端成功收到心跳信息");
//            ctx.flush();
//        }
        count++;
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

