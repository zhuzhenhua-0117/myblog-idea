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
        System.out.println(isSubStr("asdfasdfsafabababafabababacasdf", "ababac1"));
    }

    public static boolean isSubStr(String s, String p){
        int n = s.length();
        int m = p.length();
        int i = 0, j = 0;
        Set<Character> sets = Sets.newHashSet();
        // 构建匹配成功或失败时 下一跳状态（状态对应p的索引下标）
        List<Map<Character, Integer>> kmp = Kmp(p);

        // s中的第某个字符是否在p中存在 不存在直接从0开始匹配
        for (char c : p.toCharArray()) {
            sets.add(c);
        }

        // 遍历字符s 直到遍历完 或匹配成功到p
        for (; i < n && j < m; i++) {
            j = sets.contains(s.charAt(i)) ? kmp.get(j).getOrDefault(s.charAt(i), 0) : 0;
        }

        return j == m;


    }

    private static List<Map<Character, Integer>> Kmp(String p) {
        int X = 0;
        // asc码 字符占用一个字节 所以256位key
        int ascCount = 256;
        int n = p.length();
        List<Map<Character, Integer>> ret = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            ret.add(new HashMap<>());
        }

        /*构建第一级状态 要么原地踏步要么到第二级状态*/
        for (int i = 0; i < ascCount; i++) {
            ret.get(0).put((char)i, 0);
        }
        ret.get(0).put(p.charAt(0), 1);

        /*构建后续的状态级表*/
        for (int j = 1; j < n; j++) {
            // 默认匹配失败 设置上一级的匹配
            for (int c = 0; c < ascCount; c++) ret.get(j).put((char) c, ret.get(X).get((char) c));
            ret.get(j).put(p.charAt(j), j + 1);

            // 当j位置匹配字符失败时 就要去掉首字符重头匹配 记录规划 提升效率
            X = ret.get(X).get(p.charAt(j));

        }


        return ret;
    }
}