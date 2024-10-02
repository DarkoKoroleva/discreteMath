import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Frequency {

    public static HashMap<Character, Integer> getFreqOfOccurrence(File file) {
        HashMap<Character, Integer> letter = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            char c;
            while ((c = (char) reader.read()) != (char) -1) {
                if (letter.containsKey(c)) {
                    letter.put(c, letter.get(c) + 1);
                } else {
                    letter.put(c, 1);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HashMap<Character, Integer> sortedMap = letter.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors
                        .toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new));

        return sortedMap;
    }
}
