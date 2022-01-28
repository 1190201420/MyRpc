package mlf.rpc.test;

import mlf.rpc.annotation.Service;
import mlf.rpc.api.HelloObject;
import mlf.rpc.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class HelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到：{}", object.getMessage());
        return "这是用掉的返回值，id = " + object.getId();
    }
}
