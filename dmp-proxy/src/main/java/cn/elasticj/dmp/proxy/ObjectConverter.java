package cn.elasticj.dmp.proxy;

@FunctionalInterface
public interface ObjectConverter {

    Object convert(Object o, Class<?> clazz);

}
