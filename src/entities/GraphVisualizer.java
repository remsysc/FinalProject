
class GraphVisualizer {
  CityGraph graph;

  GraphVisualizer(CityGraph g) {
    this.graph = g;
  }

  void renderMap() {
    System.out.println("=== City Graph (ASCII) ===");
    for (int i = 0; i < graph.nodeCount; i++) {
      GraphNode n = graph.nodes[i];
      System.out.print(n.name + ": ");
      for (int e = 0; e < n.edgeCount; e++) {
        System.out.print("--(" + n.edges[e].weight + ")--> " + n.edges[e].to.name + "  ");
      }
      System.out.println();
    }
  }

  void highlightRoute(PathResult path) {
    if (path == null) {
      System.out.println("No route to highlight.");
      return;
    }
    System.out.println("=== Active Route ===");
    System.out.println(path.describe());
    System.out.println("====================");
  }
}
