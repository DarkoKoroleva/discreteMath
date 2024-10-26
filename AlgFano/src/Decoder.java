import java.io.*;
import java.util.HashMap;

public class Decoder {

    public static String decode(File file, File encodeFile) {
        HashMap<Character, String> encoding;
        try {
            encoding = Reader.getCode(file);
        } catch (NumberFormatException e) {
            return "invalid file";
        }
        Character[] keys = encoding.keySet().toArray(new Character[0]);

        StringBuilder line = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(encodeFile)))) {
            int valid_bites = Integer.parseInt(reader.readLine());
            char curr;
            StringBuilder binaryStringBuilder = new StringBuilder();
            while ((curr = (char)reader.read()) != (char)-1) {
                String binaryString = Integer.toBinaryString(curr);

                while (binaryString.length() < 8) {
                    binaryString = "0" + binaryString;
                }
                binaryStringBuilder.append(binaryString);
            }
            binaryStringBuilder.delete(binaryStringBuilder.length() - 8, binaryStringBuilder.length() - valid_bites);
            String binaryString = binaryStringBuilder.toString();

            for (int i = 0; i < binaryString.length(); i ++) {
                for (int j = 0; j<binaryString.length(); j++){
                    String code = binaryString.substring(i, i+j);
                    if (encoding.containsValue(code)) {
                        for (char c : keys) {
                            if (encoding.get(c).contentEquals(code)) {
                                line.append(c);
                                i += j - 1;
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("TEXT.txt"))) {
            writer.write(line.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return line.toString();
    }
}
