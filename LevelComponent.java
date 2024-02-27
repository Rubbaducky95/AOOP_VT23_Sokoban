import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * The {@link  LevelComponent} class represents a custom Swing component used for rendering a level map with interactive objects.
 * It extends {@link javax.swing.JComponent} and implements {@link java.io.Serializable} interface, allowing objects of this class
 * to be serialized and deserialized.
 * The component displays the map grid and interactive objects using provided icons.
 */
public class LevelComponent extends JComponent implements Serializable {

    /**
     * {@code String} matrix representing the map grid.
     */
    private final GameGrid map;

    /**
     * {@code String} matrix representing the movable objects on the map grid.
     */
    private final GameGrid interactive;

    /**
     * The blank tile icon (null).
     */
    private final BufferedImage blank;

    /**
     * The marked tile icon ("redmarker").
     */
    private final BufferedImage redmarker;

    /**
     * The crate icon ("box).
     */
    private final BufferedImage box;

    /**
     * The marked crate icon ("boxmarked").
     */
    private final BufferedImage boxmarked;

    /**
     * The player icon ("player").
     */
    private final BufferedImage player;

    /**
     * The wall icon ("wall").
     */
    private final BufferedImage wall;

    /**
     * The width of the imported .png files.
     */
    private final int iconWidth;

    /**
     * The height of the imported .png files.
     */
    private final int iconHeight;

    /**
     * Constructs a {@link LevelComponent} object with the specified map, interactive objects, and icons.
     *
     * @param map
     *      Contains information needed to draw the map
     * @param interactive
     *      Contains information needed to draw the interactive objects on the map
     * @param icons
     *      The icons representing different elements in the game
     * @throws IOException
     *      If an I/O error occurs while reading the icons
     */
    public LevelComponent(GameGrid map, GameGrid interactive, File[] icons) throws IOException {

        this.map = map;
        this.interactive = interactive;

        BufferedImage[] bufferedIcons = new BufferedImage[6];

        for(int i = 0; i < icons.length; i++)
            bufferedIcons[i] = ImageIO.read(icons[i]);

        blank = bufferedIcons[0];
        redmarker = bufferedIcons[1];
        player = bufferedIcons[2];
        box = bufferedIcons[3];
        boxmarked = bufferedIcons[4];
        wall = bufferedIcons[5];

        iconWidth = bufferedIcons[0].getWidth();
        iconHeight = bufferedIcons[0].getHeight();

        setPreferredSize(new Dimension(iconWidth * map.getGrid().length,
                iconHeight * map.getGrid()[0].length));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int i = 0; i < map.getGrid().length; i++){
            for(int j = 0; j < map.getGrid()[0].length; j++){
                String type = map.getValue(i, j);
                if (type == null) {
                    g.drawImage(blank, i * iconWidth, j * iconHeight, this);
                } else {
                    switch (type) {
                        case "redmarker" -> g.drawImage(redmarker, i * iconWidth, j * iconHeight, this);
                        case "wall"  -> g.drawImage(wall, i * iconWidth, j * iconHeight, this);
                    }
                }
            }
        }

        for(int i = 0; i < interactive.getGrid().length; i++){
            for(int j = 0; j < interactive.getGrid()[0].length; j++){
                String type = interactive.getValue(i, j);
                if(type != null){
                    switch (type) {
                        case "box" -> g.drawImage(box, i * iconWidth, j * iconHeight, this);
                        case "boxmarked" -> g.drawImage(boxmarked, i * iconWidth, j * iconHeight, this);
                        case "player" -> g.drawImage(player, i * iconWidth, j * iconHeight, this);
                    }
                }
            }
        }
    }
}