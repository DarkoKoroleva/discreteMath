import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file = new File("src/text");

        Frequency frequency = new Frequency();
        Fano fano = new Fano();

        fano.fano(frequency.getFreqOfOccurrence(file));
        System.out.println(fano.getEncoding());
        String code = fano.encryptText(file);
        System.out.println(code);
        System.out.println(fano.decode(code));
    }
}