package com.smallhua.org.datastructure.solveMethod.slideWindow;

/**
 * 〈一句话功能简述〉<br>
 * 〈字符全排列〉 targetStr存在一种全排列是iKnownStr的子串
 *
 * @author ZZH
 * @create 2022/11/3
 * @since 1.0.0
 */
public class StrOrder {

    public static void main(String[] args) {
        System.out.println(containRank("ab", "cbad"));
    }


    public static boolean containRank(String targetStr, String iKnownStr){
        int m = targetStr.length();
        int n = iKnownStr.length();

        if(m > n) return false;

        int[] prefixSum = new int[26];
        int diff = 0;
        for (int i = 0; i < m; i++) {
            prefixSum[targetStr.charAt(i) - 'a']--;
            prefixSum[iKnownStr.charAt(i) - 'a']++;
        }

        for (int i : prefixSum) {
            if(i != 0) diff++;
        }

        if (diff == 0) return true;

        for (int i = m; i < n; i++) {
            int right = iKnownStr.charAt(i) - 'a';
            int left = iKnownStr.charAt(i-m) - 'a';
            if(right == left) continue;

            if (prefixSum[right] == 0) diff++;
            prefixSum[right]++;

            if (prefixSum[left] == 0) diff++;
            prefixSum[left]--;

            if (prefixSum[right] == 0) diff--;
            if (prefixSum[left] == 0) diff--;

            if(diff == 0) return true;
        }


        return false;
    }
}