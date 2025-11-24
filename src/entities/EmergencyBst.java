
class EmergencyNode {
  Emergency data;
  EmergencyNode left, right;

  EmergencyNode(Emergency e) {
    this.data = e;
  }
}

/**
 * Orders by severity desc, tie-break by createdAtMs asc.
 * We implement a BST insert/search/remove without java.util.
 */
class EmergencyBST {
  EmergencyNode root;

  void insert(Emergency e) {
    root = insertRec(root, e);
  }

  private EmergencyNode insertRec(EmergencyNode node, Emergency e) {
    if (node == null)
      return new EmergencyNode(e);
    int cmp = compare(e, node.data);
    if (cmp < 0)
      node.left = insertRec(node.left, e);
    else
      node.right = insertRec(node.right, e);
    return node;
  }

  // cmp < 0 => e should go left (higher priority first)
  private int compare(Emergency a, Emergency b) {
    if (a.severity != b.severity)
      return (b.severity - a.severity); // descending severity
    if (a.createdAtMs != b.createdAtMs)
      return (a.createdAtMs < b.createdAtMs) ? -1 : 1;
    return 0;
  }

  // Get highest priority = leftmost
  Emergency peekHighestPriority() {
    if (root == null)
      return null;
    EmergencyNode cur = root;
    while (cur.left != null)
      cur = cur.left;
    return cur.data;
  }

  // Remove a specific emergency (by identity fields)
  void remove(Emergency target) {
    root = removeRec(root, target);
  }

  private EmergencyNode removeRec(EmergencyNode node, Emergency target) {
    if (node == null)
      return null;
    int cmp = compare(target, node.data);
    if (cmp < 0)
      node.left = removeRec(node.left, target);
    else if (cmp > 0)
      node.right = removeRec(node.right, target);
    else {
      // Found
      if (node.left == null)
        return node.right;
      if (node.right == null)
        return node.left;
      // Replace with inorder successor
      EmergencyNode succ = node.right;
      while (succ.left != null)
        succ = succ.left;
      node.data = succ.data;
      node.right = removeRec(node.right, succ.data);
    }
    return node;
  }

  // Simple in-order traversal dump
  void printInOrder() {
    printRec(root);
  }

  private void printRec(EmergencyNode node) {
    if (node == null)
      return;
    printRec(node.left);
    System.out.println("BST: " + node.data.summary());
    printRec(node.right);
  }
}
