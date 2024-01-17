import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class FordFulkerson {
    // Number of vertices in the given graph
    Vector<Edge> edges;
    /* Returns true if there is a path from source 's' to sink
    't' in the residual graph. Also fills parent[] to store the
    path */
    private static boolean bfs(int rGraph[][], int s, int t, int parent[]) {
        // Create a visited array and mark all vertices as not visited
        boolean visited[] = new boolean[rGraph.length];
        Arrays.fill(visited, false);

        // Create a queue, enqueue source vertex, and mark the source
        // vertex as visited
        Queue<Integer> q = new LinkedList<>();
        q.add(s);
        visited[s] = true;
        parent[s] = -1;

        // Standard BFS Loop
        while (!q.isEmpty()) {
            int u = q.poll();

            for (int v = 0; v < rGraph.length; v++) {
                if (!visited[v] && rGraph[u][v] > 0) {
                    // If we find a connection to the sink node,
                    // then there is no point in BFS anymore.
                    // We just have to set its parent and can return true
                    if (v == t) {
                        parent[v] = u;
                        return true;
                    }
                    q.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        // We didn't reach the sink in BFS starting from the source, so return false
        return false;
    }

    // Returns the maximum flow from s to t in the given graph
    public static Vector<Edge> fordFulkerson(Vector<Edge> edges, Node source, Node sink, int dim) {
        int u, v;
        int V = dim;
        int graph[][] = new int[V][V];

        for (Edge e : edges) {
            u = e.getStart().getID() - 1;
            v = e.getEnd().getID() - 1;
            graph[u][v] = e.getWeight();
        }

        int s = source.getID()-1;
        int t = sink.getID()-1;

        // Create a residual graph and fill the residual graph
        // with given capacities in the original graph as
        // residual capacities in the residual graph
        int rGraph[][] = new int[V][V];
        for (u = 0; u < V; u++)
            for (v = 0; v < V; v++)
                rGraph[u][v] = graph[u][v];

        int parent[] = new int[V]; // This array is filled by BFS and to store the path

        int max_flow = 0; // There is no flow initially

        // Augment the flow while there is a path from source to sink
        while (bfs(rGraph, s, t, parent)) {
            // Find the minimum residual capacity of the edges along
            // the path filled by BFS. Or we can say find the
            // maximum flow through the path found.
            int path_flow = Integer.MAX_VALUE;
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                path_flow = Math.min(path_flow, rGraph[u][v]);
            }

            // Update residual capacities of the edges and
            // reverse edges along the path
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                rGraph[u][v] -= path_flow;
                rGraph[v][u] += path_flow;
            }

            // Add path flow to overall flow
            max_flow += path_flow;
        }

        //print the edges with flow

        Vector<Edge> flowEdges = new Vector<Edge>();

        //System.out.println("Edges with flow:");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++)
                if (graph[i][j] > 0 && rGraph[i][j] < graph[i][j]) {
                    //System.out.println("Edge " + (i + 1) + " -> " + (j + 1) + " Flow: " + (graph[i][j] - rGraph[i][j]));
                    flowEdges.add(new Edge(new Node(i + 1), new Node(j + 1), graph[i][j] - rGraph[i][j]));
                }
        }

        System.out.println("Max flow: " + max_flow);

        // Return the overall flow
        return flowEdges;
    }
}
