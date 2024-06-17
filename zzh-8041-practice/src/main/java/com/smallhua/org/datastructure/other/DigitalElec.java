package com.smallhua.org.datastructure.other;

import lombok.Data;
import org.junit.jupiter.api.Test;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author ZZH
 * @create 2024/6/17
 * @since 1.0.0
 */
@Data
public class DigitalElec {

    private static final int[] NUMS = new int[]{888, 888, 888, 666, 9, 9 ,9 , 10, 10, 10, 666,666, 1};

    public static void main(String[] args) {
        testDigitalEle();
    }

    /**
     *  00  0 00
     *  00  1 01
     *  01  0 01
     *  01  1 10
     *  10  0 10
     *  10  1 00
     *
     * new_b = a`(b@x)
     * new_a = a`bx + ab`c`
     *
     */

    public static void testDigitalEle(){

        int[] nums = NUMS;
        int a = 0, b = 0;
        for (int num : nums) {
            int tmp = a;
            a = (~a&b&num) | (a&~b&~num);
            b = ~tmp&(b^num);
        }

        System.out.println(b);
    }
}