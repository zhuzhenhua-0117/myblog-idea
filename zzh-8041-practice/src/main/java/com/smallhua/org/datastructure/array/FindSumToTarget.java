package com.smallhua.org.datastructure.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 四指针法
 * 〈找出和为target的四个元素〉
 *
 * @author ZZH
 * @create 2021/8/12
 * @since 1.0.0
 */
public class FindSumToTarget {

    public List<List<Integer>> fourSumToTarget(int[] nums, int target){
        List<List<Integer>> ret = new ArrayList<>();
        Arrays.sort(nums);
        int n = nums.length;
        for (int i = 0; i < n-3; i++) {
            if (i > 0 && nums[i] == nums[i-1]) {
                continue;
            }
            for (int j = i + 1; j < n-2; j++){
                if (j > i + 1 && nums[j] == nums[j-1]) {
                    continue;
                }
                int left = j + 1, right = n - 1;
                int p1 = nums[i], p2 = nums[j];
                if (p1 + p2 + nums[n-2] + nums[n-1] < target) break;
                if (p1 + p2 + nums[j] + + nums[j+1] > target) break;
                while (left < right) {
                    int p3 = nums[left], p4 = nums[right];
                    if (p1 + p2 + p3 + p4 < target) {
                        left++;
                    }else if (p1 + p2 + p3 + p4 > target){
                        right--;
                    }else {
                        ret.add(Arrays.asList(p1, p2, p3, p4));
                        left++;
                        right--;
                        while (left < right && nums[left] == nums[left-1]) {
                            left++;
                        }

                        while (left < right && nums[right] == nums[right+1]) {
                            right--;
                        }
                    }
                }
            }

        }

        return ret;
    }

    public static void main(String[] args) {
        FindSumToTarget a = new FindSumToTarget();
//        int[] nums = {1,0,-1,0,-2,2};
        int[] nums = {2,2,2,2,2};
        System.out.println(a.fourSumToTarget(nums, 8).toString());
    }

}