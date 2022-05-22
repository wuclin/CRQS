package com.provider;

import org.annotation.RpcProvider;
import org.proccess.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.util.RpcRegisterEntity;
import org.util.ServiceRegisterUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Set;

import com.alibaba.fastjson.JSON;


public class Provider {
    private static final Logger logger = LoggerFactory.getLogger(Provider.class);
    private static final int port = 9999;

    public static void main(String[] args) {
        try {

            ServerSocket serverSocket = new ServerSocket(port);
            //初始化，这里传入了Provider类
            ClassScanner.init(Provider.class);
            //
            initServiceProvider();
            while (true) {

                logger.info("服务提供者已启动，等待连接中……");
                Socket accept = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(accept.getInputStream());
                // 读取类名
                String interfaceName = objectInputStream.readUTF();
                String methodName = objectInputStream.readUTF();
                // 读取方法名
                Class<?>[] parameterTypes = ( Class<?>[])objectInputStream.readObject();
                // 读取方法调用入参
                Object[] parameters = (Object[])objectInputStream.readObject();
                String serviceObject = ServiceRegisterUtil.getProviderData(interfaceName);
                System.out.println("\"方法信息：接口名称 = {}，方法名={}，参数列表 = {}，入参 = {}\", interfaceName, methodName, Arrays.toString(parameterTypes), Arrays.toString(parameters)");
               // logger.info("方法信息：接口名称 = {}，方法名={}，参数列表 = {}，入参 = {}", interfaceName, methodName, Arrays.toString(parameterTypes), Arrays.toString(parameters));
                RpcRegisterEntity rpcRegisterEntity = JSON.parseObject(serviceObject, RpcRegisterEntity.class);
                Class<?> aClass = Class.forName(rpcRegisterEntity.getServiceImplClassFullName());
                Method method = aClass.getMethod(methodName, parameterTypes);
                Object invoke = method.invoke(aClass.newInstance(), parameters);
                // 回写返回值
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(accept.getOutputStream());
              //  logger.info("方法调用结果：{}", invoke);
                objectOutputStream.writeObject(invoke);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static void initServiceProvider() throws UnknownHostException {
        //获得类集合
        Set<Class> classSet = ClassScanner.getClassSet();
        //得到当前主机的地址
        String host = InetAddress.getLocalHost().getHostAddress();
        classSet.forEach(c -> {
            //遍历类，然后查看有RpcProvider注解的类
            Annotation annotation = c.getAnnotation(RpcProvider.class);
            if (Objects.nonNull(annotation)) {
                //获取该类实现的接口
                //这里就是获取HelloService接口
                Class[] interfaces = c.getInterfaces();
                //获取接口名字
                String interfaceName = interfaces[0].getName();

                RpcRegisterEntity rpcRegisterEntity = new RpcRegisterEntity(interfaceName, host, port).setServiceImplClassFullName(c.getName());
                ServiceRegisterUtil.registerProvider(rpcRegisterEntity);
                logger.info(JSON.toJSONString(rpcRegisterEntity));
            }
        });
    }
}

