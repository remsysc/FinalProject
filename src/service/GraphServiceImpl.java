package service;


import entities.graph.CityGraph;
import entities.graph.GraphEdge;
import entities.graph.GraphNode;
import entities.graph.PathResult;

public class GraphServiceImpl {

    private  final CityGraph graph;

    public GraphServiceImpl(CityGraph graph){
        this.graph = graph;
    }


    public PathResult shortestPath( String startName, String endName) {
        GraphNode start = graph.getNode(startName);
        GraphNode end = graph.getNode(endName);
        if (start == null || end == null) return null;

        int N = graph.nodeCount;
        int[] dist = new int[N];
        int[] visited = new int[N];
        int[] prev = new int[N];

        for (int i = 0; i < N; i++) {
            dist[i] = Integer.MAX_VALUE;
            visited[i] = 0;
            prev[i] = -1;
        }

        dist[start.index] = 0;

        for (int k = 0; k < N; k++) {
            int u = -1;
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < N; i++) {
                if (visited[i] == 0 && dist[i] < best) {
                    best = dist[i];
                    u = i;
                }
            }
            if (u == -1) break;
            if (u == end.index) break;
            visited[u] = 1;

            GraphNode nodeU = graph.nodes[u];
            for (int e = 0; e < nodeU.edgeCount; e++) {
                GraphEdge edge = nodeU.edges[e];
                int v = edge.to.index;
                if (visited[v] == 1) continue;
                long cand = (long) dist[u] + (long) edge.weight;
                if (cand < dist[v]) {
                    dist[v] = (int) cand;
                    prev[v] = u;
                }
            }
        }

        if (dist[end.index] == Integer.MAX_VALUE) return null;

        // reconstruct path
        int[] stack = new int[N];
        int len = 0;
        for (int cur = end.index; cur != -1; cur = prev[cur]) {
            stack[len++] = cur;
        }

        PathResult path = new PathResult(len);
        for (int i = len - 1; i >= 0; i--) {
            path.addNode(graph.nodes[stack[i]].name);
        }
        path.totalCost = dist[end.index];
        return path;
    }
}
