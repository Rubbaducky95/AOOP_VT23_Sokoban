import java.awt.Point;
import java.io.File;
import java.io.Serializable;

/**
 * The {@code Model} class represents the game model in the application.
 * It contains information about the game state, controlled and changed by {@link Controller},
 * such as the map, player position, boxes, red markers,
 * level number, and other related data.
 *
 * Types of objects in the game:
 * <ul>
 * <li> The player, defined as "player"
 * <li> Movable boxes, defined as "box"
 * <li> Boxes on marked tiles, defined as "boxmarked"
 * <li> Marked tiles, defined as "redmarker"
 * <li> Walls encircling the playable area, defined as "wall"
 * <li> Blank tiles, defined as null (not the string "null," but an actual null value).
 * </ul>
 *
 * <p>
 * This class implements the {@link java.io.Serializable} interface, allowing objects of this class
 * to be serialized and deserialized.
 * </p>
 */
public class Model implements Serializable {

    /**
     * {@code String} matrix representing the map grid.
     */
    private GameGrid map;

    /**
     * {@code String} matrix representing the movable objects on the map grid.
     */
    private GameGrid interactive;

    /**
     * xy-coordinates each representing a marked tile on the map grid.
     */
    private Point[] redmarkers;

    /**
     * xy-coordinates each representing a box on the map grid.
     */
    private Point[] boxes;

    /**
     * xy-coordinate representing the players starting position.
     */
    private Point playerPos;

    /**
     * Level number for the current {@link Model} being created.
     */
    private int levelNo;

    /**
     * Condition if level has been completed or not.
     */
    private boolean win;

    /**
     * Condition if player has put a box in a corner.
     */
    private boolean stuck;

    /**
     * Array of .png {@code Files}s containing the icons used in the game.
     */
    private File[] files;

    /**
     * Copy of the initial layout of the interactive map grid.
     */
    private final GameGrid initialInteractive;

    /**
     * Copy of the xy-coordinates for the initial positions of all boxes.
     */
    private final Point[] initialBoxes;

    /**
     * Copy of the players initial position.
     */
    private final Point initialPlayerPos;

    /**
     * Condition if console view is on or off.
     */
    private boolean displayToConsole;

    /**
     * Constructs a {@link Model} object with the specified initial game state.
     *
     * @param map         The game map grid in the form of a {@link GameGrid}.
     *
     * @param interactive The interactive grid representing the game objects
     *                    in the form of a {@link GameGrid}.
     *
     * @param redmarkers  An array of red marker positions.
     *
     * @param boxes       An array of box positions.
     *
     * @param playerPos   The initial player position.
     *
     * @param levelNo     The current level number.
     *
     * @param files       An array of image files.
     */
    public Model(GameGrid map, GameGrid interactive, Point[] redmarkers, Point[] boxes, Point playerPos, int levelNo, File[] files){

        this.map = map;
        this.interactive = interactive;
        this.redmarkers = redmarkers;
        this.boxes = boxes;
        this.playerPos = playerPos;
        this.levelNo = levelNo;
        this.win = false;
        this.stuck = false;
        this.files = files;
        this.initialInteractive = interactive.clone();
        this.initialBoxes = new Point[boxes.length];
        for (int i = 0; i < boxes.length; i++) {
            initialBoxes[i] = new Point(boxes[i]);
        }

        this.initialPlayerPos = new Point(playerPos);
        this.displayToConsole = false;
    }

    /**
     * Returns the initial interactive grid representing the game objects.
     * @return The initial interactive grid.
     */
    public GameGrid getInitialInteractive() {
        return initialInteractive;
    }

    /**
     * Returns the array of initial box positions.
     * @return The array of initial box positions.
     */
    public Point[] getInitialBoxes() {
        return initialBoxes;
    }

    /**
     * Returns the initial player position.
     * @return The initial player position.
     */
    public Point getInitialPlayerPos() {
        return initialPlayerPos;
    }

    /**
     * Returns the game map grid.
     * @return The game map grid.
     */
    public GameGrid getMap(){
        return map;
    }

    /**
     * Sets a new game map grid.
     * @param newMap The new game map grid.
     */
    public void setMap(GameGrid newMap) {
        map = newMap;
    }

    /**
     * Returns the interactive grid representing the game objects.
     * @return The interactive grid.
     */
    public GameGrid getInteractive(){
        return interactive;
    }

    /**
     * Sets a new interactive grid representing the game objects.
     * @param newInteractive The new interactive grid.
     */
    public void setInteractive(GameGrid newInteractive) {
        interactive = newInteractive;
    }

    /**
     * Returns the array of red marker positions.
     *
     * @return The array of red marker positions.
     */
    public Point[] getRedmarkers(){
        return redmarkers;
    }

    /**
     * Sets a new array of red marker positions.
     *
     * @param newRedmarkers The new array of red marker positions.
     */
    public void setRedmarkers(Point[] newRedmarkers) {
        redmarkers = newRedmarkers;
    }

    /**
     * Returns the array of box positions.
     *
     * @return The array of box positions.
     */
    public Point[] getBoxes(){
        return boxes;
    }

    /**
     * Sets a new array of box positions.
     *
     * @param newBoxes The new array of box positions.
     */
    public void setBoxes(Point[] newBoxes) {
        boxes = newBoxes;
    }

    /**
     * Returns the player position.
     *
     * @return The player position.
     */
    public Point getPlayerPos() {
        return playerPos;
    }

    /**
     * Sets a new player position.
     *
     * @param newPlayerPos The new player position.
     */
    public void setPlayerPos(Point newPlayerPos) {
        playerPos = newPlayerPos;
    }

    /**
     * Returns the current level number.
     *
     * @return The current level number.
     */
    public int getLevel(){
        return levelNo;
    }

    /**
     * Sets a new level number.
     *
     * @param newLevelNo The new level number.
     */
    public void setLevel(int newLevelNo) {
        levelNo = newLevelNo;
    }

    /**
     * Returns whether the player has won the game.
     *
     * @return true if the player has won, false otherwise.
     */
    public boolean getWin() {
        return win;
    }

    /**
     * Sets the win status of the game.
     *
     * @param newWin The win status of the game.
     */
    public void setWin(boolean newWin) {
        win = newWin;
    }

    /**
     * Returns whether the player is stuck and cannot make any moves.
     *
     * @return true if the player is stuck, false otherwise.
     */
    public boolean getStuck() { return stuck; }

    /**
     * Sets the stuck status of the player.
     *
     * @param newStuck The stuck status of the player.
     */
    public void setStuck(boolean newStuck) { stuck = newStuck; }

    /**
     * Returns the array of image files.
     *
     * @return The array of image files.
     */
    public File[] getFiles() {
        return files;
    }

    /**
     * Sets a new array of image files.
     *
     * @param newFiles The new array of image files.
     */
    public void setFiles(File[] newFiles) {
        files = newFiles;
    }

    /**
     * Returns whether to display game updates to the console.
     *
     * @return true if game updates should be displayed to the console, false otherwise.
     */
    public boolean getDisplayToConsole() {
        return displayToConsole;
    }

    /**
     * Sets whether to display game updates to the console.
     *
     * @param yesNo true to display game updates to the console, false otherwise.
     */
    public void setDisplayToConsole(boolean yesNo) {
        displayToConsole = yesNo;
    }
}