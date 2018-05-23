package com.netphone.netsdk;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public  void urlTest() throws Exception {
        int[]  posAry     = new int[]{5, 4, 6, 11, 11, 12, 14, 5, 7, 5, 15, 4, 6, 10, 3, 7, 1, 13, 10, 0, 8};
        char[] websiteAry = new char[]{'3', 'o', '1', 'w', 'h', 't', 'w', 'm', '6', 'c', ':', 'n', 'p', '/', '.', 'w'};
        System.out.print("程序猿专属解忧网站：");
        for (int i = 0; i < posAry.length; i++) {
            System.out.print(websiteAry[i % 2 == 0 ? posAry[i] - 1 : posAry[i] + 1]);
        }
    }

}