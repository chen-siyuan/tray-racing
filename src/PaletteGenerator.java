import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class PaletteGenerator {

    private final String name;
    private final String dateCreated;
    private final int[] vision;
    private final int[] map;
    private final int[] control;

    public static void main() throws FileNotFoundException {
        PaletteGenerator pg = new PaletteGenerator("counterstrike", "2020-10-09T21:00:45.430Z");
        pg.setVision(53, 69, 53);
        pg.setMap(224, 194, 163);
        pg.setControl(192, 192, 192);
        pg.writeJSON();
    }

    public PaletteGenerator(String _name, String _dateCreated) {
        name = _name;
        dateCreated = _dateCreated;
        vision = new int[3];
        map = new int[3];
        control = new int[3];
    }

    public void setVision(int r, int g, int b) {
        vision[0] = r;
        vision[1] = g;
        vision[2] = b;
    }

    public void setMap(int r, int g, int b) {
        map[0] = r;
        map[1] = g;
        map[2] = b;
    }

    public void setControl(int r, int g, int b) {
        control[0] = r;
        control[1] = g;
        control[2] = b;
    }

    public void writeJSON() throws FileNotFoundException {

        JSONObject palette = new JSONObject();

        palette.put("name", name);
        palette.put("dateCreated", dateCreated);

        Map<String, Integer> visionMap = new LinkedHashMap<>();
        visionMap.put("r", vision[0]);
        visionMap.put("g", vision[1]);
        visionMap.put("b", vision[2]);
        palette.put("vision", visionMap);

        Map<String, Integer> mapMap = new LinkedHashMap<>();
        mapMap.put("r", map[0]);
        mapMap.put("g", map[1]);
        mapMap.put("b", map[2]);
        palette.put("map", mapMap);

        Map<String, Integer> controlMap = new LinkedHashMap<>();
        controlMap.put("r", control[0]);
        controlMap.put("g", control[1]);
        controlMap.put("b", control[2]);
        palette.put("control", controlMap);

        PrintWriter pw = new PrintWriter(String.format("palettes/%s.json", name));
        pw.println(palette);
        pw.close();

    }

}
