package org.example.hw02.tst.utils;

import com.google.common.base.Stopwatch;
import org.example.hw02.tst.utils.annotations.AfterEach;
import org.example.hw02.tst.utils.annotations.ArgSources;
import org.example.hw02.tst.utils.annotations.BeforeEach;
import org.example.hw02.tst.utils.annotations.Test;
import org.example.hw02.tst.utils.dto.MethodArgs;
import org.example.hw02.tst.utils.exception.AssertException;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CustomTestRunner {

    private static final String FAIL = "❌:";
    private static final String OK = "✅";

    public static void Assert(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertException("Expected: " + expected + ", but found: " + actual);
        }
    }

    public static void run(String packageName) {
        Map<Class<?>, Set<MethodArgs>> methodMap = getMethodMap(packageName);
        System.out.println("==== Custom Test Runner Results =====");
        System.out.printf("Package: %s%n", packageName);
        System.out.printf("Classes scanned: %s%n", methodMap.size());
        System.out.printf("Tests discovered: %s%n", methodMap.values().stream().flatMap(Collection::stream).map(MethodArgs::getMethod).distinct().count());
        System.out.println("Tests Results:");
        Stopwatch stopWatch = Stopwatch.createStarted();
        try (ExecutorService executorService = Executors.newSingleThreadExecutor()) {
            methodMap.forEach((clazz, methods) -> {
                try {
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    methods.forEach(methodArgs -> {
                        String className = clazz.getName().replace(clazz.getPackageName() + ".", "");
                        Method method = methodArgs.getMethod();
                        //Call methods, annotated @BeforeEach
                        methodArgs.getMethodListBefore().forEach(beforeMethod -> {
                            try {
                                beforeMethod.invoke(instance);
                            } catch (Exception e) {
                                System.err.println("Error: " + e.getMessage());
                            }
                        });
                        runTest(executorService, className, method, instance, methodArgs.getArgs(), methodArgs.getTimeout(), methodArgs.getTimeUnit(), methodArgs);
                        //Call methods, annotated @AfterEach
                        methodArgs.getMethodListAfter().forEach(afterMethod -> {
                            try {
                                afterMethod.invoke(instance);
                            } catch (Exception e) {
                                System.err.println("Error: " + e.getMessage());
                            }
                        });
                    });
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            });
        }
        stopWatch.stop();
        long countOk = methodMap.values().stream().flatMap(Collection::stream).filter(result -> Objects.isNull(result.getError())).count();
        long countFailed = methodMap.values().stream().flatMap(Collection::stream).filter(result -> Objects.nonNull(result.getError())).count();
        System.out.printf("%nSummary:%n");
        System.out.printf("Total tests: %s%n", countOk + countFailed);
        System.out.printf("Passed: %s%n", countOk);
        System.out.printf("Failed: %s%n", countFailed);
        System.out.printf("Total execution time: %sms%n", stopWatch.elapsed().toMillis());
        System.out.printf("Success rate: %s%%%n", 100 * countOk / (countOk + countFailed));
        System.out.println("=====================================");
    }

    /*
        Discover classes and their methods one level deep
        return object Map<Class<?>, List<MethodArgs>>
        key - class for instantiation during tests
        value - List<MethodArgs>>;
                MethodArgs:
                    Method method; -- method, annotated by @Test for call as test
                    Object[] -- args - arguments for testing as parametrized test;
     */
    private static Map<Class<?>, Set<MethodArgs>> getMethodMap(String packageName) {
        Map<Class<?>, Set<MethodArgs>> methodMaps = new LinkedHashMap<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String resourceName = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(resourceName);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                String fileName = url.getFile();
                File[] listFiles = new File(fileName).listFiles();
                if (Objects.nonNull(listFiles)) {
                    for (File file : listFiles) {
                        String classNameWithoutPostfix = file.getName().replaceAll("^*.class", "");
                        String fullClassNameWithoutPostfix = String.format("%s.%s", packageName, classNameWithoutPostfix);
                        Class<?> clazz = Class.forName(fullClassNameWithoutPostfix);
                        Method[] methods = clazz.getMethods();
                        List<Method> methodListBefore = Arrays.stream(methods).filter(my -> my.isAnnotationPresent(BeforeEach.class)).toList();
                        List<Method> methodListAfter = Arrays.stream(methods).filter(my -> my.isAnnotationPresent(AfterEach.class)).toList();
                        for (Method method : methods) {
                            Object[] args = new Object[method.getParameterCount()];
                            if (method.isAnnotationPresent(ArgSources.class)) {
                                String methodNameArgSources = method.getAnnotation(ArgSources.class).methodName();
                                Object[] argsSources = (Object[]) clazz.getMethod(methodNameArgSources).invoke(new Object[]{});
                                int argCount = method.getParameterCount();
                                for (int i = 0; i < argCount; i++) {
                                    int offset = i * argCount;
                                    args = Arrays.asList(argsSources).subList(offset, argCount + offset).toArray(new Object[]{});
                                    addToMethodMaps(methodMaps, clazz, method, methodListBefore, methodListAfter, args);
                                }
                            }
                            if (method.isAnnotationPresent(Test.class)) {
                                addToMethodMaps(methodMaps, clazz, method, methodListBefore, methodListAfter, args);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return methodMaps;
    }

    private static void addToMethodMaps(Map<Class<?>, Set<MethodArgs>> methodMaps, Class<?> clazz,
                                        Method method,
                                        List<Method> methodListBefore,
                                        List<Method> methodListAfter,
                                        Object[] args) {

        MethodArgs methodArgs = new MethodArgs(method, methodListBefore, methodListAfter, args,
                method.getAnnotation(Test.class).timeout(),
                method.getAnnotation(Test.class).timeUnit(), null, Duration.ZERO);

        if (!methodMaps.containsKey(clazz)) {
            methodMaps.put(clazz, new LinkedHashSet<>());
        }
        methodMaps.get(clazz).add(methodArgs);
    }

    private static void runTest(ExecutorService executorService, String className, Method method, Object instance, Object[] args,
                                long timeout, TimeUnit unit, MethodArgs methodArgs) {
        Stopwatch stopWatch = Stopwatch.createStarted();
        try {
            Future<?> result = executorService.submit(() -> {
                try {
                    if (args.length > 0) {
                        method.invoke(instance, args);
                    } else {
                        method.invoke(instance);
                    }
                } catch (InvocationTargetException e) {
                    if (e.getTargetException() instanceof AssertException assertException) {
                        methodArgs.setError(assertException);
                    }
                } catch (IllegalAccessException e) {
                    System.err.println("Error: " + e.getMessage());
                }
            });
            result.get(timeout, unit);
        } catch (ExecutionException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (TimeoutException e) {
            methodArgs.setError(e);
        } finally {
            stopWatch.stop();
            methodArgs.setDuration(stopWatch.elapsed());
            printResult(className, method, methodArgs);
        }
    }

    private static void printResult(String className, Method method, MethodArgs methodArgs) {
        long millis = methodArgs.getDuration().toMillis();
        if (Objects.isNull(methodArgs.getError())) {
            if (methodArgs.getArgs().length > 0) {
                System.out.printf("%s %s.%s, args: %s (%sms) %n", OK, className, method.getName(), Arrays.toString(methodArgs.getArgs()), millis);
            } else {
                System.out.printf("%s %s.%s (%sms) %n", OK, className, method.getName(), millis);
            }
        } else {
            if (methodArgs.getError() instanceof TimeoutException timeoutException) {
                System.out.printf("%s %s.%s, Error timeout: %s %s (%sms) %n", FAIL, className, method.getName(), methodArgs.getTimeout(), methodArgs.getTimeUnit(), millis);
            } else {
                if (methodArgs.getArgs().length > 0) {
                    System.out.printf("%s %s.%s, args: %s, error: %s (%sms) %n", FAIL, className, method.getName(), Arrays.toString(methodArgs.getArgs()), methodArgs.getError().getMessage(), millis);
                } else {
                    System.out.printf("%s %s.%s, error: %s (%sms) %n", FAIL, className, method.getName(), methodArgs.getError().getMessage(), millis);
                }
            }
        }
    }

}
