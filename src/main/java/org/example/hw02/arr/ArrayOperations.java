package org.example.hw02.arr;

import com.google.common.base.Stopwatch;

import java.util.List;

public class ArrayOperations {

    /**
     * Shift array elements using System.arraycopy
     */
    public static void shiftLeftSystemCopy(int[] array, int positions) {
        System.arraycopy(array, positions, array, 0, array.length - positions);
    }

    /**
     * Shift array elements using manual for loop
     */
    public static void shiftLeftManualLoop(int[] array, int positions) {
        for (int i = positions; i < array.length; i++) {
            array[i - positions] = array[i];
        }
    }

    public static int[] init(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        return arr;
    }

    public static void run() {
        int arr_1000m[] = ArrayOperations.init(1000);
        int arr_10_000m[] = ArrayOperations.init(10_000);
        int arr_100_000m[] = ArrayOperations.init(100_000);
        int arr_1_000_000m[] = ArrayOperations.init(1_000_000);

        int arr_1000s[] = ArrayOperations.init(1000);
        int arr_10_000s[] = ArrayOperations.init(10_000);
        int arr_100_000s[] = ArrayOperations.init(100_000);
        int arr_1_000_000s[] = ArrayOperations.init(1_000_000);


        for (int[] arr : List.of(arr_1000m, arr_10_000m, arr_100_000m, arr_1_000_000m)) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            ArrayOperations.shiftLeftManualLoop(arr, 1);
            ArrayOperations.shiftLeftManualLoop(arr, 10);
            ArrayOperations.shiftLeftManualLoop(arr, 100);
            ArrayOperations.shiftLeftManualLoop(arr, 1000);
            stopwatch.stop();
            System.out.printf("Shift ManualLoop in %s time: %sms%n", arr.length, stopwatch.elapsed().toMillis());
        }

        for (int[] arr : List.of(arr_1000s, arr_10_000s, arr_100_000s, arr_1_000_000s)) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            ArrayOperations.shiftLeftSystemCopy(arr, 1);
            ArrayOperations.shiftLeftSystemCopy(arr, 10);
            ArrayOperations.shiftLeftSystemCopy(arr, 100);
            ArrayOperations.shiftLeftSystemCopy(arr, 1000);
            stopwatch.stop();
            System.out.printf("Shift SystemCopy in %s time: %sms%n", arr.length, stopwatch.elapsed().toMillis());
        }

    }
}
