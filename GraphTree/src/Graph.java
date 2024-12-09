import java.io.*;
import java.util.*;

class Graph {
    private int V;  //vertex
    private List<List<Integer>> adjList;
    int cycleCount = 0;


    public Graph(int vertices) {
        V = vertices;
        adjList = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v) {
        adjList.get(u).add(v);
        //adjList.get(v).add(u);
    }

    public List<Integer> isAcyclic() {
        //System.out.println("Проверка ацикличности:");
        List<Integer> cycle = new ArrayList<>();

        if (dfsFindCycle(cycle)) {
            return cycle;
        }

        return null; // acyclic
    }

    private boolean dfsFindCycle(List<Integer> cycle2) {
        Stack<Integer> stack = new Stack<>();
        Stack<Integer> way = new Stack<>();
        boolean[] visited = new boolean[V];
        Arrays.fill(visited, false);
        int[] parent = new int[V];

        Arrays.fill(parent, -1);

        for (int start = 0; start < V; start++) {
            if (visited[start]) continue;

            stack.push(start);
            visited[start] = true;

            while (!stack.isEmpty()) {
                int current = stack.pop();
                way.push(current);
                //System.out.println("Достаем узел " + current);

                for (int neighbor : adjList.get(current)) {
                    if (!visited[neighbor]) {
                        //System.out.println("Узел " + neighbor + " не был посещен ранее");
                        visited[neighbor] = true;
                        parent[neighbor] = current;
                        stack.push(neighbor);
                        //System.out.println("Добавляем узел " + neighbor);
                        //System.out.println("stack: "+ stack);
                    } else if (neighbor != parent[current]) {
                        //System.out.println("Узел " + neighbor + " был посещен ранее и не является родителем");
                        //System.out.println("cycle is found:");
                        way.push(neighbor);

                        int node = current;
                        while (way.peek() != parent[neighbor]) {
                            cycle2.add(way.pop());
                        }
                        cycle2.add(way.pop());
                        cycle2.add(neighbor);
                        //System.out.println(cycle2);
                        return true;
                    }
                }

            }
        }
        return false;
    }

    public int countSimpleCycles() {
        cycleCount = 0;
        boolean[] visited = new boolean[V];

        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                dfsCountCycles(i, visited);
            }
        }

        cycleCount /= 2;
        return cycleCount;
    }

    private void dfsCountCycles(int start, boolean[] globalVisited) {
        Stack<Integer> stack = new Stack<>();
        boolean[] localVisited = new boolean[V];
        int[] parent = new int[V];
        Arrays.fill(parent, -1);
        //System.out.println("Считает простые циклы");
        stack.push(start);
        localVisited[start] = true;

        while (!stack.isEmpty()) {
            int current = stack.pop();
            //System.out.println("Достаем узел " + current);

            for (int neighbor : adjList.get(current)) {
                if (!localVisited[neighbor]) {
                    stack.push(neighbor);
                    //System.out.println("Добавляем узел в стэк" + neighbor);
                    localVisited[neighbor] = true;
                    globalVisited[neighbor] = true;
                    parent[neighbor] = current;
                } else if (neighbor != parent[current]) {
                    cycleCount++;
                    //System.out.println( neighbor + " посещен и не родитель -> нашли цикл; (родитель для" + current + " = " + parent[current]);
                }
            }
        }
    }

    private List<Integer> isSubcyclic() {
        //System.out.println("Проверка субцикличности");
        for (int u = 0; u < V; u++) {
            for (int v = u + 1; v < V; v++) {
                if (!adjList.get(u).contains(v)) {
                    Graph tempGraph = new Graph(V);
                    for (int i = 0; i < V; i++) {
                        tempGraph.adjList.set(i, new ArrayList<>(adjList.get(i)));
                    }
                    tempGraph.addEdge(u, v);
                    tempGraph.addEdge(v, u);
                    int c = tempGraph.countSimpleCycles();
                    if (tempGraph.countSimpleCycles() != 1) {
                        List<Integer> edge = new ArrayList<>();
                        edge.add(u);
                        edge.add(v);
                        //System.out.println(u + " " + v);
                        return edge; //false
                    }
                }
            }
        }
        return null; //subcyclic
    }

    public String checkProperties() {
        StringBuilder result = new StringBuilder();
        int p = V;
        int q = 0;
        for (List<Integer> neighbors : adjList) {
            q += neighbors.size();
        }
        q /= 2;

        boolean isAcyclic = (isAcyclic() == null);
        List<Integer> cycle = isAcyclic();
        boolean isSubcyclic = (isSubcyclic() == null);
        List<Integer> edge = isSubcyclic();
        boolean isTree = (q == p - 1);

        result.append("Ацикличный: " + isAcyclic + '\n');
        if (!isAcyclic) {
            result.append("Цикл: " + cycle + '\n');
        }
        result.append("Субцикличный: " + isSubcyclic + '\n');
        if (!isSubcyclic) {
            result.append("Ребро: " + edge + '\n');
        }
        result.append("Древочисленный: " + isTree + '\n');
        result.append("Количество вершин: " + p + '\n');
        result.append("Количество ребер: " + q + '\n');


        if (isAcyclic && isSubcyclic) {
            result.append("Граф ацикличен и субцикличен.");
            result.append(" Граф является деревом.");
        } else if (!isAcyclic && isSubcyclic) {
            result.append("Нарушена ацикличность.");
            result.append(" Граф не является деревом.");
        } else if (isAcyclic && !isSubcyclic) {
            result.append("Нарушена субцикличность для данного графа.");
            result.append(" Граф не является деревом.");
        } else {
            result.append("Нарушены оба условия: ацикличность и субцикличность.");
            result.append(" Граф не является деревом.");
        }

        if (isTree) {
            result.append(" Граф также является древочисленным." + '\n');
        } else {
            result.append(" Граф не является древочисленным." + '\n');
        }


        writeResult(result.toString());
        return result.toString();
    }

    public static Graph readGraphFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        int vertices = Integer.parseInt(reader.readLine().trim());
        Graph graph = new Graph(vertices);

        for (int i = 0; i < vertices; i++) {
            String line = reader.readLine();
            if (line == null || line.isEmpty()) {
                continue;
            }
            String[] parts = line.split(" ");
            int vertex = Integer.parseInt(parts[0]);

            for (int j = 1; j < parts.length; j++) {
                int neighbor = Integer.parseInt(parts[j]);
                graph.addEdge(vertex, neighbor);
            }
        }

        reader.close();
        return graph;
    }

    public static void writeResult(String result) {
        try (FileWriter writer = new FileWriter("result.txt", false)) {
            writer.write(result);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
