package cn.elasticj.dmp.proxy;

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

        T.R r = t.invoke(p);
        assertThat(r.ra).isEqualTo(123);
        assertThat(r.rb).isEqualTo(456.789);
        assertThat(r.rc).isEqualTo("foobar");
        assertThat(r.rd).isEqualTo(pp);
        assertThat(r.rdb).isEqualTo(new BigDecimal("123.456"));
    }


    interface T {

        @Dmp("{ra: .a, rb: .b, rc: .c, rd: .d, rdb: .d.b}")
        R invoke(P p);

        @Data
        class P {
            int a;

            double b;

            String c;

            P d;
        }

        class R {
            int ra;

            double rb;

            String rc;

            P rd;

            BigDecimal rdb;

            public int getRa() {
                return ra;
            }

            public void setRa(int ra) {
                this.ra = ra;
            }

            public double getRb() {
                return rb;
            }

            public void setRb(double rb) {
                this.rb = rb;
            }

            public String getRc() {
                return rc;
            }

            public void setRc(String rc) {
                this.rc = rc;
            }

            public P getRd() {
                return rd;
            }

            public void setRd(P rd) {
                this.rd = rd;
            }

            public BigDecimal getRdb() {
                return rdb;
            }

            public void setRdb(BigDecimal rdb) {
                this.rdb = rdb;
            }
        }
    }
}