package com.smallhua.org.datastructure.tree.treeArray;

/**
 * 〈区间修改区间查询〉<br>
 *
 * @author ZZH
 * @create 2022/10/10
 * @since 1.0.0
 */
public class IntervalUQ {

    public static void main(String[] args) {
        int[] nums = new int[]{1,5,6,4,3,5,2,2,99,88,66};
        PayContext payContext = new PayContext(nums);

        System.out.println(payContext.query(1));
        System.out.println(payContext.query(5));
        System.out.println(payContext.query(6));
        System.out.println(payContext.query(8));

        payContext.update(1, 1);
        payContext.update(8, -1);
        System.out.println(payContext.query(1));
        System.out.println(payContext.query(5));
        System.out.println(payContext.query(6));
        System.out.println(payContext.query(8));

    }

}

class PayContext{
    // 维护c[i]的前缀和，c[i]*i的前缀和 对应两列
    private int[][] c;
    private int[] a;
    private int len;
    
    public PayContext(int[] nums){
        a = nums;
        len = nums.length;
        
        c = new int[len+1][2];

        update(0, nums[0]);
        for (int i = 1; i < nums.length; i++) {
            update(i, nums[i] - nums[i - 1]);
        }
        
        
    }
    private int lowBit(int num){
        return num & (-num);
    }
    
    public void update(int index, int modifyNum){
        int hIndex = index + 1;
        while(hIndex <= len){
             c[hIndex][0] += modifyNum;
             c[hIndex][1] += modifyNum * index;
             hIndex += lowBit(hIndex);
         }
    }

    public int query(int index){
        int hIndex = index + 1;
        int ans, q = 0, p = 0;
        while(hIndex > 0){
            q += c[hIndex][0];
            p += c[hIndex][1];
            hIndex -= lowBit(hIndex);
        }
        ans = q*(index + 1) - p;
        return ans;
    }
}