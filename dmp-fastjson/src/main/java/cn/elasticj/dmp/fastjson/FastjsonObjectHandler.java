package cn.elasticj.dmp.fastjson;

import com.alibaba.fastjson.JSONObject;
import cn.elasticj.dmp.lang.DmpException;
import cn.elasticj.dmp.lang.ObjectHolder;

public class FastjsonObjectHandler implements ObjectHolder {

    @Override
    public Object newObject() {
        throw new DmpException("newObject unsupported");
    }

    @Override
    public Object getField(Object object, String name) {
        if (object instanceof JSONObject) {
            return ((JSONObject) object).get(name);
        } else {
            throw new DmpException("Unsupported class[" + object.getClass() + "]");
        }
    }

    @Override
    public void setField(Object object, String name, Object value) {
        if (object instanceof JSONObject) {
            ((JSONObject) object).put(name, value);
        } else {
            throw new DmpException("Unsupported class[" + object.getClass() + "]");
        }
    }

    @Override
    public Class<?>[] supports() {
        return new Class[]{JSONObject.class};
    }
}
