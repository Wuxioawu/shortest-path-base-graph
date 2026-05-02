# Graph Algorithms: Shortest Path & Minimum Spanning Tree

> **COMP47500 Advanced Data Structures — Assignment 5**
> Aaditya Diwan (25206991) · Peng Wu (25203407) · February 26, 2026

---

## Table of Contents

- [Overview](#overview)
- [Algorithms Implemented](#algorithms-implemented)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Experimental Results](#experimental-results)
- [Complexity Summary](#complexity-summary)
- [References](#references)

---

## Overview

A priority-queue–driven graph analytics engine implemented in Java that solves:

- **Single-Source Shortest Path (SSSP)** via Dijkstra's algorithm — `O((V+E) log V)`
- **Minimum Spanning Tree (MST)** via Prim's algorithm — `O((V+E) log V)`
- **Baseline SSSP** via Bellman-Ford — `O(VE)` (supports negative-weight edges)

The system supports weighted directed and undirected graphs with arbitrary densities and sizes, and includes a comprehensive 4-experiment benchmark harness.

---

## Algorithms Implemented

### Dijkstra's Algorithm (`Dijkstra.java`)

Greedy SSSP using a binary min-heap with lazy deletion. For each extracted vertex, relaxes all outgoing edges and re-inserts improved estimates.

```
Key properties:
  - Requires non-negative edge weights
  - Lazy deletion avoids custom decrease-key implementation
  - Path reconstruction via prev[] predecessor array
  - Time: O((V+E) log V)   Space: O(V+E)
```

### Bellman-Ford Algorithm (`BellmanFord.java`)

Iterative SSSP that relaxes all edges V−1 times. Includes early-termination optimisation and negative-cycle detection.

```
Key properties:
  - Handles negative-weight edges
  - Detects negative cycles
  - Used as correctness baseline against Dijkstra
  - Time: O(VE)   Space: O(V)
```

### Prim's MST Algorithm (`PrimMST.java`)

Greedy MST construction using a binary min-heap. Grows the spanning tree by repeatedly adding the minimum-weight crossing edge.

```
Key properties:
  - Operates on undirected graphs
  - Returns total MST weight and edge list
  - MST always contains exactly V−1 edges
  - Time: O((V+E) log V)   Space: O(V+E)
```

---

## Project Structure

```
graph-analytics/
├── src/
│   ├── Graph.java            # Adjacency-list weighted graph (directed/undirected)
│   ├── Dijkstra.java         # SSSP — O((V+E) log V)
│   ├── BellmanFord.java      # Baseline SSSP — O(VE)
│   ├── PrimMST.java          # Minimum Spanning Tree — O((V+E) log V)
│   └── GraphExperiments.java # 4-experiment benchmark harness
├── Assignment_Report_Graph.pdf
└── README.md
```

---

## Getting Started

### Prerequisites

- Java 11 or later (tested on OpenJDK 21)
- No external dependencies — uses only the Java standard library

### Compile

```bash
cd src
javac Graph.java Dijkstra.java BellmanFord.java PrimMST.java GraphExperiments.java
```

### Run All Experiments

```bash
java GraphExperiments
```

---

## Usage

### Basic Graph Construction

```java
// Create a directed weighted graph with 6 vertices
Graph g = new Graph(6, true);
g.addEdge(0, 1, 7.0);
g.addEdge(0, 2, 9.0);
g.addEdge(0, 5, 14.0);
g.addEdge(2, 5, 2.0);
// ... add more edges
```

### Run Dijkstra's SSSP

```java
Dijkstra.Result result = Dijkstra.run(g, 0);   // source = 0

// Query shortest distance to vertex 4
System.out.println(result.dist[4]);             // e.g. 26.0

// Reconstruct shortest path to vertex 4
List<Integer> path = Dijkstra.reconstructPath(result.prev, 0, 4);
System.out.println(path);                       // [0, 2, 3, 4]
```

### Run Bellman-Ford

```java
BellmanFord.Result result = BellmanFord.run(g, 0);

if (result.hasNegativeCycle) {
    System.out.println("Negative cycle detected!");
} else {
    System.out.println(Arrays.toString(result.dist));
}
```

### Run Prim's MST

```java
// Prim requires an undirected graph
Graph undirected = new Graph(6, false);
undirected.addEdge(0, 1, 4.0);
undirected.addEdge(0, 2, 3.0);
// ...

PrimMST.MSTResult mst = PrimMST.run(undirected);
System.out.println("Total MST weight: " + mst.totalWeight);
System.out.println("MST edges: " + mst.edges.size());   // always V-1
```

### Generate a Random Graph

```java
// 500 vertices, 10% edge density, directed, seed=42
Graph random = Graph.generateRandom(500, 0.10, true, 42L);
```

---

## Experimental Results

All experiments use 3 warm-up runs + 10 measurement runs on OpenJDK 21 / Ubuntu 24.04.

### Experiment 1 — Throughput Scaling (density = 5%, directed)

| V | Dijkstra (ms) | Bellman-Ford (ms) | Speedup |
|---|---|---|---|
| 100 | 0.200 | 0.772 | 3.86× |
| 400 | 1.449 | 2.390 | 1.65× |
| 800 | 2.124 | 4.769 | 2.25× |
| 1 200 | 1.259 | 4.701 | 3.73× |
| 1 500 | 1.563 | 6.937 | 4.44× |
| **2 000** | **2.419** | **31.459** | **13.0×** |

> Dijkstra outperforms Bellman-Ford by up to **13×** at V=2 000. Bellman-Ford operation count (1.6M) vs Dijkstra (207K) confirms the O(VE) vs O((V+E) log V) gap.

---

### Experiment 2 — Edge Density Sensitivity (V = 500)

| Density | Edges | Dijkstra (ms) | Prim (ms) |
|---|---|---|---|
| 0.01 | 2 481 | 0.087 | 3.261 |
| 0.10 | 24 939 | 0.868 | 3.323 |
| 0.30 | 74 828 | 0.707 | 2.520 |
| 0.70 | 174 407 | 1.010 | 5.030 |
| 0.90 | 224 489 | 1.091 | 5.012 |

> Dijkstra's operation count tracks edge count within **2%**, confirming the O(E log V) dominant term.

---

### Experiment 3 — Prim's MST Scaling (density = 10%, undirected)

| V | MST Weight | Prim (ms) | Ops | MST Edges |
|---|---|---|---|---|
| 100 | 818.13 | 0.048 | 2 186 | 99 ✓ |
| 500 | 1 098.56 | 1.192 | 51 823 | 499 ✓ |
| 1 000 | 1 575.60 | 4.179 | 205 045 | 999 ✓ |
| 2 000 | 2 578.29 | 13.110 | 812 540 | 1 999 ✓ |

> MST always contains exactly **V−1 edges** across all trials. Ops grow as O(V log V) at fixed density, consistent with theory.

---

### Experiment 4 — Correctness Verification

**Hand-crafted 6-vertex graph (source = 0):**

| Vertex | Dijkstra | Bellman-Ford | Match |
|---|---|---|--|
| 0 | 0.0 | 0.0 | yes |
| 1 | 7.0 | 7.0 | yes |
| 2 | 9.0 | 9.0 | yes |
| 3 | 20.0 | 20.0 | yes |
| 4 | 26.0 | 26.0 | yes |
| 5 | 11.0 | 11.0 | yes |

**Stress test:** 300-vertex graph × 100 random source queries = **30 000 vertex-distance comparisons → 0 mismatches**.

---

## Complexity Summary

| Algorithm | Time Complexity | Space Complexity | Negative Weights |
|---|---|---|------------------|
| Dijkstra (heap) | O((V+E) log V) | O(V+E) | no               |
| Bellman-Ford | O(VE) | O(V) | yes              |
| Prim MST (heap) | O((V+E) log V) | O(V+E) | N/A              |
| Dijkstra (array) | O(V²) | O(V) | no               |
| Floyd-Warshall | O(V³) | O(V²) | yes              |

---

## References

1. E. W. Dijkstra. *A Note on Two Problems in Connexion with Graphs.* Numerische Mathematik, 1:269–271, 1959.
2. Robert C. Prim. *Shortest Connection Networks and Some Generalizations.* Bell System Technical Journal, 36(6):1389–1401, 1957.
3. Thomas H. Cormen et al. *Introduction to Algorithms.* MIT Press, 3rd edition, 2009.
4. Michael T. Goodrich et al. *Data Structures and Algorithms in Java.* Wiley, 6th edition, 2014.
5. Robert E. Tarjan. *Amortized Computational Complexity.* SIAM J. Algebraic Discrete Methods, 6(2):306–318, 1985.