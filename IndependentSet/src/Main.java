import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
//        GraphAlgorithm g1 = new GraphAlgorithm("g1");
//        g1.checkPropereties("output1.txt");

//        GraphAlgorithm g2 = new GraphAlgorithm("g4");
//        g2.checkPropereties("output2.txt");

        GraphAlgorithm g3 = new GraphAlgorithm("input.txt");
        g3.checkPropereties("output3.txt");
    }
}