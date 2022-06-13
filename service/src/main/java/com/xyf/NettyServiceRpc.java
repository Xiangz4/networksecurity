package com.xyf;



import com.xyf.annotation.RpcScan;
import com.xyf.config.RpcServiceConfig;
import com.xyf.decode_Impl.decodeServiceImpl;
import com.xyf.remoting.transport.netty.server.NettyRpcServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@RpcScan(basePackage = "com.xyf")
public class NettyServiceRpc {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServiceRpc.class);
        NettyRpcServer nettyRpcServer = (NettyRpcServer) applicationContext.getBean("nettyRpcServer");
        decodeServiceImpl decodeService = new decodeServiceImpl();
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("test1").version("version1").service(decodeService).build();
        nettyRpcServer.registerService(rpcServiceConfig);
        nettyRpcServer.start();
    }

}
