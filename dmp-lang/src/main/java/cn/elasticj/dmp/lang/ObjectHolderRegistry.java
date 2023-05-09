package cn.elasticj.dmp.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ObjectHolderRegistry {

    private static final ObjectHolder DEFAULT_OBJECT_HOLDER = new JavaBeanObjectHolder();

    private static final Map<Class<?>, ObjectHolder> GLOBAL = new HashMap<>();

    private final ConcurrentMap<Class<?>, ObjectHolder> registry = new ConcurrentHashMap<>(GLOBAL);

    static {
        loadGlobalObjectHolders();
    }

    public void register(Class<?> clazz, ObjectHolder objectHolder) {
        registry.put(clazz, objectHolder);
    }

    public ObjectHolder get(Class<?> clazz) {
        if (clazz == null) {
            return DEFAULT_OBJECT_HOLDER;
        }
        return registry.getOrDefault(clazz, DEFAULT_OBJECT_HOLDER);
    }

    public void remove(Class<?> clazz) {
        registry.remove(clazz);
    }

    private static void loadGlobalObjectHolders() {
        ServiceLoader<ObjectHolder> loader = ServiceLoader.load(ObjectHolder.class);
        for (ObjectHolder objectHolder : loader) {
            for (Class<?> clazz : objectHolder.supports()) {
                GLOBAL.put(clazz, objectHolder);
            }
        }
    }
}
