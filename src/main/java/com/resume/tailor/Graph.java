package com.resume.tailor;

import java.util.*;

/**
 * Weighted directed graph implemented with adjacency lists.
 * Supports both directed and undirected modes.
 */
public class Graph {
    private final int vertices;
    private final boolean directed;
    private final List<List<Edge>> adjList;

    public static class Edge {
        public final int dest;
        public final double weight;

        public Edge(int dest, double weight) {
            this.dest = dest;
            this.weight = weight;
        }
    }

    public Graph(int vertices, boolean directed) {
        this.vertices = vertices;
        this.directed = directed;
        this.adjList = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    public void addEdge(int src, int dest, double weight) {
        adjList.get(src).add(new Edge(dest, weight));
        if (!directed) {
            adjList.get(dest).add(new Edge(src, weight));
        }
    }

    public List<Edge> getNeighbors(int vertex) {
        return adjList.get(vertex);
    }

    public int getVertices() {
        return vertices;
    }

    public boolean isDirected() {
        return directed;
    }

    /**
     * Generate a random graph with given edge density (0.0 - 1.0).
     */
    public static Graph generateRandom(int vertices, double density, boolean directed, long seed) {
        Random rand = new Random(seed);
        Graph g = new Graph(vertices, directed);
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i != j && rand.nextDouble() < density) {
                    double weight = 1.0 + rand.nextDouble() * 99.0; // [1, 100]
                    g.addEdge(i, j, weight);
                }
            }
        }
        return g;
    }
}