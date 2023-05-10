package cn.elasticj.dmp.fastjson;

import cn.elasticj.dmp.proxy.ObjectConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    public <T> T convert(Object o, Class<T> clazz) {
        return objectMapper.convertValue(o, clazz);
    }
}
