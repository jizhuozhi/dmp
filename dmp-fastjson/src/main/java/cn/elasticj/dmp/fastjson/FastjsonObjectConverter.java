package cn.elasticj.dmp.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.elasticj.dmp.proxy.ObjectConverter;

public class FastjsonObjectConverter implements ObjectConverter {
    @Override
    public Object convert(Object o, Class<?> clazz) {
        return JSONObject.parseObject(JSON.toJSONString(o), clazz);
    }
}
