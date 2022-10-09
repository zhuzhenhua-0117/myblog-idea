package com.smallhua.org.datastructure.tree.treeArray;

/**
 * 〈一句话功能简述〉<br> 区间修改，单点查询
 *  区间统一加减某值 求操作之后某个索引下标的
 * @author ZZH
 * @create 2022/10/9
 * @since 1.0.0
 */
public class IntervalSplit {

    public static void main(String[] args) {
        int[] nums = new int[]{1, 5, 6, 9, 8, 3, 4};
        TreeArrayForIntervalSplit treeArrayForIntervalSplit = new TreeArrayForIntervalSplit(nums);
        System.out.println(treeArrayForIntervalSplit.query(2));

        // 区间修改[2,4]的值 第2个到第四个增加4
        treeArrayForIntervalSplit.modify(2, 4);
        treeArrayForIntervalSplit.modify(5, -4);
        System.out.println(treeArrayForIntervalSplit.query(2));
        System.out.println(treeArrayForIntervalSplit.query(3));
        System.out.println(treeArrayForIntervalSplit.query(4));
        System.out.println(treeArrayForIntervalSplit.query(5));

    }

}

class TreeArrayForIntervalSplit {

    int[] h, a, b;
    int len;

    public TreeArrayForIntervalSplit(int[] nums){
        a = nums;
        this.len = nums.length;
        b = new int[nums.length];
        h = new int[nums.length + 1];
        b[0] = a[0];
        modify(1, b[0]);
        for (int i = 1; i < nums.length; i++) {
            b[i] = a[i] - a[i-1];
            modify(i+1, b[i]);
        }
    }

    private int lowBit(int index){return index & (-index);}

    // index从1开始
    public void modify(int index, int increment){
        while(index <= len){
            h[index] += increment;
            index += lowBit(index);
        }
    }


    // index从1开始
    public int query(int index){
        int ans = 0;
        while(index > 0){
            ans += h[index];
            index -= lowBit(index);
        }

        return ans;
    }


}