package entities.graph;

public class GraphEdge {
    public final GraphNode to;
    public final int weight;

    public GraphEdge(GraphNode to, int weight) {
        this.to = to;
        this.weight = weight;
    }
}
