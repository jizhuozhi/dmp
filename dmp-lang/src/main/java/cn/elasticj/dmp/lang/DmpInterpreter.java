package cn.elasticj.dmp.lang;

import java.lang.reflect.Array;
import java.util.*;

public class DmpInterpreter {

    private final ObjectHolderRegistry objectHolderRegistry;

    public DmpInterpreter() {
        this.objectHolderRegistry = new ObjectHolderRegistry();
    }

    public DmpInterpreter(ObjectHolderRegistry objectHolderRegistry) {
        this.objectHolderRegistry = objectHolderRegistry;
    }

    public Object run(DmpDefinition definition, Object origin) {
        return run(definition, origin, new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    public Object run(DmpDefinition definition, Object origin, Map<String, Object> symbols) {
        Bytecode[] bytecodes = definition.bytecodes;
        if (bytecodes.length == 0) {
            return origin;
        }

        // runtime structures
        Object[] slots = new Object[definition.slots];
        List<Object> stack = new ArrayList<>();
        int pc = 0;

        while (pc < bytecodes.length) {
            Bytecode bytecode = bytecodes[pc];
            Op op = bytecode.op();
            Object[] values = bytecode.values();
            switch (op) {
                case LOAD_ORIGIN: {
                    stack.add(origin);
                    break;
                }
                case LOAD_SLOT: {
                    int slot = (int) values[0];
                    Object o = slots[slot];
                    stack.add(o);
                    break;
                }
                case STORE_SLOT: {
                    int slot = (int) values[0];
                    Object top = stack.remove(stack.size() - 1);
                    slots[slot] = top;
                    break;
                }
                case LOAD_SYMBOL: {
                    String symbol = (String) values[0];
                    Object o = symbols.get(symbol);
                    stack.add(o);
                    break;
                }
                case STORE_SYMBOL: {
                    String symbol = (String) values[0];
                    Object o = stack.remove(stack.size() - 1);
                    symbols.put(symbol, o);
                    break;
                }
                case REMOVE_SYMBOL: {
                    String symbol = (String) values[0];
                    symbols.remove(symbol);
                    break;
                }
                case ITERATOR_NEW: {
                    Object o = stack.remove(stack.size() - 1);
                    Iterator<?> iterator;
                    if (o.getClass().isArray()) {
                        iterator = new ArrayIterator(o);
                    } else if (o instanceof Iterable) {
                        iterator = ((Iterable<?>) o).iterator();
                    } else {
                        throw new DmpException("Object is not iterable");
                    }
                    stack.add(iterator);
                    break;
                }
                case ITERATOR_HAS_NEXT: {
                    Iterator<?> iterator = (Iterator<?>) stack.remove(stack.size() - 1);
                    stack.add(iterator.hasNext());
                    break;
                }
                case ITERATOR_NEXT: {
                    Iterator<?> iterator = (Iterator<?>) stack.remove(stack.size() - 1);
                    stack.add(iterator.next());
                    break;
                }
                case JUMP: {
                    pc = (int) values[0];
                    break;
                }
                case JUMP_FALSE: {
                    boolean topValue = (boolean) stack.remove(stack.size() - 1);
                    if (!topValue) {
                        pc = (int) values[0];
                    }
                    break;
                }
                case ARRAY_NEW: {
                    List<Object> array = new ArrayList<>();
                    stack.add(array);
                    break;
                }
                case ARRAY_PUSH: {
                    Object top = stack.remove(stack.size() - 1);
                    List<Object> top2 = (List<Object>) stack.remove(stack.size() - 1);
                    top2.add(top);
                    break;
                }
                case PUSH: {
                    stack.add(values[0]);
                    break;
                }
                case NEW: {
                    ObjectHolder objectHolder = getHolder(null);
                    Object o = objectHolder.newObject();
                    stack.add(o);
                    break;
                }
                case GET_FIELD: {
                    Object top = stack.remove(stack.size() - 1);
                    String field = (String) values[0];
                    ObjectHolder objectHolder = getHolder(top);
                    Object o = objectHolder.getField(top, field);
                    stack.add(o);
                    break;
                }
                case PUT_FIELD: {
                    Object top = stack.remove(stack.size() - 1);
                    Object top2 = stack.remove(stack.size() - 1);
                    String field = (String) values[0];
                    ObjectHolder objectHolder = getHolder(top2);
                    objectHolder.setField(top2, field, top);
                    break;
                }
            }
            pc++;
        }
        assert stack.size() == 1;
        return stack.get(0);
    }

    private ObjectHolder getHolder(Object o) {
        if (o == null) {
            return objectHolderRegistry.get(Object.class);
        }
        return objectHolderRegistry.get(o.getClass());
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
