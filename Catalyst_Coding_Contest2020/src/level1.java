import java.io.*;
import java.math.BigDecimal;

public class level1 {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/level1_5.in"))) {
            int N = Integer.parseInt(reader.readLine());
            String[] firstLine = reader.readLine().split(",");
            long minTime = Long.parseLong(firstLine[0]), maxTime = Long.parseLong(firstLine[0]);
            double minLat1 = Double.parseDouble(firstLine[1]), maxLat1 = Double.parseDouble(firstLine[1]),
                    minLong1 = Double.parseDouble(firstLine[2]), maxLong1 = Double.parseDouble(firstLine[2]),
                    maxAlt1 = Double.parseDouble(firstLine[3]);
            BigDecimal minLat = BigDecimal.valueOf(minLat1).setScale(5), maxLat = BigDecimal.valueOf(maxLat1).setScale(5),
                    minLong = BigDecimal.valueOf(minLong1).setScale(5), maxLong = BigDecimal.valueOf(maxLong1).setScale(5),
                    maxAlt = BigDecimal.valueOf(maxAlt1).setScale(5);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cur = line.split(",");
                if (Long.parseLong(cur[0]) < minTime) minTime = Long.parseLong(cur[0]);
                if (Long.parseLong(cur[0]) > maxTime) maxTime = Long.parseLong(cur[0]);

                BigDecimal lat = BigDecimal.valueOf(Double.parseDouble(cur[1])).setScale(5);
                if (lat.compareTo(minLat) < 0) {
                    minLat = lat;
                    minLat1 = Double.parseDouble(cur[1]);
                }
                if (lat.compareTo(maxLat) > 0) {
                    maxLat = lat;
                    maxLat1 = Double.parseDouble(cur[1]);
                }

                BigDecimal long1 = BigDecimal.valueOf(Double.parseDouble(cur[2])).setScale(5);
                if (long1.compareTo(minLong) < 0) {
                    minLong = long1;
                    minLong1 = Double.parseDouble(cur[2]);
                }
                if (long1.compareTo(maxLong) > 0) {
                    maxLong = long1;
                    maxLong1 = Double.parseDouble(cur[2]);
                }

                BigDecimal alt = BigDecimal.valueOf(Double.parseDouble(cur[3])).setScale(5);
                if (alt.compareTo(maxAlt) > 0) {
                    maxAlt = alt;
                    maxAlt1 = Double.parseDouble(cur[3]);
                }
            }

            reader.close();
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("data/level1_5.out")));
            writer.println("" + minTime + " " + maxTime);
            writer.println("" + minLat1 + " " + maxLat1);
            writer.println("" + minLong1 + " " + maxLong1);
            writer.println("" + maxAlt1);
            writer.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
