import java.io.*;
import java.util.*;

public class GraphAlgorithm {
    private Map<Integer, Set<Integer>> adjacencyList;
    private List<Set<Integer>> independentSets;
    private Set<Integer> maxIndSet;
    private Set<Integer> minVerCov;

    public GraphAlgorithm (String filePath) throws IOException {
        adjacencyList = readGraph(filePath);
        independentSets = new ArrayList<>();
        maxIndSet = new HashSet<>();
        minVerCov = new HashSet<>();
    }

    public Map<Integer, Set<Integer>> readGraph(String filePath) throws IOException {
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int node = Integer.parseInt(parts[0]);
                Set<Integer> neighbors = new HashSet<>();
                for (int i = 1; i < parts.length; i++) {
                    neighbors.add(Integer.parseInt(parts[i]));
                }
                graph.put(node, neighbors);
            }
        }
        adjacencyList = graph;
        return graph;
    }

    // Проверка, является ли множество независимым
    private static boolean isIndependentSet(Map<Integer, List<Integer>> graph, Set<Integer> subset) {
        for (int node : subset) {
            for (int neighbor : graph.get(node)) {
                if (subset.contains(neighbor)) {
                    return false;
                }
            }
        }
        return true;
    }

//    public static Set<Integer> findMaxIndependentSet(Map<Integer, List<Integer>> graph) {
//        List<Integer> nodes = new ArrayList<>(graph.keySet());
//        Set<Integer> maxSet = new HashSet<>();
//        int n = nodes.size();
//
//        for (int i = 0; i < (1 << n); i++) {
//            Set<Integer> subset = new HashSet<>();
//            for (int j = 0; j < n; j++) {
//                if ((i & (1 << j)) != 0) {
//                    subset.add(nodes.get(j));
//                }
//            }
//            if (isIndependentSet(graph, subset) && subset.size() > maxSet.size()) {
//                maxSet = subset;
//            }
//        }
//        return maxSet;
//    }

    public List<Set<Integer>> findIndependentSets() {
        boolean step1 = true;

        int k = 0;
        List<Set<Integer>> S = new ArrayList<>(); // независимое множество из k вершин
        List<Set<Integer>> QMinus = new ArrayList<>(); // уже использованы для расширения S[k]
        List<Set<Integer>> QPlus = new ArrayList<>(); // ещё не использованы для расширения S[k]
        Stack<Integer> X = new Stack<>(); // pасширяющие вершины

        S.add(new HashSet<>());
        QMinus.add(new HashSet<>());
        QPlus.add(new HashSet<>(adjacencyList.keySet()));

        while (true) {
            // M1 шаг вперёд
            if (step1) {
                int v = selectVertex(QPlus.get(k));
                X.push(v);
                if (k + 1 >= S.size()) {
                    S.add(new HashSet<>(S.get(k)));
                    S.get(k + 1).add(v);
                    QMinus.add(new HashSet<>(QMinus.get(k)));
                    QPlus.add(new HashSet<>(QPlus.get(k)));
                } else {
                    S.set(k + 1, new HashSet<>(S.get(k)));
                    S.get(k + 1).add(v);
                    QMinus.set(k + 1, new HashSet<>(QMinus.get(k)));
                    QPlus.set(k + 1, new HashSet<>(QPlus.get(k)));
                }

                QMinus.get(k + 1).removeAll(adjacencyList.get(v));
                QPlus.get(k + 1).removeAll(adjacencyList.get(v));
                QPlus.get(k + 1).remove(v);

                k++;
            }

            //M2 проверка
            boolean step3 = false;
            for (int u : QMinus.get(k)) {
                Set<Integer> neighbors = adjacencyList.get(u);
                if (Collections.disjoint(neighbors, QPlus.get(k))) {
                    step3 = true;
                    //break;
                } else {
                    step3 = false;
                    break;
                }
            }


            if (!step3) {
                if (QPlus.get(k).isEmpty()) {
                    if (QMinus.get(k).isEmpty()) {
                        independentSets.add(new HashSet<>(S.get(k)));
                    }
                    step3 = true;
                } else {
                    step1 = true;
                    continue;
                }
            }


            if (step3) {
                //M3 шаг назад

                k--;
                int lastVertex = X.pop();
                S.set(k, new HashSet<>(S.get(k + 1)));
                S.get(k).remove(lastVertex);
                QMinus.get(k).add(lastVertex);
                QPlus.get(k).remove(lastVertex);


                if (k == 0 && QPlus.get(k).isEmpty()) {
                    break;
                } else {
                    step1 = false;
                }
            }

        }
        return independentSets;
    }

    public Set<Integer> findMaxIndependentSet() {
        findIndependentSets();
        Set<Integer> miv = independentSets.getFirst(); // miv - max independent vertex
        for (Set<Integer> independentVertex : independentSets) {
            if (independentVertex.size() > miv.size()) {
                miv = independentVertex;
            }
        }
        maxIndSet = miv;
        return miv;
    }


    private int selectVertex(Set<Integer> vertices) {
        return vertices.iterator().next();
    }

    public Set<Integer> findMinVertexCover() {
        Set<Integer> allNodes = new HashSet<>(adjacencyList.keySet());
        allNodes.removeAll(maxIndSet);
        minVerCov = allNodes;
        return minVerCov;
    }

    private void writeResult(String filePath, Set<Integer> mis, Set<Integer> mvc) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("Maximum Independent Set: " + mis + "\n");
            bw.write("Minimum Vertex Cover: " + mvc + "\n");
        }
    }

    public void checkPropereties(String filePath) throws IOException {
        findMaxIndependentSet();
        findMinVertexCover();
        writeResult(filePath, maxIndSet, minVerCov);
    }
}

