package com.smallhua.org.datastructure.sort;

/**
 * 〈一句话功能简述〉<br>
 * 〈用快速排序找出最小的k个数〉
 *
 * @author ZZH
 * @create 2021/8/5
 * @since 1.0.0
 */
public class QuickSort_kthSmallest {
    public static void main(String[] args) {
        Solution_kthSmallest s = new Solution_kthSmallest();
        int[] nums = new int[]{1,9,5,6,8,4,3};
        int i = s.kthNum(nums, 5);
        System.out.println(i);
    }
}

class Solution_kthSmallest {
    public int kthNum(int[] nums, int target) {
        int res = 0;
        sortK(nums, target - 1);
        res = nums[target - 1];
        return res;
    }

    private void sortK(int[] nums, int k) {
        int p = 0, q = nums.length - 1;
        while (p < q) {
            int j = partitionNth(nums, q, p);
            if (j < k) {
                p = j;
            } else if (j > k) {
                q = j;
            } else {
                break;
            }
        }
    }

    private int partitionNth(int[] nums, int start, int end) {
        int i = start, j = end + 1;
        int pos = nums[start];
        while (i < j) {
            while (i < j && nums[--j] > pos) ;
            while (i < j && nums[++i] < pos) ;
            if (i < j)
                swap(nums, i, j);
            else
                break;
        }

        //起始位置要和比他小的进行交换 循环结束j下标的值小
        swap(nums, start, j);

        return j;
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}