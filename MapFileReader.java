import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code MapFileReader} class provides functionality to read .txt files
 * containing map data and create a matrix of strings representing the map.
 * It also supports the creation of a {@link Model} using the map data and image files.
 * The class offers methods to read a map file, create a model from files, and perform
 * various operations on the map data.
 */
public class MapFileReader {

    /**
     * The file path name to the .txt file
     * containing the map of the stationary objects.
     */
    public final String mapFilePath;

    /**
     * The file path name to the .txt file
     * containing the map of the interactive objects.
     */
    public final String interactiveFilePath;

    /**
     * Creates a {@link MapFileReader} which can use functions
     * such as {@code readMapFile} to import .txt files of a certain format
     * (read README.txt in levels/custom folder), and create
     * a matrix of Strings, which in turn can be used in {@code createModelFromFiles}
     * to create a {@link Model} to be used to display the game.
     *
     * @param map           File path name to the desired .txt file (the map).
     *
     * @param interactive   File path name to the desired .txt file
     *                      (the interactive map).
     */
    public MapFileReader(String map, String interactive) {
        this.mapFilePath = map;
        this.interactiveFilePath = interactive;
    }

    /**
     * Is used in {@code createModelFromFiles} to read a .txt file of a certain format
     * and places the content in a matrix of {@code String}s
     * in a certain way such as it can be interpreted by {@link GameGrid} to create a map out of it.
     *
     * @param txtFilePathName   File path name to the desired .txt file
     *
     * @return                  Matrix of Strings with the content of the .txt file
     *                          from the {@code txtFilePathName}.
     *
     * @throws                  IOException if file path name is incorrect or a file
     *                          is not present at that location.
     */
    public String[][] readMapFile(String txtFilePathName) throws IOException {

        String line;
        BufferedReader bf = new BufferedReader(new FileReader(txtFilePathName));
        List<String> data = new ArrayList<String>();

        //Add strings from file to data array and convert "null" to null
        while((line = bf.readLine()) != null) {
            if(line.equals("null"))
                data.add(null);
            else
                data.add(line);
        }
        bf.close();

        //Figure out width and height of map (length of double String array)
        int i = 0;
        int j = 1;
        for(String s : data){
            i++;
            if(s != null){
                if(s.equals("next")){
                    j++;
                    i = 0;
                }
            }
        }
        String[][] map = new String[i][j];

        //Add elements in their correct positions in the map
        i = 0;
        j = 0;
        for(String s : data){
            if(s == null)
                i++;
            else if(s != null){
                if(!s.equals("next")){
                    map[i][j] = s;
                    i++;
                }
                else{
                    i = 0;
                    j++;
                }
            }
        }

        return map;
    }

    /**
     * Creates a model which can be used by {@link View} to display the game.
     *
     * @param mapTxt            File path name to the .txt file which contains the map.
     * @param interactiveTxt    File path name to the .txt file which contains the interactive map.
     * @param lvlNr             Level ID for the map to be created.
     * @param icons             {@code File[]} which contains the .png image-files to be used in the game.
     * @return                  {@link Model} derived from the .txt files and the image-files imported.
     * @throws                  IOException if file path name is incorrect or a file
     *                          is not present at that location.
     */
    public Model createModelFromFiles(String mapTxt, String interactiveTxt, int lvlNr, File[] icons) throws IOException {

        String[][] map = readMapFile(mapTxt);
        String[][] interactive = readMapFile(interactiveTxt);

        //Count number of redmarkers and boxes to create the Point array for each
        int nOfRedmarkers = 0;
        int nOfBoxes = 0;
        for(String[] column : map) {
            for(String s : column){
                if(s != null) {
                    if (s.equals("redmarker"))
                        nOfRedmarkers++;
                }
            }
        }

        for(String[] column : interactive) {
            for(String s : column){
                if(s != null) {
                    if(s.equals("box") || s.equals("boxmarked"))
                        nOfBoxes++;
                }
            }
        }

        //Create Point arrays
        Point[] redmarkers = new Point[nOfRedmarkers];
        Point[] boxes = new Point[nOfBoxes];
        Point player = new Point();

        //Find x-, and y-coordinates for each interesting Point, create a Point and place it in the Point array
        int i = 0;
        int j = 0;
        for(int k = 0; k < map.length; k++){
            for (int l = 0; l < map[0].length; l++){
                if(map[k][l] != null){
                    if(map[k][l].equals("redmarker")){
                        redmarkers[i] = new Point(k,l);
                        i++;
                    }
                }
                if(interactive[k][l] != null){
                    if(interactive[k][l].equals("box") || interactive[k][l].equals("boxmarked")){
                        boxes[j] = new Point(k,l);
                        j++;
                    }
                    if(interactive[k][l].equals("player"))
                        player = new Point(k,l);
                }
            }
        }

        GameGrid ggMap = new GameGrid(map);
        GameGrid ggInteractive = new GameGrid(interactive);
        return new Model(ggMap,ggInteractive, redmarkers, boxes, player, lvlNr, icons);
    }
}