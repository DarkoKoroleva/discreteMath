import java.io.*;

public class Reader {
    public static double[][] readAdjacencyMatrix(File file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String s = reader.readLine();
            String[] weight = s.split(" ");
            int count = weight.length;
            double[][] graph = new double[count][count];
            int j = 0;
            do {
                weight = s.split(" ");
                for (int i = 0; i < count; i++) {
                    try {
                        graph[j][i] = Double.parseDouble(weight[i]); //i-column
                    } catch (NumberFormatException e) {
                        graph[j][i] = Double.MAX_VALUE;
                    }
                }
                j++;
            }
            while ((s = reader.readLine()) != null);

            return graph;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}

