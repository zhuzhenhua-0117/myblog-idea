package com.smallhua.org.datastructure.other;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈深度递归+回溯生成括号〉
 *
 * @author ZZH
 * @create 2021/8/12
 * @since 1.0.0
 */
public class GenerateKuohao {

    public List<String> generateKuohao(int n){
        List<String> ret = new ArrayList<>();
        dfs(ret, 0, 0, n, new StringBuilder());
        return ret;
    }

    private void dfs(List<String> ret, int open, int close, int n, StringBuilder sb) {
        if (sb.length() == 2*n) {
            ret.add(sb.toString());
            return;
        }

        if (open < close)
            return;

        if (open < n){
            sb.append("(");
            dfs(ret, open + 1, close, n, sb);
            sb.deleteCharAt(sb.length() - 1);
        }

        if (close < n){
            sb.append(")");
            dfs(ret, open, close + 1, n, sb);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    public static void main(String[] args) {
        GenerateKuohao a = new GenerateKuohao();
        System.out.println(a.generateKuohao(2).toString());


    }

}