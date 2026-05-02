package com.resume.tailor;

import java.util.*;

/**
 * Bellman-Ford Single-Source Shortest Path algorithm.
 * Handles negative-weight edges and detects negative cycles.
 * Time complexity: O(V * E)
 * Space complexity: O(V)
 */
public class BellmanFord {

    public static class Result {
        public final double[] dist;
        public final int[] prev;
        public final boolean hasNegativeCycle;
        public final long operationCount;

        public Result(double[] dist, int[] prev, boolean hasNegativeCycle, long ops) {
            this.dist = dist;
            this.prev = prev;
            this.hasNegativeCycle = hasNegativeCycle;
            this.operationCount = ops;
        }
    }

    public static Result run(Graph graph, int source) {
        int V = graph.getVertices();
        double[] dist = new double[V];
        int[] prev = new int[V];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);
        dist[source] = 0.0;

        long ops = 0;

        // Collect all edges for iteration
        List<int[]> edges = new ArrayList<>(); // [src, dest, weight-bits-hi, weight-bits-lo]
        List<double[]> weightedEdges = new ArrayList<>();
        for (int u = 0; u < V; u++) {
            for (Graph.Edge e : graph.getNeighbors(u)) {
                weightedEdges.add(new double[]{u, e.dest, e.weight});
            }
        }

        // Relax edges V-1 times
        for (int i = 0; i < V - 1; i++) {
            boolean updated = false;
            for (double[] edge : weightedEdges) {
                int u = (int) edge[0];
                int v = (int) edge[1];
                double w = edge[2];
                ops++;
                if (dist[u] != Double.POSITIVE_INFINITY && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    prev[v] = u;
                    updated = true;
                }
            }
            if (!updated) break; // Early termination
        }

        // Check for negative cycles
        boolean negativeCycle = false;
        for (double[] edge : weightedEdges) {
            int u = (int) edge[0];
            int v = (int) edge[1];
            double w = edge[2];
            ops++;
            if (dist[u] != Double.POSITIVE_INFINITY && dist[u] + w < dist[v]) {
                negativeCycle = true;
                break;
            }
        }

        return new Result(dist, prev, negativeCycle, ops);
    }
}