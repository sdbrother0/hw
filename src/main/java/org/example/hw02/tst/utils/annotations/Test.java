package org.example.hw02.tst.utils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    int timeout() default Integer.MAX_VALUE;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
