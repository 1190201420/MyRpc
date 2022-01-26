package mlf.rpc.test;

import mlf.rpc.api.HelloObject;
import mlf.rpc.api.HelloService;
import mlf.rpc.client.RpcClientProxy;

public class TestClient {

    /*public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject helloObject = new HelloObject(12, "mlf is playing...");
        String res = helloService.hello(helloObject);
        System.out.println(res);
    }*/
}
