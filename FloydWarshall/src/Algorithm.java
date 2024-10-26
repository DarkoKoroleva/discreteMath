import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Algorithm {

    public static void floydWarshall(double[][] graph) {
        int V = graph.length; // count of vertex
        double[][] dist = new double[V][V]; // matrix of distance
        int[][] next = new int[V][V]; // paths matrix

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = graph[i][j];
                if (graph[i][j] != Double.MAX_VALUE && i != j) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1;
                }
            }
        }

        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] != Double.MAX_VALUE && dist[k][j] != Double.MAX_VALUE
                            && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        for (int i = 0; i < V; i++) {
            if (dist[i][i] < 0) {
                System.out.println("The graph contains a negative cycle, the paths may be incorrect.");
                return;
            }
        }

        printSolution(dist, next);
    }

    public static void printSolution(double[][] dist, int[][] next) {
        int V = dist.length;
        try ( BufferedWriter writer = new BufferedWriter(new FileWriter("ShortDist.txt"))){
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][j] == Double.MAX_VALUE) {
                        writer.write("INF ");
                    } else {
                        writer.write(dist[i][j] + " ");
                    }
                }
                writer.write('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try ( BufferedWriter writer = new BufferedWriter(new FileWriter("Paths.txt"))){
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (i != j && next[i][j] != -1) {
                        writer.write("Path from " + i + " to " + j + ": ");
                        writer.write(printPath(i, j, next) + '\n');
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Восстанавливает и печатает путь между вершинами u и v
    public static String printPath(int from, int to, int[][] next) {
        StringBuilder result;
        if (next[from][to] == -1) {
            result =  new StringBuilder("There is no path");
            return result.toString();
        }
        result = new StringBuilder();
        result.append(from);
        while (from != to) {
            from = next[from][to];
            result.append(" -> " + from);
        }
        return result.toString();
    }
}