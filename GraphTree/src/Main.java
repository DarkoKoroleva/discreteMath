import java.io.IOException;

class Main {
    public static void main(String[] args) throws IOException {
        Graph g1 = Graph.readGraphFromFile("src/g1");
        System.out.println(g1.checkProperties());
        System.out.println();

        Graph g2 = Graph.readGraphFromFile("src/g2");
        System.out.println(g2.checkProperties());
        System.out.println();

        Graph g3 = Graph.readGraphFromFile("src/g3");
        System.out.println(g3.checkProperties());
        System.out.println();

        Graph g4 = Graph.readGraphFromFile("src/g4");
        System.out.println(g4.checkProperties());
        System.out.println();

        Graph g5 = Graph.readGraphFromFile("src/g5");
        System.out.println(g5.checkProperties());
        System.out.println();

    }
}