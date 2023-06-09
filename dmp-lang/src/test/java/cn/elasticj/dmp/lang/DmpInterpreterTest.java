package cn.elasticj.dmp.lang;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

class DmpInterpreterTest {

    @Test
    void run() {
        String script = "{ a: 1, b: 1.0, c: true, d: false, e: 'foobar', f: .foo.bar, g: ., h: { a: 123}, i: .foo(it -> it.bar), " +
                "j: .foo(it -> it.bar(it1 -> { a: it, b: it1 })), k: .foobar[it->{ a: it }] }";
        DmpCompiler compiler = new DmpCompiler();
        DmpDefinition definition = compiler.compile(new StringReader(script));

        DmpInterpreter interpreter = new DmpInterpreter();
        Map<String, Object> origin = new HashMap<>();
        origin.put("foo", singletonMap("bar", 123));
        origin.put("foobar", new Object[]{1, 2, 3, 4});
        Object result = interpreter.run(definition, origin);

        Map<String, Object> expected = new HashMap<>();
        expected.put("a", BigInteger.ONE);
        expected.put("b", new BigDecimal("1.0"));
        expected.put("c", true);
        expected.put("d", false);
        expected.put("e", "foobar");
        expected.put("f", 123);
        expected.put("g", origin);
        expected.put("i", 123);

        Map<String, Object> h = new HashMap<>();
        h.put("a", BigInteger.valueOf(123));
        expected.put("h", h);

        Map<String, Object> j = new HashMap<>();
        j.put("a", singletonMap("bar", 123));
        j.put("b", 123);
        expected.put("j", j);

        expected.put("k", Arrays.asList(singletonMap("a", 1), singletonMap("a", 2), singletonMap("a", 3), singletonMap("a", 4)));

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void runOptional() {
        String script = ".a.b?.c";

        DmpCompiler compiler = new DmpCompiler();
        DmpDefinition definition = compiler.compile(new StringReader(script));

        DmpInterpreter interpreter = new DmpInterpreter();
        Map<String, Object> origin = singletonMap("a", singletonMap("b", null));
        Object result = interpreter.run(definition, origin);
        assertThat(result).isNull();

        origin = singletonMap("a", singletonMap("b", singletonMap("c", 123)));
        result = interpreter.run(definition, origin);
        assertThat(result).isEqualTo(123);
    }
}
