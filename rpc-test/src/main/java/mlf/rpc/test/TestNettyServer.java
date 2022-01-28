package mlf.rpc.test;

import mlf.rpc.annotation.ServiceScan;
import mlf.rpc.serializer.CommonSerializer;
import mlf.rpc.server.NettyServer;
import mlf.rpc.server.RpcServer;

@ServiceScan
public class TestNettyServer {
    public static void main(String[] args) {
        RpcServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.KRYO_SERIALIZER);
        server.start();
    }
}
