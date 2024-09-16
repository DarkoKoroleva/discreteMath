import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Fano {
    private HashMap<Character, String> encoding = new HashMap<>();
    Character[] keys;

    public HashMap<Character, String> getEncoding() {
        return encoding;
    }

    public void fano(HashMap<Character, Integer> frequency) {
        keys = frequency.keySet().toArray(new Character[0]);
        Integer[] values = frequency.values().toArray(new Integer[0]);
        encodeByFano(keys, values);
    }

    public void encodeByFano(Character[] keys, Integer[] values) {
        int sum = 0;
        int mid = 0;
        int currS = 0, middlePos = 0, curr = 0;

        for (Integer i : values) {
            sum += i;
        }
        mid = sum / 2;
        for (Integer i : values) {
            if (currS + i <= mid) {
                currS += i;
                if (encoding.containsKey(keys[curr])) {
                    encoding.put(keys[curr], encoding.get(keys[curr]) + "0");
                } else {
                    encoding.put(keys[curr], "0");
                }
                middlePos++;
            } else {
                if (encoding.containsKey(keys[curr])) {
                    encoding.put(keys[curr], encoding.get(keys[curr]) + "1");
                } else {
                    encoding.put(keys[curr], "1");
                }
            }
            curr++;
        }

        Character[] leftKeys = Arrays.copyOfRange(keys, 0, middlePos);
        Integer[] leftValues = Arrays.copyOfRange(values, 0, middlePos);

        Character[] rightKeys = Arrays.copyOfRange(keys, middlePos, keys.length);
        Integer[] rightValues = Arrays.copyOfRange(values, middlePos, values.length);

        if (leftKeys.length != 1) {
            encodeByFano(leftKeys, leftValues);
        }
        if (rightKeys.length != 1) {
            encodeByFano(rightKeys, rightValues);
        }
    }

    public String encryptText(File fileInput) {
        StringBuilder s = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileInput)))) {
            char c;
            while ((c = (char) reader.read()) != (char) -1) {
                s.append(encoding.get(c));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return s.toString();
    }

    public String decode(String s) {
        StringBuilder code = new StringBuilder();
        StringBuilder line = new StringBuilder();
        try (Reader reader = new StringReader(s)) {
            int curr;
            while ((curr = reader.read()) != -1){
                code.append((char) curr);
                if (encoding.containsValue(code.toString())) {
                    for (char c : keys) {
                        if (encoding.get(c).contentEquals(code)) {
                            line.append(c);
                            code.delete(0, code.length());
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return line.toString();
    }
}
