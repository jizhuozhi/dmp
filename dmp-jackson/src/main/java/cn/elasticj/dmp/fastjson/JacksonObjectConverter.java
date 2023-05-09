package cn.elasticj.dmp.fastjson;

import com.fasterxml.jackson.databind.ObjectMapper;
import cn.elasticj.dmp.proxy.ObjectConverter;

public class JacksonObjectConverter implements ObjectConverter {

    private static final ObjectMapper DEFAULT = new ObjectMapper();

    private final ObjectMapper objectMapper;

    public JacksonObjectConverter() {
        this.objectMapper = DEFAULT;
    }

    public JacksonObjectConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object convert(Object o, Class<?> clazz) {
        return objectMapper.convertValue(o, clazz);
    }
}
