package mlf.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import mlf.rpc.annotation.Service;
import mlf.rpc.annotation.ServiceScan;
import mlf.rpc.code.CommonDecoder;
import mlf.rpc.code.CommonEncoder;
import mlf.rpc.enumeration.RpcError;
import mlf.rpc.exception.RpcException;
import mlf.rpc.hook.ShutdownHook;
import mlf.rpc.registry.ServiceProviderImpl;
import mlf.rpc.registry.NacosServiceRegistry;
import mlf.rpc.registry.ServiceProvider;
import mlf.rpc.registry.ServiceRegistry;
import mlf.rpc.serializer.CommonSerializer;
import mlf.rpc.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class NettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private final String host;
    private final int port;

    private final ServiceProvider serviceProvider;
    private final ServiceRegistry serviceRegistry;
    private final CommonSerializer serializer;

    public NettyServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        serviceProvider = new ServiceProviderImpl();
        serviceRegistry = new NacosServiceRegistry();
        this.serializer = CommonSerializer.getByCode(serializer);
        scanServices();
    }

    @Override
    public void start() {
        ShutdownHook.getShutdownHook().addClearAllHook();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS))
                                    .addLast(new CommonEncoder(serializer))
                                    .addLast(new CommonDecoder())
                                    .addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("启动服务器时有错误发生：", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public <T> void publishService(Object service, String serviceName) {
        serviceProvider.addServiceProvider(service);
        serviceRegistry.register(serviceName, new InetSocketAddress(host, port));
    }

    public void scanServices() {
        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        try {
            startClass = Class.forName(mainClassName);
            if (!startClass.isAnnotationPresent(ServiceScan.class)) {
                logger.error("启动类缺少ServiceScan注解！");
                throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            logger.error("找不到相应类！");
            throw new RpcException(RpcError.UNKNOWN_ERROR);
        }
        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        if ("".equals(basePackage)) {
            basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
        }
        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);
        for (Class<?> clazz : classSet) {
            if (clazz.isAnnotationPresent(Service.class)) {
                String serviceName = clazz.getAnnotation(Service.class).name();
                Object obj;
                try {
                    obj = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("创建" + clazz + "时有错误发生");
                    continue;
                }
                if ("".equals(serviceName)) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> i : interfaces) {
                        publishService(obj, i.getCanonicalName());
                    }
                } else {
                    publishService(obj, serviceName);
                }
            }
        }
    }

}
