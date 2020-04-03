import java.io.*;
import java.util.*;

public class level2 {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/level2_5.in"))) {
            int N = Integer.parseInt(reader.readLine());
            String line;
            TreeMap<String, ArrayList<String[]>> map = new TreeMap<>();
            while ((line = reader.readLine()) != null) {
                String[] curr = line.split(",");
                if (!(map.containsKey(curr[4]))) {
                    ArrayList<String[]> other = new ArrayList<>();
                    other.add(new String[]{curr[0], curr[1], curr[2], curr[3], curr[5], curr[6]});
                    map.put(curr[4], other);
                } else {
                    ArrayList<String[]> old = map.get(curr[4]);
                    old.add(new String[]{curr[0], curr[1], curr[2], curr[3], curr[5], curr[6]});
                    map.replace(curr[4], old);
                }
            }

            reader.close();

            Set<Map.Entry<String, ArrayList<String[]>>> entrySet = map.entrySet();
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("data/level2_5.out")));

            for (Map.Entry<String, ArrayList<String[]>> entry : entrySet) {
                String key = entry.getKey();
                ArrayList<String[]> destinations = entry.getValue();
                TreeMap<String, Integer> finalDestinations = new TreeMap<>();

                for (String[] value : destinations) {
                    int count = 1;
                    for (String[] anotherValue : destinations) {
                        if (value[4].equals(anotherValue[4])) {
                            if (!(
                                    (value[0].equals(anotherValue[0])) &&
                                            (value[1].equals(anotherValue[1])) &&
                                            (value[2].equals(anotherValue[2])) &&
                                            (value[3].equals(anotherValue[3])) &&
                                            (value[5].equals(anotherValue[5]))))
                                    if(!(value[5].equals(anotherValue[5]))) count++;

                        }
                    }
                    finalDestinations.put(value[4], new Integer(count));
                }
                Set<Map.Entry<String, Integer>> loopDestinations = finalDestinations.entrySet();
                for (Map.Entry<String, Integer> currentDestination : loopDestinations)
                    writer.println(key + " " + currentDestination.getKey() + " " + currentDestination.getValue());

            }
            writer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
