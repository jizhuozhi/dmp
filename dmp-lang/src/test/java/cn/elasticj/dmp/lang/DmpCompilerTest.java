package cn.elasticj.dmp.lang;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class DmpCompilerTest {

    @Test
    void compile() {
        String script = "{ a: 1, b: 1.0, c: true, d: false, e: 'foobar', f: .foo.bar, g: ., h: { a: 123} }";
        Bytecode[] expected = new Bytecode[]{
                new Bytecode(Op.NEW),
                new Bytecode(Op.FRAME_NEW, 1),

                new Bytecode(Op.LOAD_SLOT, 0),
                new Bytecode(Op.PUSH, BigInteger.ONE),
                new Bytecode(Op.PUT_FIELD, "a"),

                new Bytecode(Op.LOAD_SLOT, 0),
                new Bytecode(Op.PUSH, new BigDecimal("1.0")),
                new Bytecode(Op.PUT_FIELD, "b"),

                new Bytecode(Op.LOAD_SLOT, 0),
                new Bytecode(Op.PUSH, true),
                new Bytecode(Op.PUT_FIELD, "c"),

                new Bytecode(Op.LOAD_SLOT, 0),
                new Bytecode(Op.PUSH, false),
                new Bytecode(Op.PUT_FIELD, "d"),

                new Bytecode(Op.LOAD_SLOT, 0),
                new Bytecode(Op.PUSH, "foobar"),
                new Bytecode(Op.PUT_FIELD, "e"),

                new Bytecode(Op.LOAD_SLOT, 0),
                new Bytecode(Op.LOAD_ORIGIN),
                new Bytecode(Op.GET_FIELD, "foo"),
                new Bytecode(Op.GET_FIELD, "bar"),
                new Bytecode(Op.PUT_FIELD, "f"),

                new Bytecode(Op.LOAD_SLOT, 0),
                new Bytecode(Op.LOAD_ORIGIN),
                new Bytecode(Op.PUT_FIELD, "g"),

                new Bytecode(Op.LOAD_SLOT, 0),

                new Bytecode(Op.NEW),
                new Bytecode(Op.FRAME_NEW, 1),

                new Bytecode(Op.LOAD_SLOT, 0),
                new Bytecode(Op.PUSH, BigInteger.valueOf(123)),
                new Bytecode(Op.PUT_FIELD, "a"),

                new Bytecode(Op.LOAD_SLOT, 0),
                new Bytecode(Op.FRAME_RETURN),

                new Bytecode(Op.PUT_FIELD, "h"),

                new Bytecode(Op.LOAD_SLOT, 0),
                new Bytecode(Op.FRAME_RETURN)
        };

        DmpCompiler compiler = new DmpCompiler();
        Bytecode[] bytecodes = compiler.compile(new StringReader(script));

        assertThat(bytecodes).isEqualTo(expected);
    }
}