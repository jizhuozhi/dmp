package cn.elasticj.dmp.lang;

import org.apache.commons.beanutils.BeanUtils;

import java.util.HashMap;

@SuppressWarnings({"rawtypes", "unchecked"})
class JavaBeanObjectHolder implements ObjectHolder {
    @Override
    public Object newObject() {
        return new HashMap<>();
    }

    @Override
    public Object getField(Object object, String name) throws DmpException {
        try {
            return BeanUtils.getProperty(object, name);
        } catch (Exception e) {
            throw new DmpException(e);
        }
    }

    @Override
    public void setField(Object object, String name, Object value) {
        try {
            BeanUtils.setProperty(object, name, value);
        } catch (Exception e) {
            throw new DmpException(e);
        }
    }
}
