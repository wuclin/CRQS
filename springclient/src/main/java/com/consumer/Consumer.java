package com.consumer;

import com.service.HelloService;
import org.annotation.RpcClient;
import org.annotation.RpcComponentScan;
import org.annotation.RpcConsumer;
import org.proccess.ClassScanner;
import org.proccess.RpcClientContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.util.RpcRegisterEntity;
import org.util.ServiceRegisterUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RpcConsumer
@RpcComponentScan("io.github.syske.rpc.consumer")
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
    @RpcClient
    private HelloService helloService;

    public static void main(String[] args) throws UnknownHostException {
        initServiceConsumer();
        Map<Class, Object> rpcClientContentMap = RpcClientContentHandler.getRpcClientContentMap();
        Consumer consumer = (Consumer)rpcClientContentMap.get(Consumer.class);
        String syske = consumer.helloService.sayHello("syske");
        logger.info("消费者远程调用返回结果：{}", syske);
    }

    private static void initServiceConsumer() throws UnknownHostException {
        ClassScanner.init(Consumer.class);
        Set<Class> classSet = ClassScanner.getClassSet();
        String host = InetAddress.getLocalHost().getHostAddress();
        classSet.forEach(c -> {
            Field[] declaredFields = c.getDeclaredFields();
            for (Field field : declaredFields) {
                RpcClient annotation = field.getAnnotation(RpcClient.class);
                if (Objects.nonNull(annotation)) {
                    Class<?> fieldType = field.getType();
                    String name = fieldType.getName();
                    RpcRegisterEntity rpcRegisterEntity = new RpcRegisterEntity();
                    rpcRegisterEntity.setHost(host).setInteraceClassFullName(name);
                    ServiceRegisterUtil.registerConsumer(rpcRegisterEntity);
                    Object proxyInstance = getProxyInstance(fieldType);
                    try {
                        Object consumer = c.newInstance();
                        field.set(consumer, proxyInstance);
                        RpcClientContentHandler.initRpcClientContent(c, consumer);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 获取动态代理实例
     * @param tClass
     * @param <T>
     * @return
     */
    private static <T> T getProxyInstance(Class<T> tClass) {
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(),
                new Class[] {tClass}, new ConsumerProxyInvocationHandler(tClass));
    }

}

