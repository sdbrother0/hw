package org.example.hw02.tst.utils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ArgSources {

    /*
        //example method for source of parametrized test
        public static Object[] getArgs() {
            return new Object[]{
                    "test1", 1,
                    "test2", 2,
            };
        }
    */
    String methodName();
}
