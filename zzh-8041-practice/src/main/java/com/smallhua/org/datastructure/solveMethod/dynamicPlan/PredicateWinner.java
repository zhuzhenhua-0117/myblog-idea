package com.smallhua.org.datastructure.solveMethod.dynamicPlan;

/**
 * 〈一句话功能简述〉<br>
 * 〈预测赢家〉
 *
 * @author ZZH
 * @create 2022/11/3
 * @since 1.0.0
 */
public class PredicateWinner
{

    public static void main(String[] args) {
//        int[] nums = new int[]{1,5,233,7};
        int[] nums = new int[]{1,5,2};
        System.out.println(predictWinner(nums));

    }

    public static boolean predictWinner(int[] nums){
        int n = nums.length;
        int[][] dp = new int[n][n];

        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }

        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                dp[i][j] = Math.max(nums[i] - dp[i+1][j], nums[j] - dp[i][j - 1]);
            }
        }

        return dp[0][n-1] >= 0;
    }
}