package cn.elasticj.dmp.proxy;

import java.lang.reflect.Proxy;

public final class DmpProxy {

    private DmpProxy() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clazz, ObjectConverter objectConverter) {
        DmpInvocationHandler handler = new DmpInvocationHandler(clazz, objectConverter);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, handler);
    }
}
