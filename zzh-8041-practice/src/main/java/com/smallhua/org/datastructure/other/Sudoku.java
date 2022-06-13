package com.smallhua.org.datastructure.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈填充数独〉
 *
 * @author ZZH
 * @create 2021/9/1
 * @since 1.0.0
 */
public class Sudoku {

    volatile boolean flag = false;

    /**
     * @param nums 9X9的矩阵
     * @return
     */
    public char[][] fillData(char[][] nums) {

        //用于标记某一行的1-9（0-8）那些被使用了
        boolean[][] rows = new boolean[9][9];
        //用于标记某一列的1-9（0-8）那些被使用了
        boolean[][] cols = new boolean[9][9];
        //用于标记某一块的1-9（0-8）那些被使用了
        boolean[][][] block = new boolean[3][3][9];

        List<int[]> spaces = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                char num = nums[i][j];
                if (num == '.') {
                    spaces.add(new int[]{i, j});
                } else {
                    int digit = num - '0';
                    rows[i][digit-1] = cols[j][digit-1] = block[i / 3][j / 3][digit-1] = true;
                }
            }
        }

        dfs(nums, 0, rows, cols, block, spaces);
        return nums;
    }

    private void dfs(char[][] nums, int pos, boolean[][] rows, boolean[][] cols, boolean[][][] block, List<int[]> spaces) {
        if (pos == spaces.size()) {
            //加这个很重要 控制不让方法找活结点 继续递归下去
            flag = true;
            return;
        }
        int[] indexs = spaces.get(pos);
        int row = indexs[0], col = indexs[1];
        for (int digit = 0; digit < 9 && !flag; digit++) {
            if (rows[row][digit] || cols[col][digit] || block[row / 3][col / 3][digit])
                continue;
            nums[row][col] = (char) (digit + '1');
            rows[row][digit] = cols[col][digit] = block[row / 3][col / 3][digit] = true;
            dfs(nums, pos + 1, rows, cols, block, spaces);
//            nums[row][col] = '.';
            rows[row][digit] = cols[col][digit] = block[row / 3][col / 3][digit] = false;
        }
    }

    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        char[][] chars = {
                {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
        };
        System.out.println(Arrays.deepToString(sudoku.fillData(chars)));
    }
}