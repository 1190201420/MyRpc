package mlf.rpc.test;

import mlf.rpc.api.HelloService;
import mlf.rpc.serializer.CommonSerializer;
import mlf.rpc.serializer.KryoSerializer;
import mlf.rpc.server.NettyServer;
import mlf.rpc.server.RpcServer;

public class TestNettyServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new NettyServer("127.0.0.1", 9999, CommonSerializer.KRYO_SERIALIZER);
        rpcServer.publishService(helloService, HelloService.class);
        rpcServer.start();
    }
}
