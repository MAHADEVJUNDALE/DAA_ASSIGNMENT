import java.util.*;
 
public class TSPBranchAndBound {
 
    static final int INF = 1_000_000;
    static int n;
    static int[][] cost, time;
    static int timeLimit;
    static long generated = 0, pruned = 0;
 
    // A node of the search tree
    static class Node implements Comparable<Node> {
        int[][] matrix;
        int lb, city, level, elapsed;
        long id;
        List<Integer> path;
 
        public int compareTo(Node o) {
            if (lb != o.lb) return Integer.compare(lb, o.lb);
            if (level != o.level) return Integer.compare(o.level, level);
            return Long.compare(id, o.id);
        }
    }
 
    // Row and column reduction. Returns the total amount subtracted.
    static int reduce(int[][] m) {
        int total = 0;
        for (int i = 0; i < n; i++) {
            int min = INF;
            for (int j = 0; j < n; j++) min = Math.min(min, m[i][j]);
            if (min != INF && min > 0) {
                for (int j = 0; j < n; j++) if (m[i][j] != INF) m[i][j] -= min;
                total += min;
            }
        }
        for (int j = 0; j < n; j++) {
            int min = INF;
            for (int i = 0; i < n; i++) min = Math.min(min, m[i][j]);
            if (min != INF && min > 0) {
                for (int i = 0; i < n; i++) if (m[i][j] != INF) m[i][j] -= min;
                total += min;
            }
        }
        return total;
    }
 
    static int[][] copy(int[][] m) {
        int[][] c = new int[n][];
        for (int i = 0; i < n; i++) c[i] = m[i].clone();
        return c;
    }
 
    static void solve(int start) {
        int bestCost = INF;
        List<Integer> bestPath = null;
        long seq = 0;
 
        int[][] rootMatrix = copy(cost);
        for (int i = 0; i < n; i++) rootMatrix[i][i] = INF;
 
        Node root = new Node();
        root.matrix = rootMatrix;
        root.lb = reduce(rootMatrix);
        root.city = start;
        root.level = 1;
        root.elapsed = 0;
        root.id = seq++;
        root.path = new ArrayList<>(List.of(start));
        generated++;
 
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(root);
 
        while (!pq.isEmpty()) {
            Node node = pq.poll();
            if (node.lb >= bestCost) { pruned++; continue; }
 
            int u = node.city;
            for (int v = 0; v < n; v++) {
                if (node.path.contains(v)) continue;
 
                int arrival = node.elapsed + time[u][v];
                boolean last = (node.level + 1 == n);
                if (last) arrival += time[v][start];
                generated++;
                if (arrival > timeLimit) { pruned++; continue; }
 
                int[][] m = copy(node.matrix);
                int edge = node.matrix[u][v];
                for (int k = 0; k < n; k++) { m[u][k] = INF; m[k][v] = INF; }
                if (!last) m[v][start] = INF;
                int childLb = node.lb + edge + reduce(m);
 
                if (childLb >= bestCost) { pruned++; continue; }
 
                if (last) {
                    bestCost = childLb;
                    bestPath = new ArrayList<>(node.path);
                    bestPath.add(v);
                    bestPath.add(start);
                    continue;
                }
 
                Node child = new Node();
                child.matrix = m;
                child.lb = childLb;
                child.city = v;
                child.level = node.level + 1;
                child.elapsed = node.elapsed + time[u][v];
                child.id = seq++;
                child.path = new ArrayList<>(node.path);
                child.path.add(v);
                pq.add(child);
            }
        }
 
        if (bestPath == null) {
            System.out.println("No feasible route exists within the time limit.");
            return;
        }
        int totalTime = 0;
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < bestPath.size(); i++) {
            route.append(bestPath.get(i) + 1);
            if (i + 1 < bestPath.size()) {
                route.append(" -> ");
                totalTime += time[bestPath.get(i)][bestPath.get(i + 1)];
            }
        }
        System.out.println();
        System.out.println("Minimum Fuel Cost: " + bestCost);
        System.out.println("Optimal Delivery Route: " + route);
        System.out.println("Total Travel Time: " + totalTime + " hours");
        System.out.println("Nodes generated: " + generated
                + ", Nodes pruned: " + pruned);
    }
 
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of cities: ");
        n = sc.nextInt();
 
        cost = new int[n][n];
        time = new int[n][n];
        System.out.println("Enter fuel cost matrix (" + n + " x " + n
                + "), use 0 on the diagonal:");
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) cost[i][j] = sc.nextInt();
 
        System.out.println("Enter travel time matrix in hours ("
                + n + " x " + n + "):");
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) time[i][j] = sc.nextInt();
 
        System.out.print("Enter maximum allowed time (hours): ");
        timeLimit = sc.nextInt();
        System.out.print("Enter starting city (1-" + n + "): ");
        int start = sc.nextInt() - 1;
 
        solve(start);
        sc.close();
    }
}
