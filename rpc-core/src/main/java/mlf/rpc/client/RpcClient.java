package mlf.rpc.client;

import mlf.rpc.entity.RpcRequest;
import mlf.rpc.serializer.CommonSerializer;

public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    Object sendRequest(RpcRequest rpcRequest);
}
