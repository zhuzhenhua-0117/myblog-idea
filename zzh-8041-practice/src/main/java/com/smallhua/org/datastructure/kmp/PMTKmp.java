package com.smallhua.org.datastructure.kmp;

/**
 * 〈一句话功能简述〉<br>
 * 〈部分匹配表Kmp〉
 *
 * @author ZZH
 * @create 2022/12/20
 * @since 1.0.0
 */
public class PMTKmp {

    public static void main(String[] args) {
        System.out.println(match("asdfasdfsafabababafabababacasdf", "ababac"));
    }

    public static boolean match(String s, String p){
        int[] pmt = PMT(p);
        int i = 0, j = 0;
        int m = s.length();
        int n = p.length();

        while(i < m){
            if(j == -1 || s.charAt(i) == p.charAt(j)){
                if (j == n-1) return true;
                i++;
                j++;
            } else {
                j = pmt[j];
            }

        }
        return false;
    }

    private static int[] PMT(String p){
        int n = p.length();
        int[] ret = new int[n+1];
        int i = 0, j = -1;

        ret[0] = -1;
        while(i < n){
            if(j == -1 || p.charAt(i) == p.charAt(j)){
                i++;
                j++;
                ret[i] = j;
            } else {
                j = ret[j];
            }

        }
        return ret;
    }

}