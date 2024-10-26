import java.io.File;

public class Main {
    public static void main(String[] args) {
        double[][] graph = Reader.readAdjacencyMatrix(new File("adj.txt"));

        Algorithm.floydWarshall(graph);
    }
}