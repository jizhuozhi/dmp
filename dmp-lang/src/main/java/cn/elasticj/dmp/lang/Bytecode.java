package cn.elasticj.dmp.lang;

import java.util.Arrays;
import java.util.Objects;

public class Bytecode {

    private final Op op;

    private final Object[] values;

    public Bytecode(Op op, Object... values) {
        this.op = op;
        this.values = values;
    }

    public Op op() {
        return op;
    }

    public Object[] values() {
        return Arrays.copyOf(values, values.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bytecode bytecode = (Bytecode) o;
        return op == bytecode.op && Arrays.equals(values, bytecode.values);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(op);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }

    @Override
    public String toString() {
        return "Bytecode{" +
                "op=" + op +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
