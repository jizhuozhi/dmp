package cn.elasticj.dmp.proxy;

@FunctionalInterface
public interface ObjectConverter {

    <T> T convert(Object o, Class<T> clazz);

}
