package com.consumer;

import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.util.RpcRegisterEntity;
import org.util.ServiceRegisterUtil;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;

public class ConsumerProxyInvocationHandler implements InvocationHandler {
    private final Logger logger = LoggerFactory.getLogger(ConsumerProxyInvocationHandler.class);
    /**
     * 代理类的class
     */
    private Class<?> serviceClass;

    public ConsumerProxyInvocationHandler(Class<?> serviceClass) {
        this.serviceClass = serviceClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("proxy: {}", proxy.getClass().getName());
        logger.info("method: {}", method);
        logger.info("args: {}", Arrays.toString(args));
        String interfaceName = serviceClass.getName();
        String serviceObject = ServiceRegisterUtil.getProviderData(interfaceName);
        RpcRegisterEntity rpcRegisterEntity = JSON.parseObject(serviceObject, RpcRegisterEntity.class);
        logger.info("建立socket连接: {}:{}", rpcRegisterEntity.getHost(), rpcRegisterEntity.getPort());
        Socket socket = new Socket(rpcRegisterEntity.getHost(), rpcRegisterEntity.getPort());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        // 写接口类名
        objectOutputStream.writeUTF(interfaceName);
        // 发送方法名
        objectOutputStream.writeUTF(method.getName());
        // 写方法参数列表
        objectOutputStream.writeObject(method.getParameterTypes());
        // 写调用入参参数
        objectOutputStream.writeObject(args);
        // 读取返回值
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        logger.info("执行方法前，这里可以实现aop的before需求");
        Object o = objectInputStream.readObject();
        logger.info("执行方法完成，这里可以实现aop的after需求，消费者远程调用返回结果：{}", o);
        return "hello, proxyInvoke, result = " + o;
    }
}
