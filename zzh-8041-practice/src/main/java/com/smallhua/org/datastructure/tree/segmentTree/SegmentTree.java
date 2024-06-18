package com.smallhua.org.datastructure.tree.segmentTree;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author ZZH
 * @create 2024/6/18
 * @since 1.0.0
 */
public class SegmentTree {
    private int[] data;

    private int[] segmentTreeData;

    public SegmentTree(int[] data){
        this.data = data;
        this.segmentTreeData = new int[data.length * 4];
        buildInit(0, 0, data.length - 1);
    }

    public void buildInit(int node, int start, int end){
        if (start == end){
            segmentTreeData[node] = data[start];
            return;
        }
        int leftIndex = node*2 + 1;
        int rightIndex = node*2 + 2;

        int mid = start + (end - start)/2;

        buildInit(leftIndex, start, mid);
        buildInit(rightIndex, mid + 1, end);

        segmentTreeData[node] = segmentTreeData[leftIndex] + segmentTreeData[rightIndex];

    }

    public int query(int node, int start, int end, int queryStart, int queryEnd) {
        if (end < queryStart || start > queryEnd) {
            return 0;
        }

        if (queryStart <= start && end <= queryEnd) {
            return segmentTreeData[node];
        }

        int mid = start + (end - start) / 2;
        int leftNode = node * 2 + 1;
        int rightNode = node * 2 + 2;

        return query(leftNode, start, mid, queryStart, queryEnd) +
                query(rightNode, mid + 1, end, queryStart, queryEnd);
    }

    public void update(int node, int start, int end, int index, int newVal) {
        if (start == end) {
            segmentTreeData[node] = newVal;
            return;
        }

        int mid = start + (end - start) / 2;
        int leftNode = node * 2 + 1;
        int rightNode = node * 2 + 2;

        if (index >= start && index <= mid) {
            update(leftNode, start, mid, index, newVal);
        } else {
            update(rightNode, mid + 1, end, index, newVal);
        }

        segmentTreeData[node] = segmentTreeData[leftNode] + segmentTreeData[rightNode];
    }

    public static void main(String[] args) {
        int[] arr = {1, 3, 5, 7, 9};
        SegmentTree segmentTree = new SegmentTree(arr);

        // Query sum of values in range [2, 4]
        System.out.println(segmentTree.query(0, 0, arr.length - 1, 2, 4));

        // Update value at index 2 to 10
        segmentTree.update(0, 0, arr.length - 1, 2, 10);

        // Query sum of values in range [2, 4] again
        System.out.println(segmentTree.query(0, 0, arr.length - 1, 2, 4));
    }
}