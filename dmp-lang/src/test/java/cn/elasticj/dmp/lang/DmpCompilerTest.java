package cn.elasticj.dmp.lang;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class DmpCompilerTest {

    @Test
    void compile() {
        String script = "{ a: 1, b: 1.0, c: true, d: false, e: 'foobar', f: .foo.bar, g: ., h: { a: 123}, i: .foo(it -> it.bar), " +
                "j: .foo(it -> it.bar(it1 -> { a: it, b: it1 })), k: .foobar[it->{ a: it }] }";
        Bytecode[] expected = new Bytecode[]{
                new Bytecode(Op.NEW),
                new Bytecode(Op.STORE_SLOT, 0),

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
                new Bytecode(Op.STORE_SLOT, 1),

                new Bytecode(Op.LOAD_SLOT, 1),
                new Bytecode(Op.PUSH, BigInteger.valueOf(123)),
                new Bytecode(Op.PUT_FIELD, "a"),

                new Bytecode(Op.LOAD_SLOT, 1),

                new Bytecode(Op.PUT_FIELD, "h"),

                new Bytecode(Op.LOAD_SLOT, 0),

                new Bytecode(Op.LOAD_ORIGIN),
                new Bytecode(Op.GET_FIELD, "foo"),
                new Bytecode(Op.STORE_SYMBOL, "it"),
                new Bytecode(Op.LOAD_SYMBOL, "it"),
                new Bytecode(Op.GET_FIELD, "bar"),
                new Bytecode(Op.REMOVE_SYMBOL, "it"),
                new Bytecode(Op.PUT_FIELD, "i"),
                new Bytecode(Op.LOAD_SLOT, 0),

                new Bytecode(Op.LOAD_ORIGIN),
                new Bytecode(Op.GET_FIELD, "foo"),
                new Bytecode(Op.STORE_SYMBOL, "it"),
                new Bytecode(Op.LOAD_SYMBOL, "it"),
                new Bytecode(Op.GET_FIELD, "bar"),
                new Bytecode(Op.STORE_SYMBOL, "it1"),

                new Bytecode(Op.NEW),
                new Bytecode(Op.STORE_SLOT, 2),
                new Bytecode(Op.LOAD_SLOT, 2),
                new Bytecode(Op.LOAD_SYMBOL, "it"),
                new Bytecode(Op.PUT_FIELD, "a"),
                new Bytecode(Op.LOAD_SLOT, 2),
                new Bytecode(Op.LOAD_SYMBOL, "it1"),
                new Bytecode(Op.PUT_FIELD, "b"),
                new Bytecode(Op.LOAD_SLOT, 2),
                new Bytecode(Op.REMOVE_SYMBOL, "it1"),
                new Bytecode(Op.REMOVE_SYMBOL, "it"),
                new Bytecode(Op.PUT_FIELD, "j"),

                new Bytecode(Op.LOAD_SLOT, 0),

                new Bytecode(Op.LOAD_ORIGIN),
                new Bytecode(Op.GET_FIELD, "foobar"),
                new Bytecode(Op.ITERATOR_NEW),
                new Bytecode(Op.STORE_SLOT, 3),

                new Bytecode(Op.ARRAY_NEW),
                new Bytecode(Op.STORE_SLOT, 4),

                new Bytecode(Op.LOAD_SLOT, 3),
                new Bytecode(Op.ITERATOR_HAS_NEXT),
                new Bytecode(Op.JUMP_FALSE, 82),

                new Bytecode(Op.LOAD_SLOT, 4),

                new Bytecode(Op.LOAD_SLOT, 3),
                new Bytecode(Op.ITERATOR_NEXT),
                new Bytecode(Op.STORE_SYMBOL, "it"),

                new Bytecode(Op.NEW),
                new Bytecode(Op.STORE_SLOT, 5),
                new Bytecode(Op.LOAD_SLOT, 5),
                new Bytecode(Op.LOAD_SYMBOL, "it"),
                new Bytecode(Op.PUT_FIELD, "a"),
                new Bytecode(Op.LOAD_SLOT, 5),
                new Bytecode(Op.REMOVE_SYMBOL, "it"),

                new Bytecode(Op.ARRAY_PUSH),
                new Bytecode(Op.JUMP, 66),
                new Bytecode(Op.LOAD_SLOT, 4),

                new Bytecode(Op.PUT_FIELD, "k"),
                new Bytecode(Op.LOAD_SLOT, 0),
        };

        DmpCompiler compiler = new DmpCompiler();
        DmpDefinition definition = compiler.compile(new StringReader(script));

        assertThat(definition.bytecodes).isEqualTo(expected);
    }
}