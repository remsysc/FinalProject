package service;

import entities.Emergency;
import entities.EmergencyBST;
import entities.EmergencyBST.EmergencyNode;

public class EmergencyBSTService {

    private  final EmergencyBST bst;

    public  EmergencyBSTService(){
        this.bst = new EmergencyBST();
    }

    // Insert
    public void insert( Emergency e) {
        bst.root = insertRec(bst.root, e);
    }

    private EmergencyNode insertRec(EmergencyNode node, Emergency e) {
        if (node == null) return new EmergencyNode(e);
        int cmp = compare(e, node.data);
        if (cmp < 0) node.left = insertRec(node.left, e);
        else node.right = insertRec(node.right, e);
        return node;
    }

    // Compare by severity desc, then createdAtMs asc
    private int compare(Emergency a, Emergency b) {
        if (a.severity != b.severity) return (b.severity - a.severity);
        if (a.createdAtMs != b.createdAtMs) return (a.createdAtMs < b.createdAtMs) ? -1 : 1;
        return 0;
    }

    // Peek highest priority (leftmost)
    public Emergency peekHighestPriority() {
        if (bst.root == null) return null;
        EmergencyNode cur = bst.root;
        while (cur.left != null) cur = cur.left;
        return cur.data;
    }

    // Remove
    public void remove( Emergency target) {
        bst.root = removeRec(bst.root, target);
    }

    private EmergencyNode removeRec(EmergencyNode node, Emergency target) {
        if (node == null) return null;
        int cmp = compare(target, node.data);
        if (cmp < 0) node.left = removeRec(node.left, target);
        else if (cmp > 0) node.right = removeRec(node.right, target);
        else {
            // Found
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            // Replace with inorder successor
            EmergencyNode succ = node.right;
            while (succ.left != null) succ = succ.left;
            node.data = succ.data;
            node.right = removeRec(node.right, succ.data);
        }
        return node;
    }

    // In-order traversal
    public void printInOrder() {
        printRec(bst.root);
    }

    private void printRec(EmergencyNode node) {
        if (node == null) return;
        printRec(node.left);
        System.out.println("BST: " + node.data.summary());
        printRec(node.right);
    }


}
