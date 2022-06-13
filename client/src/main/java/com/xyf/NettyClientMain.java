package com.xyf;


import RSA.RSAUtils;
import com.xyf.annotation.RpcScan;
import com.xyf.config.NettyClientConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@RpcScan(basePackage = {"com.xyf"})
public class NettyClientMain {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientMain.class);
        clientController clientController = (com.xyf.clientController)applicationContext.getBean("clientController");
        clientController.send();

    }

}
