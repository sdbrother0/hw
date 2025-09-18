package org.example.hw02.tst.tests;

import org.example.hw02.tst.utils.annotations.Test;

import static org.example.hw02.tst.utils.CustomTestRunner.Assert;

public class TestExample02 {

    @Test
    public void test1() {

    }

    @Test(timeout = 1000)
    public void test2() throws InterruptedException {
        Thread.sleep(1500);
        Assert(1, 1);
    }
}
