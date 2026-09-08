import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class dijikstra{

    static class edge
    {
        int node;
        int weight;

        edge(int node,int weight)
        {
            this.node = node;
            this.weight = weight;
        }
    }

    static void diji(int v,ArrayList<edge>[] graph,int source)
    {
        int[] dist = new int[v];

        Arrays.fill(dist , Integer.MAX_VALUE);

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparing(a -> a[0]));

        dist[source] = 0;
        pq.add(new int[]{0,source});

        while (!pq.isEmpty()) 
        {
            int[] current = pq.poll();
            
            int distance = current[0];
            int node = current[1];

            if(distance > dist[node])
            {
                continue;
            }

            for(edge e : graph[node])
            {
                int nextnode = e.node;
                int weight = e.weight;

                if(dist[node] + weight < dist[nextnode])
                {
                    dist[nextnode] = dist[node]+weight;

                    pq.add(new int[]
                    {
                        dist[nextnode],nextnode
                    });
                }
            }
        }
         
        System.out.println("Shortest distances from source " + source + ":");

        for (int i = 0; i < v; i++) {
            System.out.println(
                "Vertex " + i + " = " + dist[i]
            );
        }
    }

     public static void main(String[] args) {

        int V = 5;

        ArrayList<edge>[] graph = new ArrayList[V];

        for (int i = 0; i < V; i++) {
            graph[i] = new ArrayList<>();
        }

        // Graph
        graph[0].add(new edge(1, 10));
        graph[0].add(new edge(4, 5));

        graph[1].add(new edge(2, 1));
        graph[1].add(new edge(4, 2));

        graph[2].add(new edge(3, 4));

        graph[3].add(new edge(0, 7));
        graph[3].add(new edge(2, 6));

        graph[4].add(new edge(1, 3));
        graph[4].add(new edge(2, 9));
        graph[4].add(new edge(3, 2));

        // Source vertex
        int source = 0;

        diji(V, graph, source);
    }
    
}
