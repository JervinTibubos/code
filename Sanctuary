import java.util.concurrent.TimeUnit;

public class Sanctuary {

    public static void main(String[] args) {
        String[] lines = {
            "If you've been waiting for fallin' in love",
            "Babe, you don't have to wait on me",
            "'Cause I've been aiming for heaven above",
            "But an angel ain't what i need...",
        };

        double[] charDelays = {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.2};
        double[] lineDelays = {0.5, 4.00, 0.05, 0.02};

        for (int i = 0; i < lines.length; i++) {
            printWithDelay(lines[i], charDelays[i]);
            try {
                TimeUnit.SECONDS.sleep((long) lineDelays[i]);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println();
        }
    }

    private static void printWithDelay(String line, double charDelay) {
        for (char c : line.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            try {
                TimeUnit.MILLISECONDS.sleep((long) (charDelay * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
