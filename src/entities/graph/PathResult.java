package entities.graph;


public class PathResult {
    public final String[] nodes;
    public int length;
    public int totalCost;

    public PathResult(int capacity) {
        nodes = new String[capacity];
        length = 0;
        totalCost = 0;
    }

    public void addNode(String nodeName) {
        nodes[length++] = nodeName;
    }

    public String describe() {
        StringBuilder sb = new StringBuilder("Path(cost=" + totalCost + "): ");
        for (int i = 0; i < length; i++) {
            sb.append(nodes[i]);
            if (i < length - 1) sb.append(" -> ");
        }
        return sb.toString();
    }
}
