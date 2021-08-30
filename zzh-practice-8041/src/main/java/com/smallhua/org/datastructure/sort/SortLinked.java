package com.smallhua.org.datastructure.sort;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 〈一句话功能简述〉<br>
 * 〈排序链表〉
 *
 * @author ZZH
 * @create 2021/8/5
 * @since 1.0.0
 */
public class SortLinked {
    public static void main(String[] args) {
        ListNode node = new ListNode(2);
        ListNode node1 = new ListNode(5);
        ListNode node2 = new ListNode(8);
        ListNode node3 = new ListNode(6);
        ListNode node4 = new ListNode(1);
        node.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;

        ListNode listNode = sortListNode(node);
        while (listNode != null) {
            System.out.println(listNode.value);
            listNode = listNode.next;
        }
    }

    public static ListNode sortListNode(ListNode head) {
        ListNode pre = new ListNode(0);
        ListNode cur = pre;
        PriorityQueue<ListNode> queue = new PriorityQueue<ListNode>(new Comparator<ListNode>() {
            @Override
            public int compare(ListNode o1, ListNode o2) {
                return o1.value - o2.value;
            }
        });

        int num = 0;
        while (head != null) {
            queue.offer(head);
            num++;
            head = head.next;
        }

        for (int i = 0; i <= num; i++) {
            cur.next = queue.poll();
            cur = cur.next;
        }

        return pre.next;
    }
}

class ListNode {
    Integer value;
    ListNode next;

    public ListNode(Integer value) {
        this.value = value;
    }
}