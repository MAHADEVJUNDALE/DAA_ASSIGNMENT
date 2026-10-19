
import java.util.*;

public class MultistageGraph {

    static long solve(int n, List<int[]>[] adj, int source, int sink,
                      long[] cost, int[] next) {

        Arrays.fill(cost, Long.MAX_VALUE);
        Arrays.fill(next, -1);

        cost[sink] = 0;

        for (int v = sink - 1; v >= source; v--) {
            for (int[] edge : adj[v]) {
                int to = edge[0];
                int weight = edge[1];

                if (cost[to] != Long.MAX_VALUE &&
                    weight + cost[to] < cost[v]) {

                    cost[v] = weight + cost[to];
                    next[v] = to;
                }
            }
        }

        return cost[source];
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of stages: ");
        int stages = sc.nextInt();

        System.out.print("Enter total number of vertices: ");
        int n = sc.nextInt();

        System.out.print("Enter total number of edges: ");
        int m = sc.nextInt();

        List<int[]>[] adj = new ArrayList[n + 1];

        for (int i = 1; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }

        System.out.println("Enter edges (u v weight):");

        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            int w = sc.nextInt();

            if (u < 1 || u > n || v < 1 || v > n) {
                System.out.println("Invalid vertex number.");
                sc.close();
                return;
            }

            if (v <= u) {
                System.out.println(
                    "Invalid edge: vertices must follow stage order."
                );
                sc.close();
                return;
            }

            adj[u].add(new int[]{v, w});
        }

        System.out.print("Enter source vertex: ");
        int source = sc.nextInt();

        System.out.print("Enter sink vertex: ");
        int sink = sc.nextInt();

        if (source < 1 || source > n ||
            sink < 1 || sink > n || source >= sink) {
            System.out.println("Invalid source or sink.");
            sc.close();
            return;
        }

        long[] cost = new long[n + 1];
        int[] next = new int[n + 1];

        long minCost = solve(n, adj, source, sink, cost, next);

        System.out.println();

        if (minCost == Long.MAX_VALUE) {
            System.out.println("No path exists from source to sink.");
        } else {
            System.out.println("Minimum Delivery Cost: " + minCost);
            System.out.print("Optimal Delivery Route: ");

            int v = source;

            while (v != sink) {
                System.out.print(v + " -> ");
                v = next[v];

                if (v == -1) {
                    System.out.println("No valid route.");
                    sc.close();
                    return;
                }
            }

            System.out.println(sink);
        }

        sc.close();
    }
}
