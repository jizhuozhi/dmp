package cn.elasticj.dmp.lang;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
class JavaBeanObjectHolder implements ObjectHolder {
    @Override
    public Object newObject() {
        return new HashMap<>();
    }

    @Override
    public Object getField(Object object, String name) throws DmpException {
        try {
            if (object == null) {
                throw new NullPointerException("getField " + name + " from null");
            }
            if (object instanceof Map) {
                Map map = ((Map) object);
                return map.get(name);
            }

            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            throw new DmpException(e);
        }
    }

    @Override
    public void setField(Object object, String name, Object value) {
        try {
            if (object == null) {
                throw new NullPointerException("setField " + name + " to null");
            }

            if (object instanceof Map) {
                Map map = ((Map) object);
                map.put(name, value);
                return;
            }

            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new DmpException(e);
        }
    }

    @Override
    public Class<?>[] supports() {
        return new Class[]{Object.class};
    }
}
