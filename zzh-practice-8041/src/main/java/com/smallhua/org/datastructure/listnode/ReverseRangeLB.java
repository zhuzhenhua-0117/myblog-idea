package com.smallhua.org.datastructure.listnode;

/**
 * 〈一句话功能简述〉<br>
 * 〈反转区间链表〉
 *
 * @author ZZH
 * @create 2021/8/9
 * @since 1.0.0
 */
public class ReverseRangeLB {

    /**
     * 反转（left, right）区间的链表节点
     *
     * @param node
     * @param left
     * @param right
     * @return
     */
    public ListNode reverseLB(ListNode node, int left, int right) {
        ListNode dummyNode = new ListNode(-1);
        dummyNode.next = node;
        ListNode cur = dummyNode;
        ListNode firstTailNode = null, secondeHeadNode, secondeTailNode, thirdHeadNode;

        for (int i = 0; i < left - 1; i++) {
            cur = cur.next;
        }

        firstTailNode = cur;

        secondeHeadNode = firstTailNode.next;

        for (int i = left - 1; i < right; i++) {
            cur = cur.next;
        }

        secondeTailNode = cur;
        thirdHeadNode = cur.next;
        secondeTailNode.next = null;

        firstTailNode.next = reverse(secondeHeadNode);
        secondeHeadNode.next = thirdHeadNode;
        return dummyNode.next;
    }

    private ListNode reverse(ListNode node) {
        ListNode pre = null;
        ListNode cur = node;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }


    public static void main(String[] args) {
        ReverseRangeLB rangeLB = new ReverseRangeLB();
        ListNode head = new ListNode(1);
        ListNode cur = head;
        for (int i = 2; i <= 10; i++) {
            cur.next = new ListNode(i);
            cur = cur.next;
        }
        ListNode listNode = rangeLB.reverseLB(head, 1, 5);
        while (listNode != null) {
            System.out.println(listNode.val);
            listNode = listNode.next;
        }
    }
}