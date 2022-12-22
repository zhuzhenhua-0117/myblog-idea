package com.smallhua.org.datastructure.kmp;

import com.google.common.collect.Sets;

import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈基于确定有限状态机的Kmp〉
 *
 * @author ZZH
 * @create 2022/12/20
 * @since 1.0.0
 */
public class DFMKmp {
    public static void main(String[] args) {
        System.out.println(isSubStr("asdfasdfsafabababafabababacasdf", "ababac"));
    }

    public static boolean isSubStr(String s, String p){
        int m = s.length(), n = p.length();
        int j = 0, i = 0;
        char[][] kmp = kmp(p);

        Set<Character> sets = new HashSet<>();

        for (char c : s.toCharArray()) {
            sets.add(c);
        }

        for (; i < m && j < n; i++) {
            j = sets.contains(s.charAt(i)) ? kmp[j][s.charAt(i)] : 0;
        }


        return j == n;
    }

    private static char[][] kmp(String p){
        int length = p.length(), charCount = 256, X = 0;
        char[][] ret = new char[length][charCount];
        for (int i = 0; i < charCount; i++) {
            ret[0][i] = 0;
        }
        ret[0][p.charAt(0)] = 1;

        for (int i = 1; i < length; i++) {
            for (int j = 0; j < charCount; j++) ret[i][j] = ret[X][j];
            ret[i][p.charAt(i)] = (char) (i + 1);
            X = ret[X][p.charAt(i)];

        }
        return ret;
    }
}