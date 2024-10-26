import java.io.*;
import java.util.HashMap;

public class Reader {
    public static HashMap<Character, String> getCode(File file) throws NumberFormatException {
        HashMap<Character, String> encoding = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String s;
            char c;
            while ((s = reader.readLine()) != null) {
                StringBuilder str = new StringBuilder(s);
                if (s.contains("caret") || s.contains("enter")) {
                    String[] arg = s.split(" = ", 2);
                    if (arg[0].equals("caret")){
                        encoding.put('\r', arg[1]);
                    } else if (arg[0].equals("enter")){
                        encoding.put('\n', arg[1]);
                    }
                } else {
                    c = s.charAt(0);
                    String code = str.delete(0, 4).toString();
                    encoding.put(c, code);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return encoding;
    }
}
