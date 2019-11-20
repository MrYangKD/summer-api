package com.cn.summer.api;

/**
 * .
 *
 * @author YangYK
 * @create: 2019-11-17 15:20
 * @since 1.0
 */
public class Test {
    public static void main(String[] args) {
        int number = 7;
        //原始数二进制
        printInfo(number);
        number = number >>> 16;
        printInfo(number);
    }

    private static void printInfo(int num) {
        System.out.println(Integer.toBinaryString(num));
    }
}
