package entities;



/**
 * Orders by severity desc, tie-break by createdAtMs asc.
 * We implement a BST insert/search/remove without java.util.
 */
public class EmergencyBST {
    public EmergencyNode root;

    public static class EmergencyNode {
        public Emergency data;
        public EmergencyNode left;
        public EmergencyNode right;

        public EmergencyNode(Emergency e) {
            this.data = e;
        }
    }
}