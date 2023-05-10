package cn.elasticj.dmp.proxy;

import cn.elasticj.dmp.lang.DmpCompiler;
import cn.elasticj.dmp.lang.DmpDefinition;
import cn.elasticj.dmp.lang.DmpInterpreter;

import java.io.StringReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class DmpInvocationHandler implements InvocationHandler {

    private final Map<Method, DmpDefinition> cache = new HashMap<>();

    private final Map<Method, String[]> parameterSymbolsCache = new HashMap<>();

    private final Map<Method, Integer> originIndexCache = new HashMap<>();

    private final DmpCompiler compiler = new DmpCompiler();

    private final ObjectConverter objectConverter;

    public DmpInvocationHandler(Class<?> clazz, ObjectConverter objectConverter) {
        this.objectConverter = objectConverter;
        scanClassMethods(clazz);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        DmpDefinition definition = cache.get(method);
        DmpInterpreter interpreter = new DmpInterpreter();
        Object result;

        Map<String, Object> symbols = new HashMap<>();
        String[] parameterSymbols = parameterSymbolsCache.get(method);
        for (int i = 0; i < parameterSymbols.length; i++) {
            symbols.put(parameterSymbols[i], args[i]);
        }

        Object origin = null;
        int originIndex = originIndexCache.get(method);
        if (originIndex == -1 && args.length == 1) {
            originIndex = 0;
        }
        if (originIndex != -1) {
            origin = args[originIndex];
        }

        result = interpreter.run(definition, origin, symbols);

        return objectConverter.convert(result, method.getReturnType());
    }

    private void scanClassMethods(Class<?> clazz) {
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("Class " + clazz + " is not interface");
        }

        Method[] methods = clazz.getDeclaredMethods();
        Map<String, DmpDefinition> scanningCache = new HashMap<>();

        for (Method method : methods) {
            if (method.isDefault()) {
                continue;
            }

            if (method.getReturnType() == Void.class) {
                throw new IllegalArgumentException("Method " + method + "cannot return void");
            }

            Dmp dmp = method.getAnnotation(Dmp.class);
            if (dmp == null) {
                throw new IllegalArgumentException("Method " + method + " is not annotated with @Dmp");
            }

            int originIndex = -1;
            Parameter[] parameters = method.getParameters();
            String[] parameterSymbols = new String[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                Symbol symbol = parameter.getAnnotation(Symbol.class);
                if (symbol != null) {
                    parameterSymbols[i] = symbol.value();
                } else {
                    parameterSymbols[i] = parameter.getName();
                }

                if (parameter.isAnnotationPresent(Origin.class)) {
                    if (originIndex == -1) {
                        originIndex = i;
                    } else {
                        throw new IllegalArgumentException("Method has more than 1 parameter annotated with @Origin");
                    }
                }
            }

            originIndexCache.put(method, originIndex);
            parameterSymbolsCache.put(method, parameterSymbols);

            String script = dmp.value();
            DmpDefinition definition = scanningCache.computeIfAbsent(script, s -> compiler.compile(new StringReader(s)));
            cache.put(method, definition);
        }
    }
}
