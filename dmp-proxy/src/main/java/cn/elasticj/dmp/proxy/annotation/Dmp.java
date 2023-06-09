package cn.elasticj.dmp.proxy.annotation;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Dmp {

    String value() default "";
}
