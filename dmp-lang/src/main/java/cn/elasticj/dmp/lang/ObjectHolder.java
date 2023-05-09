package cn.elasticj.dmp.lang;

public interface ObjectHolder {

    Object newObject();

    Object getField(Object object, String name);

    void setField(Object object, String name, Object value);

    default Class<?>[] supports() {
        return new Class<?>[0];
    }

}
