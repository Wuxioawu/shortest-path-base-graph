package com.resume.tailor;

import java.util.*;

/**
 * Dijkstra's Single-Source Shortest Path algorithm using a binary min-heap (PriorityQueue).
 * Time complexity: O((V + E) log V)
 * Space complexity: O(V + E)
 */
public class Dijkstra {

    public static class Result {
        public final double[] dist;
        public final int[] prev;
        public final long operationCount;

        public Result(double[] dist, int[] prev, long operationCount) {
            this.dist = dist;
            this.prev = prev;
            this.operationCount = operationCount;
        }
    }

    /**
     * Run Dijkstra from a given source vertex.
     * @param graph  the input graph (non-negative weights required)
     * @param source the starting vertex
     * @return a Result containing distances, predecessors, and op count
     */
    public static Result run(Graph graph, int source) {
        int V = graph.getVertices();
        double[] dist = new double[V];
        int[] prev = new int[V];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);
        dist[source] = 0.0;

        // PriorityQueue: (distance, vertex)
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> Double.longBitsToDouble(a[0])));
        pq.offer(new long[]{Double.doubleToLongBits(0.0), source});

        long ops = 0;

        while (!pq.isEmpty()) {
            long[] top = pq.poll();
            double d = Double.longBitsToDouble(top[0]);
            int u = (int) top[1];
            ops++;

            if (d > dist[u]) continue; // stale entry

            for (Graph.Edge e : graph.getNeighbors(u)) {
                ops++;
                double newDist = dist[u] + e.weight;
                if (newDist < dist[e.dest]) {
                    dist[e.dest] = newDist;
                    prev[e.dest] = u;
                    pq.offer(new long[]{Double.doubleToLongBits(newDist), e.dest});
                }
            }
        }

        return new Result(dist, prev, ops);
    }

    /**
     * Reconstruct the shortest path from source to target.
     */
    public static List<Integer> reconstructPath(int[] prev, int source, int target) {
        LinkedList<Integer> path = new LinkedList<>();
        for (int at = target; at != -1; at = prev[at]) {
            path.addFirst(at);
            if (at == source) break;
        }
        if (path.isEmpty() || path.getFirst() != source) return Collections.emptyList();
        return path;
    }
}