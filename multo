 public class Multo {
    static long t(String ts) {
        String[] parts = ts.split("[:\\.]");
        int m = Integer.parseInt(parts[0]);
        int s = Integer.parseInt(parts[1]);
        int ms = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
        return m * 60_000L + s * 1_000L + ms;
    }

    static class Line {
        long atMs;
        String text;
        Line(String at, String text) { this.atMs = t(at); this.text = text; }
    }

    public static void main(String[] args) throws Exception {
        Line[] chorus = new Line[] {
            new Line("00:00.000", "Hindi na makalaya"),
            new Line("00:04.200", "Dinadalaw mo 'ko bawat gabi"),
            new Line("00:08.450", "Wala mang nakikita"),
            new Line("00:12.700", "Haplos mo'y ramdam pa rin sa dilim")
        };

        long start = System.currentTimeMillis();
        int i = 0;
        while (i < chorus.length) {
            long elapsed = System.currentTimeMillis() - start;
            if (elapsed >= chorus[i].atMs) {
                System.out.println(chorus[i].text);
                i++;
            }
            Thread.sleep(10);
        }
    }
}
