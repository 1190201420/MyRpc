package mlf.rpc.server;

import mlf.rpc.serializer.CommonSerializer;

public interface RpcServer {
    void start();
    <T> void publishService(Object service, Class<T> serviceClass);
    void setSerializer(CommonSerializer serializer);
}
