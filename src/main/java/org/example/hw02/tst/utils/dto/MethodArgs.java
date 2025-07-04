package org.example.hw02.tst.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
public class MethodArgs {
    private Method method;
    private List<Method> methodListBefore;
    private List<Method> methodListAfter;
    private Object[] args;
    long timeout;
    private TimeUnit timeUnit;
    private Exception error;
    private Duration duration;
}
