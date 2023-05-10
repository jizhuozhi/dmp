package cn.elasticj.dmp.proxy;

import cn.elasticj.dmp.proxy.annotation.Dmp;
import cn.elasticj.dmp.proxy.annotation.Origin;
import cn.elasticj.dmp.proxy.annotation.Symbol;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class DmpInvocationHandlerTest {

    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void invoke() {
        T t = DmpProxy.newInstance(T.class, OBJECT_MAPPER::convertValue);

        T.P p = new T.P();
        p.a = 123;
        p.b = 456.789;
        p.c = "foobar";

        T.P pp = new T.P();
        pp.b = 123.456;

        p.d = pp;

        T.R r = t.invoke(p, 1, 2);
        assertThat(r.ra).isEqualTo(123);
        assertThat(r.rb).isEqualTo(456.789);
        assertThat(r.rc).isEqualTo("foobar");
        assertThat(r.rd).isEqualTo(pp);
        assertThat(r.rdb).isEqualTo(new BigDecimal("123.456"));
        assertThat(r.sra).isEqualTo(1);
        assertThat(r.srb).isEqualTo(2);
    }

    interface T {

        @Dmp("{ra: .a, rb: .b, rc: .c, rd: .d, rdb: .d.b, sra: a, srb: arg2}")
        R invoke(@Origin P p, @Symbol("a") int a, int b);

        @Data
        class P {
            int a;

            double b;

            String c;

            P d;
        }

        @Data
        class R {
            int ra;

            double rb;

            String rc;

            P rd;

            BigDecimal rdb;

            int sra;

            int srb;
        }
    }
}