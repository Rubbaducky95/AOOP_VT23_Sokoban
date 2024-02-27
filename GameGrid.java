import java.io.Serializable;

/**
 * The {@code GameGrid} class represents a grid used in the game, providing functions to access and modify
 * values in the grid. It implements the {@link Serializable} interface to support object serialization.
 * The grid is represented by a 2D array of strings, where each string represents an icon in the game.
 * The class also provides methods to retrieve the current grid, get a value at a specific position, set a value
 * at a specific position, and create a clone of the grid.
 */
public class GameGrid implements Serializable {

    /**
     * Matrix of {@code String}s to represent the map grid.
     */
    private String[][] gameGrid;

    /**
     * Creates a {@link GameGrid} with functions to find values, set values, and clone map.
     *
     * @param gameGrid  Matrix of {@code String}s representing the map in a 2D environment.
     */
    public GameGrid(String[][] gameGrid) {
        this.gameGrid = gameGrid;
    }

    /**
     * Get the current {@link GameGrid}.
     *
     * @return  Matrix of {@code String}s representing the map in a 2D environment.
     */
    public String[][] getGrid(){
        return gameGrid;
    }

    /**
     * Get a value from a position in the map.
     *
     * @param x     Column
     * @param y     Row
     * @return      Object in position [x][y] in the {@code String[][]}
     */
    public String getValue(int x, int y) {
        return gameGrid[x][y];
    }

    /**
     * Sets a value in a position in the map.
     *
     * @param x         Column
     * @param y         Row
     * @param value     {@code String} or {@code null} which represents icons in the game.
     */
    public void setValue(int x, int y, String value) {
        gameGrid[x][y] = value;
    }

    /**
     * Creates a clone of the current {@link GameGrid} to be used when resetting map.
     *
     * @return  Copy of the current {@link GameGrid}.
     */
    public GameGrid clone(){
        String[][] cloneGrid = new String[gameGrid.length][gameGrid[0].length];
        for(int i = 0; i < gameGrid.length; i++){
            for(int j = 0; j < gameGrid[0].length; j++){
                if (gameGrid[i][j] != null) {
                    cloneGrid[i][j] = new String(gameGrid[i][j]);
                } else {
                    cloneGrid[i][j] = null;
                }
            }
        }

        return new GameGrid(cloneGrid);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gameGrid[0].length; i++) {
            for (int j = 0; j < gameGrid.length; j++) {
                sb.append(gameGrid[j][i]);
                if (gameGrid[j][i] != null) {
                    if (gameGrid[j][i].length() != 9) {
                        int blanks = 9 - gameGrid[j][i].length();
                        for (int k = 0; k < blanks; k++) {
                            sb.append(" ");
                        }
                    }
                } else {
                    sb.append("     ");
                }
                if (j < gameGrid[j].length - 1) {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}