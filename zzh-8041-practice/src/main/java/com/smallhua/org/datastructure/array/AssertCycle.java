package com.smallhua.org.datastructure.array;

/**
 * 〈一句话功能简述〉<br>
 * 〈判断数组是否存在环〉
 *
 * @author ZZH
 * @create 2021/8/12
 * @since 1.0.0
 */
public class AssertCycle {

    public boolean hasCycle(int[] nums){
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            boolean isForward = nums[i] > 0;
            int count = 0;
            int next = i;
            while (count < n) {
                count++;
                next = ((nums[next] + next)%n + n)%n;
                if (isForward && nums[next] < 0) break;
                if (!isForward && nums[next] > 0) break;
                if (next == i) return count > 1;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        AssertCycle a = new AssertCycle();
//        int[] nums = {2,-1,1,2,2};
//        int[] nums = {1,1};
        int[] nums = {-2,1,-1,-2,-2};
        System.out.println(a.hasCycle(nums));
    }

}