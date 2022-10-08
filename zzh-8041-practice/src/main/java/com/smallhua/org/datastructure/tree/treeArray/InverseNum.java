package com.smallhua.org.datastructure.tree.treeArray;

/**
 * 〈一句话功能简述〉<br>
 * 〈求逆数对〉
 *
 */
public class InverseNum {

    public static void main(String[] args) {
        int[] b = new int[]{7,5,6,1};
        TreeArray treeArray = new TreeArray(b);
        System.out.println(treeArray.getResult());

    }

}

class TreeArray{
    public int len;
    public int[] treeArrays;

    private int result;

    public TreeArray(int[] nums){
        int maxNum = 0;
        for (int i = 0; i < nums.length; i++) {
            maxNum = Math.max(maxNum, nums[i]);
        }
        treeArrays = new int[maxNum];
        this.len = maxNum;
        for (int i = 0; i < nums.length; i++) {
            result += (i-query(nums[i]));
            update(nums[i]);
        }
    }

    public void update(int p){
        while(p <= len){
            treeArrays[p-1]++;
            p += lowBit(p);
        }
    }

    public int query(int p){
        int sum = 0;
        while(p > 0){
            sum += treeArrays[p-1];
            p -= lowBit(p);
        }
        return sum;
    }

    private int lowBit(int p){
        return p&(-p);
    }

    public int getResult(){
        return result;
    }

}