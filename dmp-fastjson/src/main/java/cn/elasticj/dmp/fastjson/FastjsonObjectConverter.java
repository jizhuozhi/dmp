package cn.elasticj.dmp.fastjson;

import cn.elasticj.dmp.proxy.ObjectConverter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FastjsonObjectConverter implements ObjectConverter {
    @Override
    public <T> T convert(Object o, Class<T> clazz) {
        return JSONObject.parseObject(JSON.toJSONString(o), clazz);
    }
}
