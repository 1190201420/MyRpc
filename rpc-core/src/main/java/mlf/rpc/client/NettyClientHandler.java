package mlf.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import mlf.rpc.entity.RpcResponse;
import mlf.rpc.factory.SingletonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    private final UnprocessedRequests unprocessedRequests;

    public NettyClientHandler() {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        try {
            logger.info("客户端接收到消息：{}", msg);
            unprocessedRequests.complete(msg);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        logger.error("处理过程调用时有错误发生：");
        cause.printStackTrace();
        ctx.close();
    }

}
