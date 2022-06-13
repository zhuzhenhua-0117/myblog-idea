package com.smallhua.org.datastructure.tree;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 〈一句话功能简述〉<br>
 * 〈字典树〉
 *
 * @author ZZH
 * @create 2021/8/5
 * @since 1.0.0
 */
public class DictTree {

    public static void main(String[] args) {
        Trie trie = new Trie();
        Scanner sc = new Scanner(System.in);
        Scanner sc1 = new Scanner(System.in);
        int n = sc.nextInt();
        int res = 0;
        for (int i = 0; i < n; i++){
            String str = sc1.nextLine();
            char[] chars = str.toCharArray();
            //进行分类 同类型字符算同一个字符（每个字母出现个数一样，只是顺序不同）
            Arrays.sort(chars);
            String newStr = String.valueOf(chars);
            if (!trie.search(newStr)){
                res++;
                trie.insert(newStr);
            }
        }
        System.out.println(res);
    }
}

class Trie{
    private boolean isEnd;

    private Trie[] children;

    public Trie() {
        isEnd = false;
        children = new Trie[26];
    }

    public Trie[] getChildren() {
        return children;
    }

    public void insert(String s){
        Trie node = this;
        char[] chars = s.toCharArray();
        for (char aChar : chars) {
            int index = aChar - 'a';
            if(node.children[index] == null) {
                node.children[index] = new Trie();
            }
            node = node.children[index];
        }
        node.isEnd = true;
    }

    public boolean search(String s){
        Trie node = searchPrefix(s);
        return node != null && node.isEnd;
    }

    public boolean startWith(String s){
        Trie node = searchPrefix(s);
        return node != null;
    }

    private Trie searchPrefix(String s){
        Trie node = this;
        char[] chars = s.toCharArray();
        for (char aChar : chars) {
            int index = aChar - 'a';
            if(node.children[index] == null) {
               return null;
            }
            node = node.children[index];
        }
        return node;
    }
}