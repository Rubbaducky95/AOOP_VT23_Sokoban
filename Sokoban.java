import java.io.*;

/**
 * The {@code Sokoban} class is a test class that demonstrates reading files and creating models.
 * It creates instances of {@link MapFileReader} for different levels and uses them to create models.
 * The models are then used to create a {@link Controller} instance.
 */
public class Sokoban {

    /**
     * The main method serves as the entry point for the program.
     *
     * @param args the command-line arguments
     * @throws IOException if an I/O error occurs while reading the files
     */
    public static void main(String[] args) throws IOException {

        File[] icons = new File[] {
                new File("sokoban_icons/blank.png"),
                new File("sokoban_icons/blankmarked.png"),
                new File("sokoban_icons/player.png"),
                new File("sokoban_icons/crate.png"),
                new File("sokoban_icons/cratemarked.png"),
                new File("sokoban_icons/wall.png")};

        //LVL 1
        MapFileReader mfr1 = new MapFileReader("levels/custom/lvl1_map.txt","levels/custom/lvl1_interactive.txt");

        //LVL 2
        MapFileReader mfr2 = new MapFileReader("levels/custom/lvl2_map.txt","levels/custom/lvl2_interactive.txt");

        //LVL 3
        MapFileReader mfr3 = new MapFileReader("levels/custom/lvl3_map.txt","levels/custom/lvl3_interactive.txt");

        //LVL 4
        MapFileReader mfr4 = new MapFileReader("levels/custom/lvl4_map.txt","levels/custom/lvl4_interactive.txt");

        Model[] m = new Model[] {
                mfr1.createModelFromFiles(mfr1.mapFilePath, mfr1.interactiveFilePath, 0, icons),
                mfr2.createModelFromFiles(mfr2.mapFilePath, mfr2.interactiveFilePath, 1, icons),
                mfr3.createModelFromFiles(mfr3.mapFilePath, mfr3.interactiveFilePath, 2, icons),
                mfr4.createModelFromFiles(mfr4.mapFilePath, mfr4.interactiveFilePath, 3, icons)};

        Controller c = new Controller(m);
    }
}
