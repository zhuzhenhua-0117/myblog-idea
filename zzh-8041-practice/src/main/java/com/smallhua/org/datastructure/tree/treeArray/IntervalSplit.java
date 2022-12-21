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
        treeArrayForIntervalSplit.update(2, 4);
        treeArrayForIntervalSplit.update(5, -4);
        System.out.println(treeArrayForIntervalSplit.query(2));
        System.out.println(treeArrayForIntervalSplit.query(3));
        System.out.println(treeArrayForIntervalSplit.query(4));
        System.out.println(treeArrayForIntervalSplit.query(5));

    }

}

class TreeArrayForIntervalSplit {

    int destArray[], len, nums[];

    public TreeArrayForIntervalSplit(int[] nums){
        this.nums = nums;
        len = nums.length;
        destArray = new int[len + 1];

        for (int i = 0; i < len; i++) {
            update(i, nums[i] - ((i-1) >= 0 ? nums[i-1] : 0));
        }
    }

    public void update(int index, int diff){
        int treeIndex = index + 1;

        while (treeIndex <= len) {
            destArray[treeIndex] += diff;

            treeIndex += lowBit(treeIndex);
        }

    }
    public int query(int index){
        int ret = 0, treeIndex = index + 1;
        while(treeIndex > 0){
            ret += destArray[treeIndex];
            treeIndex -= lowBit(treeIndex);
        }
        return ret;
    }


    private int lowBit(int treeIndex){
        return  treeIndex&(-treeIndex);
    }


}