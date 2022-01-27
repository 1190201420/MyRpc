package mlf.rpc.test;

import mlf.rpc.api.HelloObject;
import mlf.rpc.api.HelloService;
import mlf.rpc.client.NettyClient;
import mlf.rpc.client.RpcClient;
import mlf.rpc.client.RpcClientProxy;

public class TestNettyClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject helloObject = new HelloObject(12, "mlf is so nb");
        String res = helloService.hello(helloObject);
        System.out.println(res);
    }
}
