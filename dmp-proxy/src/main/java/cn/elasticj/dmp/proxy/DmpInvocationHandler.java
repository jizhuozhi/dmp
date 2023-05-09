package cn.elasticj.dmp.proxy;

import cn.elasticj.dmp.lang.Bytecode;
import cn.elasticj.dmp.lang.DmpCompiler;
import cn.elasticj.dmp.lang.DmpInterpreter;

import java.io.StringReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DmpInvocationHandler implements InvocationHandler {

    private final Map<Method, Bytecode[]> cache = new HashMap<>();

    private final DmpCompiler compiler = new DmpCompiler();

    private final ObjectConverter objectConverter;

    public DmpInvocationHandler(Class<?> clazz, ObjectConverter objectConverter) {
        this.objectConverter = objectConverter;
        scanClassMethods(clazz);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Bytecode[] bytecodes = cache.get(method);
        DmpInterpreter interpreter = new DmpInterpreter();
        Object result;
        if (args.length == 1) {
            result = interpreter.run(args[0], bytecodes);
        } else {
            result = interpreter.run(null, bytecodes);
        }

        return objectConverter.convert(result, method.getReturnType());
    }

    private void scanClassMethods(Class<?> clazz) {
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("Class " + clazz + " is not interface");
        }

        Method[] methods = clazz.getDeclaredMethods();
        Map<String, Bytecode[]> scanningCache = new HashMap<>();

        for (Method method : methods) {
            if (method.isDefault()) {
                continue;
            }

            if (method.getParameterCount() > 1) {
                throw new IllegalArgumentException("Method " + method + " has more than 1 parameters");
            }

            if (method.getReturnType() == Void.class) {
                throw new IllegalArgumentException("Method " + method + "cannot return void");
            }

            Dmp dmp = method.getAnnotation(Dmp.class);
            if (dmp == null) {
                throw new IllegalArgumentException("Method " + method + " is not annotated with @Dmp");
            }

            String script = dmp.value();
            Bytecode[] bytecodes = scanningCache.computeIfAbsent(script, s -> compiler.compile(new StringReader(s)));
            cache.put(method, bytecodes);
        }
    }
}
