package mlf.rpc.client;

import mlf.rpc.entity.RpcRequest;
import mlf.rpc.serializer.CommonSerializer;

public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);

    void setSerializer(CommonSerializer serializer);
}
