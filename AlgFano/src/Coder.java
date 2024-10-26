import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class Coder {
    private static int getMiddle(HashMap<Character, Integer> frequency, int begin, int end) {
        int sum = 0;
        int counter = 0;

        for (Integer i : Arrays.copyOfRange(frequency.values().toArray(new Integer[0]), begin, end)) {
            sum += i;
        }

        int mid = sum / 2;
        sum=0;

        for (Integer i : Arrays.copyOfRange(frequency.values().toArray(new Integer[0]), begin, end)) {
            sum += i;
            if (sum > mid) break;
            counter++;
        }

        if (counter == 0){
            return 1;
        }
        return counter;
    }

    public static HashMap<Character, String> fano(HashMap<Character, Integer> frequency) {
        HashMap<Character, String> encoding = new HashMap<>();

        int mid = 0;

        mid = getMiddle(frequency, 0, frequency.size());
        Stack<Integer> stackEnd = new Stack<>();
        int begin = 0, end = frequency.size();
        while (true) {
            int currPos = 0;
            for (Character c : Arrays.copyOfRange(frequency.keySet().toArray(new Character[0]), begin, end)){
                mid = getMiddle(frequency, begin, end);
                if (currPos < mid) {
                    if (encoding.containsKey(c)) {
                        encoding.put(c, encoding.get(c) + "0");
                    } else {
                        encoding.put(c, "0");
                    }
                    currPos ++;
                } else {
                    if (encoding.containsKey(c)) {
                        encoding.put(c, encoding.get(c) + "1");
                    } else {
                        encoding.put(c, "1");
                    }
                }
            }

            if (end - begin > 2) {
                stackEnd.push(end);
                end = begin + mid;
            } else {
                begin = end;
                if (!stackEnd.empty()) {
                    end = stackEnd.pop();
                } else {
                    break;
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("CODE.txt"))) {
            for (Character c:encoding.keySet()){
                if (c == '\n') {
                    writer.write("enter = " + encoding.get(c) +"\n");
                } else if (c == '\r') {
                    writer.write("caret = " + encoding.get(c) +"\n");
                } else {
                    writer.write(c + " = " + encoding.get(c) + "\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return encoding;
    }

    public static String encryptText(File fileInput) {
        HashMap<Character, String> encoding = fano(Frequency.getFreqOfOccurrence(fileInput));
        StringBuilder s = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileInput)))) {
            char c;
            while ((c = (char) reader.read()) != (char) -1) {
                s.append(encoding.get(c));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String binaryString = s.toString();
        char[] bytes;
        if (binaryString.length() % 8 == 0){
            bytes = new char[binaryString.length() / 8];
        } else {
            bytes = new char[binaryString.length() / 8 + 1];
        }
        String byteString = "";
        for (int i = 0; i < bytes.length; i++) {
            byteString= binaryString.substring(i * 8, Math.min (i * 8 + 8, binaryString.length()));
            bytes[i] = (char) Integer.parseInt(byteString, 2);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ENCODE.txt"))) {
            writer.write(byteString.length() + "\n");
            for (char b : bytes) {
                writer.write(b);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return s.toString();
    }
}
