package service;


import entities.graph.CityGraph;
import entities.graph.GraphNode;
import entities.graph.PathResult;

public class GraphVisualizer {
    private  final CityGraph graph;

   public GraphVisualizer(CityGraph g) {
        this.graph = g;
    }

    void renderMap() {
        System.out.println("=== City Graph (ASCII) ===");

        for(int i = 0; i < this.graph.nodeCount; ++i) {
            GraphNode n = this.graph.nodes[i];
            System.out.print(n.name + ": ");

            for(int e = 0; e < n.edgeCount; ++e) {
                int var10001 = n.edges[e].weight;
                System.out.print("--(" + var10001 + ")--> " + n.edges[e].to.name + "  ");
            }

            System.out.println();
        }

    }

    void highlightRoute(PathResult path) {
        if (path == null) {
            System.out.println("No route to highlight.");
        } else {
            System.out.println("=== Active Route ===");
            System.out.println(path.describe());
            System.out.println("====================");
        }
    }
}
