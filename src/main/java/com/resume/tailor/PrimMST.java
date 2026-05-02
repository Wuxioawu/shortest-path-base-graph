package com.resume.tailor;

import java.util.*;

/**
 * Prim's Minimum Spanning Tree algorithm using a binary min-heap.
 * Time complexity: O((V + E) log V)
 * Space complexity: O(V + E)
 */
public class PrimMST {

    public static class MSTResult {
        public final double totalWeight;
        public final List<int[]> edges; // [u, v] pairs
        public final long operationCount;

        public MSTResult(double totalWeight, List<int[]> edges, long ops) {
            this.totalWeight = totalWeight;
            this.edges = edges;
            this.operationCount = ops;
        }
    }

    public static MSTResult run(Graph graph) {
        int V = graph.getVertices();
        double[] key = new double[V];    // minimum edge weight to connect to MST
        int[] parent = new int[V];       // MST parent
        boolean[] inMST = new boolean[V];

        Arrays.fill(key, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);
        key[0] = 0.0;

        // PriorityQueue: (key, vertex)
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> Double.longBitsToDouble(a[0])));
        pq.offer(new long[]{Double.doubleToLongBits(0.0), 0});

        double totalWeight = 0.0;
        List<int[]> mstEdges = new ArrayList<>();
        long ops = 0;

        while (!pq.isEmpty()) {
            long[] top = pq.poll();
            int u = (int) top[1];
            ops++;

            if (inMST[u]) continue;
            inMST[u] = true;

            if (parent[u] != -1) {
                totalWeight += key[u];
                mstEdges.add(new int[]{parent[u], u});
            }

            for (Graph.Edge e : graph.getNeighbors(u)) {
                ops++;
                if (!inMST[e.dest] && e.weight < key[e.dest]) {
                    key[e.dest] = e.weight;
                    parent[e.dest] = u;
                    pq.offer(new long[]{Double.doubleToLongBits(e.weight), e.dest});
                }
            }
        }

        return new MSTResult(totalWeight, mstEdges, ops);
    }
}