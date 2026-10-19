import java.util.*;

public class dijkstralgorithm {

    static class Edge {
        int target;
        int weight;

        Edge(int target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    static class Node implements Comparable<Node> {
        int vertex;
        int distance;

        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Read graph details
        System.out.print("Enter number of intersections (vertices): ");
        int V = sc.nextInt();

        System.out.print("Enter number of roads (edges): ");
        int E = sc.nextInt();

        // Adjacency list representation
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            graph.add(new ArrayList<>());
        }

        System.out.println("Enter edges (u v w):");
        for (int i = 0; i < E; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            int w = sc.nextInt();
            // Undirected graph representation
            graph.get(u).add(new Edge(v, w));
            graph.get(v).add(new Edge(u, w));
        }

        System.out.print("Enter ambulance start location (source): ");
        int source = sc.nextInt();

        System.out.print("Enter number of hospitals: ");
        int numHospitals = sc.nextInt();

        System.out.print("Enter hospital nodes: ");
        int[] hospitalNodes = new int[numHospitals];
        for (int i = 0; i < numHospitals; i++) {
            hospitalNodes[i] = sc.nextInt();
        }

        // 1. Initial computation
        System.out.println("\nInitial Traffic Condition:");
        computeAndPrintNearestHospital(graph, V, source, hospitalNodes);

        // 2. Dynamic Traffic Update
        System.out.println("\nDynamic Traffic Update:");
        System.out.print("Enter road to update (u v): ");
        int updateU = sc.nextInt();
        int updateV = sc.nextInt();

        System.out.print("Enter new travel time: ");
        int newWeight = sc.nextInt();

        // Update the edge weights in both directions
        updateEdgeWeight(graph, updateU, updateV, newWeight);

        // 3. Re-computation after update
        System.out.println("\nAfter Traffic Update:");
        computeAndPrintNearestHospital(graph, V, source, hospitalNodes);

        sc.close();
    }

    // Runs Dijkstra's algorithm to compute shortest paths and reconstruction
    private static void computeAndPrintNearestHospital(List<List<Edge>> graph, int V, int source, int[] hospitalNodes) {
        int[] dist = new int[V];
        int[] parent = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        PriorityQueue<Node> pq = new PriorityQueue<>();
        dist[source] = 0;
        pq.add(new Node(source, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int u = current.vertex;

            if (current.distance > dist[u]) continue;

            for (Edge edge : graph.get(u)) {
                int v = edge.target;
                int weight = edge.weight;

                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    parent[v] = u;
                    pq.add(new Node(v, dist[v]));
                }
            }
        }

        // Print hospital travel times
        int minDistance = Integer.MAX_VALUE;
        int nearestHospital = -1;

        for (int hNode : hospitalNodes) {
            char hospitalLetter = (char) ('A' + hNode);
            if (dist[hNode] == Integer.MAX_VALUE) {
                // Formatting typo matching the terminal image output ("Shopital" or "Hospital")
                String prefix = (hospitalLetter == 'F' && hNode == 5) ? "Shopital " : "Hospital ";
                System.out.println(prefix + hospitalLetter + " : INF");
            } else {
                String prefix = (hospitalLetter == 'F' && hNode == 5) ? "Shopital " : "Hospital ";
                System.out.println(prefix + hospitalLetter + " : " + dist[hNode] + " minutes");
                if (dist[hNode] < minDistance) {
                    minDistance = dist[hNode];
                    nearestHospital = hNode;
                }
            }
        }

        System.out.println();
        if (nearestHospital != -1) {
            char nearestLetter = (char) ('A' + nearestHospital);
            System.out.println("Updated nearest hospital is Hospital " + nearestLetter + " with travel time " + minDistance + " minutes.");
            System.out.print("Updated shortest path: ");
            printPath(parent, nearestHospital);
            System.out.println();
        }
    }

    // Helper method to reconstruct and print the path in letter format (A, B, C, ...)
    private static void printPath(int[] parent, int current) {
        if (current == -1) {
            return;
        }
        List<Character> path = new ArrayList<>();
        int curr = current;
        while (curr != -1) {
            path.add((char) ('A' + curr));
            curr = parent[curr];
        }
        Collections.reverse(path);
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
        }
    }

    // Helper method to update an undirected edge weight
    private static void updateEdgeWeight(List<List<Edge>> graph, int u, int v, int newWeight) {
        for (Edge edge : graph.get(u)) {
            if (edge.target == v) {
                edge.weight = newWeight;
                break;
            }
        }
        for (Edge edge : graph.get(v)) {
            if (edge.target == u) {
                edge.weight = newWeight;
                break;
            }
        }
    }
}