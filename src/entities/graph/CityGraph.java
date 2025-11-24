package entities.graph;

public class CityGraph {
    public GraphNode[] nodes;
    public int nodeCount;

    public CityGraph(int initialCapacity) {
        nodes = new GraphNode[initialCapacity];
        nodeCount = 0;
    }

    public GraphNode addNode(String name) {
        GraphNode existing = getNode(name);
        if (existing != null) return existing;

        if (nodeCount >= nodes.length) {
            GraphNode[] newArr = new GraphNode[nodes.length * 2 + 1];
            for (int i = 0; i < nodes.length; i++)
                newArr[i] = nodes[i];
            nodes = newArr;
        }

        GraphNode n = new GraphNode(name, 4, nodeCount);
        nodes[nodeCount++] = n;
        return n;
    }

    public void addEdge(String from, String to, int weight, boolean bidirectional) {
        GraphNode f = getNode(from);
        GraphNode t = getNode(to);
        if (f == null) f = addNode(from);
        if (t == null) t = addNode(to);

        f.addEdge(new GraphEdge(t, weight));
        if (bidirectional) t.addEdge(new GraphEdge(f, weight));
    }

    public GraphNode getNode(String name) {
        for (int i = 0; i < nodeCount; i++) {
            if (nodes[i].name.equals(name)) return nodes[i];
        }
        return null;
    }
}
