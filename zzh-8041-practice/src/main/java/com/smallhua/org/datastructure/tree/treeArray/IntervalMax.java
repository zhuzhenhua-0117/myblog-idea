package com.smallhua.org.datastructure.tree.treeArray;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author ZZH
 * @create 2022/10/8
 * @since 1.0.0
 */
public class IntervalMax {

    public static void main(String[] args) {
        int[] nums = new int[]{1,5,6,2,9,56,85,66,45,32,4,1,1,1,3};
        TreeArrayForIntervalMax treeArrayForIntervalMax = new TreeArrayForIntervalMax(nums);
        System.out.println(treeArrayForIntervalMax.query(0, nums.length));

    }

}

class TreeArrayForIntervalMax {

    private int[] a, h;
    private int n;

    public TreeArrayForIntervalMax(int[] nums){
        this.a = nums;
        this.n = nums.length;
        h = new int[nums.length+1];
        for (int i = 0; i < nums.length; i++) {
            update(i+1);
        }
    }

    // 注意树状数组一定从1开始 从0开始的话会死循环
    public void update(int index){
        while(index <= n){
            h[index] = a[index-1];

            int hl = lowBit(index);
            for (int i = 1; i <= hl; i = i << 1) {
                h[index] = Math.max(h[index], h[index-i]);
            }
            index += hl;
        }
    }

    public int query(int begin, int end){
        int i = begin + 1, j = end + 1;
        int ans = 0;
        while(i <= j){
            ans = Math.max(ans, h[end]);
            j -= lowBit(j);
        }

        return ans;
    }

    private int lowBit(int index){
        return index & (-index);
    }
}

