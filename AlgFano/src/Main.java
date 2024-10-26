import java.io.*;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                try {
                    String s = reader.readLine();

                    String[] arg = s.split(" ");
                    if (arg.length == 1) {
                        if (arg[0].equals("exit")) {
                            exit(0);
                        }
                        File file = new File(arg[0]);
                        Coder.encryptText(file);
                    }
                    else if (arg.length == 2) {
                        File fileDecode = new File(arg[0]);
                        File encodeFile = new File(arg[1]);
                        Decoder.decode(fileDecode, encodeFile);
                    }
                    else {
                        System.out.println("the wrong input");
                    }
                } catch (RuntimeException e) {
                    System.out.println("file not found");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}