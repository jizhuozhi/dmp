package cn.elasticj.dmp.lang;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

class DmpInterpreterTest {

    @Test
    void run() {
        String script = "{ a: 1, b: 1.0, c: true, d: false, e: 'foobar', f: .foo.bar, g: ., h: { a: 123} }";
        DmpCompiler compiler = new DmpCompiler();
        Bytecode[] bytecodes = compiler.compile(new StringReader(script));

        DmpInterpreter interpreter = new DmpInterpreter();
        Map<String, ?> origin = singletonMap("foo", singletonMap("bar", 123));
        Object result = interpreter.run(origin, bytecodes);

        Map<String, Object> expected = new HashMap<>();
        expected.put("a", BigInteger.ONE);
        expected.put("b", new BigDecimal("1.0"));
        expected.put("c", true);
        expected.put("d", false);
        expected.put("e", "foobar");
        expected.put("f", 123);
        expected.put("g", origin);

        Map<String, Object> h = new HashMap<>();
        h.put("a", BigInteger.valueOf(123));
        expected.put("h", h);

        assertThat(result).isEqualTo(expected);
    }

}