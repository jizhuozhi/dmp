package cn.elasticj.dmp.lang;

import java.lang.reflect.Array;
import java.util.Iterator;

@SuppressWarnings("rawtypes")
public class DmpIterator implements Iterator<Object> {

    private final Iterator iterator;

    public DmpIterator(Object o) {
        if (o.getClass().isArray()) {
            iterator = new ArrayIterator(o);
        } else if (o instanceof Iterable) {
            iterator = ((Iterable) o).iterator();
        } else {
            throw new DmpException("Object is not iterable");
        }
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Object next() {
        return iterator.next();
    }

    static class ArrayIterator implements Iterator<Object> {

        final Object o;

        int position = 0;

        ArrayIterator(Object o) {
            this.o = o;
        }

        @Override
        public boolean hasNext() {
            return position < Array.getLength(o);
        }

        @Override
        public Object next() {
            return Array.get(o, position++);
        }
    }
}
