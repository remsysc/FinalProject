
/**
 * Custom graph: arrays for nodes and edges, custom Dijkstra using arrays.
 * No java.util used.
 */
class GraphNode {
  String name;
  GraphEdge[] edges;
  int edgeCount;

  GraphNode(String name, int maxEdges) {
    this.name = name;
    this.edges = new GraphEdge[maxEdges];
    this.edgeCount = 0;
  }

  void addEdge(GraphEdge e) {
    if (edgeCount < edges.length) {
      edges[edgeCount++] = e;
    } else {
      // Basic grow
      GraphEdge[] newArr = new GraphEdge[edges.length * 2 + 1];
      for (int i = 0; i < edges.length; i++)
        newArr[i] = edges[i];
      edges = newArr;
      edges[edgeCount++] = e;
    }
  }
}

class GraphEdge {
  GraphNode to;
  int weight; // distance in meters or time seconds

  GraphEdge(GraphNode to, int weight) {
    this.to = to;
    this.weight = weight;
  }
}

class PathResult {
  String[] nodes;
  int length; // number of nodes in path
  int totalCost;

  PathResult(int capacity) {
    nodes = new String[capacity];
    length = 0;
    totalCost = 0;
  }

  void pushFront(String nodeName) {
    // shift right
    for (int i = length; i > 0; i--)
      nodes[i] = nodes[i - 1];
    nodes[0] = nodeName;
    length++;
  }

  String describe() {
    String s = "Path(cost=" + totalCost + "): ";
    for (int i = 0; i < length; i++) {
      s += nodes[i];
      if (i < length - 1)
        s += " -> ";
    }
    return s;
  }
}

class CityGraph {
  GraphNode[] nodes;
  int nodeCount;

  CityGraph(int initialCapacity) {
    nodes = new GraphNode[initialCapacity];
    nodeCount = 0;
  }

  GraphNode addNode(String name) {
    // Check exists
    GraphNode existing = getNode(name);
    if (existing != null)
      return existing;
    if (nodeCount >= nodes.length) {
      GraphNode[] newArr = new GraphNode[nodes.length * 2 + 1];
      for (int i = 0; i < nodes.length; i++)
        newArr[i] = nodes[i];
      nodes = newArr;
    }
    GraphNode n = new GraphNode(name, 4);
    nodes[nodeCount++] = n;
    return n;
  }

  void addEdge(String from, String to, int weight, boolean bidirectional) {
    GraphNode f = getNode(from);
    GraphNode t = getNode(to);
    if (f == null)
      f = addNode(from);
    if (t == null)
      t = addNode(to);
    f.addEdge(new GraphEdge(t, weight));
    if (bidirectional)
      t.addEdge(new GraphEdge(f, weight));
  }

  GraphNode getNode(String name) {
    for (int i = 0; i < nodeCount; i++) {
      if (nodes[i].name.equals(name))
        return nodes[i];
    }
    return null;
  }

  /**
   * Custom Dijkstra using arrays:
   * - distances[] int
   * - visited[] boolean (int 0/1)
   * - prev[] indexes
   * - linear search for min (no heap)
   */
  PathResult shortestPath(String startName, String endName) {
    GraphNode start = getNode(startName);
    GraphNode end = getNode(endName);
    if (start == null || end == null)
      return null;

    int N = nodeCount;
    int[] dist = new int[N];
    int[] visited = new int[N];
    int[] prev = new int[N];

    for (int i = 0; i < N; i++) {
      dist[i] = Integer.MAX_VALUE;
      visited[i] = 0;
      prev[i] = -1;
    }
    int startIdx = indexOf(start);
    int endIdx = indexOf(end);
    dist[startIdx] = 0;

    for (int k = 0; k < N; k++) {
      int u = -1;
      int best = Integer.MAX_VALUE;
      for (int i = 0; i < N; i++) {
        if (visited[i] == 0 && dist[i] < best) {
          best = dist[i];
          u = i;
        }
      }
      if (u == -1)
        break; // disconnected
      if (u == endIdx)
        break; // reached end
      visited[u] = 1;

      GraphNode nodeU = nodes[u];
      for (int e = 0; e < nodeU.edgeCount; e++) {
        GraphEdge edge = nodeU.edges[e];
        int v = indexOf(edge.to);
        if (visited[v] == 1)
          continue;
        long cand = (long) dist[u] + (long) edge.weight;
        if (cand < dist[v]) {
          dist[v] = (int) cand;
          prev[v] = u;
        }
      }
    }

    if (dist[endIdx] == Integer.MAX_VALUE)
      return null;

    // Reconstruct
    PathResult path = new PathResult(N);
    int cur = endIdx;
    while (cur != -1) {
      path.pushFront(nodes[cur].name);
      cur = prev[cur];
    }
    path.totalCost = dist[endIdx];
    return path;
  }

  private int indexOf(GraphNode n) {
    for (int i = 0; i < nodeCount; i++)
      if (nodes[i] == n)
        return i;
    return -1;
  }
}
