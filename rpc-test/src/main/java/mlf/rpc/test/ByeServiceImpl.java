package mlf.rpc.test;

import mlf.rpc.annotation.Service;
import mlf.rpc.api.ByeService;

@Service
public class ByeServiceImpl implements ByeService {

    @Override
    public String bye(String name) {
        return "bye" + name;
    }

}
