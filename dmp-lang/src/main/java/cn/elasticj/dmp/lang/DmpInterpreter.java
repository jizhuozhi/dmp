package cn.elasticj.dmp.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DmpInterpreter {

    private final ObjectHolderRegistry objectHolderRegistry;

    public DmpInterpreter() {
        this.objectHolderRegistry = new ObjectHolderRegistry();
    }

    public DmpInterpreter(ObjectHolderRegistry objectHolderRegistry) {
        this.objectHolderRegistry = objectHolderRegistry;
    }


    public Object run(Object origin, Bytecode... bytecodes) {
        if (bytecodes.length == 0) {
            return origin;
        }
        StackFrame frame = new StackFrame(null, new Object[]{origin});
        List<Object> stack = new ArrayList<>();
        Map<String, Object> symbols = new HashMap<>();
        int pc = 0;

        while (pc < bytecodes.length) {
            Bytecode bytecode = bytecodes[pc];
            Op op = bytecode.op();
            Object[] values = bytecode.values();
            switch (op) {
                case FRAME_NEW: {
                    Object[] slots = new Object[(int) values[0]];
                    for (int i = 0; i < slots.length; i++) {
                        slots[i] = stack.remove(stack.size() - 1);
                    }
                    frame = new StackFrame(frame, slots);
                    break;
                }
                case FRAME_RETURN: {
                    frame = frame.parent;
                    break;
                }
                case LOAD_ORIGIN: {
                    stack.add(origin);
                    break;
                }
                case LOAD_SLOT: {
                    int slot = (int) values[0];
                    Object o = frame.slots[slot];
                    stack.add(o);
                    break;
                }
                case STORE_SLOT: {
                    int slot = (int) values[0];
                    Object top = stack.remove(stack.size() - 1);
                    frame.slots[slot] = top;
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
        return stack.get(stack.size() - 1);
    }

    private ObjectHolder getHolder(Object o) {
        if (o == null) {
            return objectHolderRegistry.get(Object.class);
        }
        return objectHolderRegistry.get(o.getClass());
    }

    static class StackFrame {
        final StackFrame parent;
        final Object[] slots;

        StackFrame(StackFrame parent, Object[] slots) {
            this.parent = parent;
            this.slots = slots;
        }
    }
}
