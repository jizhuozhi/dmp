package cn.elasticj.dmp.fastjson;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cn.elasticj.dmp.lang.DmpException;
import cn.elasticj.dmp.lang.ObjectHolder;

public class JacksonObjectHandler implements ObjectHolder {

    @Override
    public Object newObject() {
        throw new DmpException("newObject unsupported");
    }

    @Override
    public Object getField(Object object, String name) {
        if (object instanceof ObjectNode) {
            return ((ObjectNode) object).get(name);
        } else {
            throw new DmpException("Unsupported class[" + object.getClass() + "]");
        }
    }

    @Override
    public void setField(Object object, String name, Object value) {
        if (object instanceof ObjectNode) {
            ((ObjectNode) object).putPOJO(name, value);
        } else {
            throw new DmpException("Unsupported class[" + object.getClass() + "]");
        }
    }

    @Override
    public Class<?>[] supports() {
        return new Class[]{ObjectNode.class};
    }
}
