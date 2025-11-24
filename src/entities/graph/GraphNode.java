package entities.graph;

/**
 * Custom graph: arrays for nodes and edges, custom Dijkstra using arrays.
 * No java.util used.
 */

public class GraphNode {
    public final String name;
    public GraphEdge[] edges;
    public int edgeCount;
    public int index; // cached index for fast lookup

    public GraphNode(String name, int maxEdges, int index) {
        this.name = name;
        this.edges = new GraphEdge[maxEdges];
        this.edgeCount = 0;
        this.index = index;
    }

    public void addEdge(GraphEdge e) {
        // prevent duplicates
        for (int i = 0; i < edgeCount; i++) {
            if (edges[i].to == e.to && edges[i].weight == e.weight) {
                return;
            }
        }
        if (edgeCount >= edges.length) {
            GraphEdge[] newArr = new GraphEdge[edges.length * 2 + 1];
            for (int i = 0; i < edges.length; i++)
                newArr[i] = edges[i];
            edges = newArr;
        }
        edges[edgeCount++] = e;
    }
}
