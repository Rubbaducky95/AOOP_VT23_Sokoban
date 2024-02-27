import javax.swing.JComponent;
import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The Controller class is responsible for handling user input and controlling the game logic in the Sokoban game.
 * It extends the {@link View} class and implements the {@link java.io.Serializable} interface, allowing objects of this class
 * to be serialized and deserialized.
 */
public class Controller extends View implements Serializable{

    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * Array of {@link Model}s that represents the levels of the game.
     */
    private final Model[] models;

    /**
     * Current {@link Model} being controlled.
     */
    private Model model;

    /**
     * Current center component being controlled of the type {@link LevelComponent} .
     */
    public JComponent levelComponent;

    /**
     * Total amount of levels imported.
     */
    private final int maxNOfLevels;

    /**
     * Name of saved file when "save" button is pressed.
     */
    private String savedDataName;

    /**
     * List of {@link SoundObserver}s.
     */
    private final List<SoundObserver> soundObservers;

    /**
     * Handles all the imported sounds.
     */
    private final SoundManager soundManager;

    /**
     *
     * Constructs a new Controller object with the given {@link Model}s.
     * Also adds sounds to the {@link SoundManager} with keys representing different sounds.
     *
     * @param dataModel an array of Model objects representing the game data models
     * @throws IOException if an I/O error occurs during sound file loading
     */
    public Controller(Model[] dataModel) throws IOException {

        super(dataModel);
        this.models = dataModel;
        this.maxNOfLevels = models.length;
        this.soundObservers = new ArrayList<>();
        this.soundManager = new SoundManager();
        soundManager.addSound("move", "soundeffects/step2.wav");
        soundManager.addSound("reset", "soundeffects/reset.wav");
        soundManager.addSound("win", "soundeffects/win.wav");
        soundManager.addSound("moveBox","soundeffects/move_box.wav");
        soundManager.addSound("newLvl","soundeffects/next_level.wav");
        soundManager.addSound("stuck","soundeffects/stuck.wav");
        addSoundObserver(soundManager);
    }

    /**
     * Handles the movement of the player in the specified direction.
     * @param direction the direction of movement ("up", "down", "left", "right")
     * @param position the current position of the player
     */
    private void playerMove(String direction, Point position) {

        String playerMoveTile = checkMove(direction, position);

        if(playerMoveTile == null || playerMoveTile.equals("redmarker")){
            notifySoundObservers("move");
            Point newPosition = new Point(position.x, position.y);
            switch (direction) {
                case "up" -> newPosition.y -= 1;
                case "down" -> newPosition.y += 1;
                case "left" -> newPosition.x -= 1;
                case "right" -> newPosition.x += 1;
            }

            GameGrid old = model.getInteractive();
            old.setValue(position.x, position.y, null);
            old.setValue(newPosition.x, newPosition.y, "player");
            model.setInteractive(old);
            model.setPlayerPos(newPosition);
        } else {
            if (playerMoveTile.equals("box") || playerMoveTile.equals("boxmarked")) {
                Point boxMovePosition = new Point(position.x, position.y);
                switch (direction) {
                    case "up" -> boxMovePosition.y -= 1;
                    case "down" -> boxMovePosition.y += 1;
                    case "left" -> boxMovePosition.x -= 1;
                    case "right" -> boxMovePosition.x += 1;
                }

                boxMove(direction, boxMovePosition);
            }
        }
    }

    /**
     * Checks if the player's movement in the specified direction is valid or blocked.
     * @param direction the direction of movement ("up", "down", "left", "right")
     * @param position the current position of the player
     * @return a String representing the type of tile blocking the movement, or null if the movement is valid
     */
    private String checkMove(String direction, Point position) {

        GameGrid map = model.getMap();
        GameGrid interactive = model.getInteractive();

        switch (direction) {
            case "up" -> {//y-1
                if (map.getValue(position.x, position.y - 1) != null) {
                    if (map.getValue(position.x, position.y - 1).equals("wall")) {
                        return map.getValue(position.x, position.y - 1);
                    }

                    if (interactive.getValue(position.x, position.y - 1) != null) {
                        return interactive.getValue(position.x, position.y - 1);
                    }

                    return map.getValue(position.x, position.y - 1);
                }
                if (interactive.getValue(position.x, position.y - 1) != null) {
                    return interactive.getValue(position.x, position.y - 1);
                }
                return null;
            }
            case "down" -> {//y+1
                if (map.getValue(position.x, position.y + 1) != null) {
                    if (map.getValue(position.x, position.y + 1).equals("wall")) {
                        return map.getValue(position.x, position.y + 1);
                    }

                    if (interactive.getValue(position.x, position.y + 1) != null) {
                        return interactive.getValue(position.x, position.y + 1);
                    }

                    return map.getValue(position.x, position.y + 1);
                }
                if (interactive.getValue(position.x, position.y + 1) != null) {
                    return interactive.getValue(position.x, position.y + 1);
                }
                return null;
            }
            case "left" -> {//x-1
                if (map.getValue(position.x - 1, position.y) != null) {
                    if (map.getValue(position.x - 1, position.y).equals("wall")) {
                        return map.getValue(position.x - 1, position.y);
                    }

                    if (interactive.getValue(position.x - 1, position.y) != null) {
                        return interactive.getValue(position.x - 1, position.y);
                    }

                    return map.getValue(position.x - 1, position.y);
                }
                if (interactive.getValue(position.x - 1, position.y) != null) {
                    return interactive.getValue(position.x - 1, position.y);
                }
                return null;
            }
            case "right" -> {//x+1
                if (map.getValue(position.x + 1, position.y) != null) {
                    if (map.getValue(position.x + 1, position.y).equals("wall")) {
                        return map.getValue(position.x + 1, position.y);
                    }

                    if (interactive.getValue(position.x + 1, position.y) != null) {
                        return interactive.getValue(position.x + 1, position.y);
                    }

                    return map.getValue(position.x + 1, position.y);
                }
                if (interactive.getValue(position.x + 1, position.y) != null) {
                    return interactive.getValue(position.x + 1, position.y);
                }
                return null;
            }
        }

        return null;
    }

    /**
     * Moves the box in the specified direction.
     * @param direction the direction of movement ("up", "down", "left", "right")
     * @param position the current position of the box
     */
    private void boxMove(String direction, Point position) {

        String boxMoveTile = checkMove(direction, position);
        if(boxMoveTile == null)
            notifySoundObservers("moveBox");

        boolean moveOk = false;

        if (boxMoveTile != null) {
            if (boxMoveTile.equals("redmarker")) {
                notifySoundObservers("moveBox");
                moveOk = true;
            }
        } else {
            moveOk = true;
        }

        if (moveOk) {
            Point newBoxPosition = new Point(position.x, position.y);
            switch (direction) {
                case "up" -> newBoxPosition.y -= 1;
                case "down" -> newBoxPosition.y += 1;
                case "left" -> newBoxPosition.x -= 1;
                case "right" -> newBoxPosition.x += 1;
            }

            Point oldPlayerPosition = new Point(model.getPlayerPos().x, model.getPlayerPos().y);

            GameGrid old = model.getInteractive();
            old.setValue(oldPlayerPosition.x, oldPlayerPosition.y, null);
            old.setValue(position.x, position.y, "player");
            if (checkMarked(newBoxPosition)) {
                old.setValue(newBoxPosition.x, newBoxPosition.y, "boxmarked");
            } else {
                old.setValue(newBoxPosition.x, newBoxPosition.y, "box");
            }

            model.setInteractive(old);
            model.setPlayerPos(position);

            for (Point p : model.getBoxes()) {
                if (p.x == position.x && p.y == position.y) {
                    p.x = newBoxPosition.x;
                    p.y = newBoxPosition.y;
                }
            }
        }
    }

    /**
     * Checks if the specified box is marked as placed on a red marker tile.
     * @param boxPosition the position of the box
     * @return true if the box is marked, false otherwise
     */
    private boolean checkMarked(Point boxPosition) {
        boolean marked = false;

        for (Point p : model.getRedmarkers()) {
            if (p.x == boxPosition.x && p.y == boxPosition.y) {
                marked = true;
                break;
            }
        }

        return marked;
    }

    /**
     * Checks if the game has been won or if the player is stuck,
     * and updates the game state accordingly.
     */
    private void checkWinAndStuck() {

        boolean win = false;
        boolean stuck = false;
        int counter = 0;
        String[] objects = new String[4];
        Point[] boxes = model.getBoxes();
        for (Point box : boxes) {
            if (checkMarked(box)) {
                counter++;
            } else {
                objects[0] = checkMove("up", box);
                objects[1] = checkMove("down", box);
                objects[2] = checkMove("left", box);
                objects[3] = checkMove("right", box);
                for (int j = 0; j < 4; j++) {
                    if (objects[j] == null) {
                        objects[j] = "null";
                    }
                }

                if (objects[0].equals("wall") || objects[1].equals("wall")) {
                    if (objects[2].equals("wall") || objects[3].equals("wall")) {
                        stuck = true;
                        break;
                    }
                }
            }
        }

        if (stuck) {
            notifySoundObservers("stuck");
            model.setStuck(stuck);
        }

        if (counter == boxes.length) {
            notifySoundObservers("win");
            win = true;
        }

        model.setWin(win);
    }

    /**
     * Moves the player character up when the up arrow key is pressed.
     * It checks if the move is valid, updates the game state accordingly, and
     * checks if the player has won or is stuck. Finally, it redraws the level component.
     */
    public void upArrowKeyPressed() {

        playerMove("up", model.getPlayerPos());
        checkWinAndStuck();
        redraw(levelComponent);
    }

    /**
     * Moves the player character down when the down arrow key is pressed.
     * It checks if the move is valid, updates the game state accordingly, and
     * checks if the player has won or is stuck. Finally, it redraws the level component.
     */
    public void downArrowKeyPressed() {

        playerMove("down", model.getPlayerPos());
        checkWinAndStuck();
        redraw(levelComponent);
    }

    /**
     * Moves the player character to the left when the left arrow key is pressed.
     * It checks if the move is valid, updates the game state accordingly, and
     * checks if the player has won or is stuck. Finally, it redraws the level component.
     */
    public void leftArrowKeyPressed() {

        playerMove("left", model.getPlayerPos());
        checkWinAndStuck();
        redraw(levelComponent);
    }

    /**
     * Moves the player character to the right when the right arrow key is pressed.
     * It checks if the move is valid, updates the game state accordingly, and
     * checks if the player has won or is stuck. Finally, it redraws the level component.
     */
    public void rightArrowKeyPressed() {

        playerMove("right", model.getPlayerPos());
        checkWinAndStuck();
        redraw(levelComponent);
    }

    /**
     * Returns the center component of the view based on the provided model.
     *
     * @param m The model object to create the center component for.
     * @return The center component representing the game level.
     * @throws IOException If an I/O error occurs while creating the component.
     */
    @Override
    public JComponent centerComponent(Model m) throws IOException {

        setModel(m);
        levelComponent = new LevelComponent(model.getMap(),model.getInteractive(),model.getFiles());
        return levelComponent;
    }

    /**
     * Resets the game state to the initial state, including player position,
     * box positions, and interactive grid. It also clears the win status and
     * redraws the level component. Additionally, it notifies sound observers
     * about the reset event.
     */
    @Override
    public void resetPressed() {

        model.setInteractive(model.getInitialInteractive().clone());
        Point[] newBoxes = new Point[model.getInitialBoxes().length];
        for (int i = 0; i < model.getInitialBoxes().length; i++) {
            newBoxes[i] = new Point(model.getInitialBoxes()[i]);
        }

        model.setBoxes(newBoxes);
        model.setPlayerPos(model.getInitialPlayerPos());
        model.setWin(false);

        try {
            newCenterComponent(model);
        } catch (IOException e) {
            e.printStackTrace();
        }

        notifySoundObservers("reset");
        redraw(levelComponent);
    }

    /**
     * Resets the game state to the initial state without playing the reset sound.
     * It performs the same steps as the {@code resetPressed()} method but skips
     * notifying sound observers about the reset event.
     */
    public void resetWithoutSound() {

        model.setInteractive(model.getInitialInteractive().clone());
        Point[] newBoxes = new Point[model.getInitialBoxes().length];
        for (int i = 0; i < model.getInitialBoxes().length; i++) {
            newBoxes[i] = new Point(model.getInitialBoxes()[i]);
        }

        model.setBoxes(newBoxes);
        model.setPlayerPos(model.getInitialPlayerPos());
        model.setWin(false);

        try {
            newCenterComponent(model);
        } catch (IOException e) {
            e.printStackTrace();
        }

        redraw(levelComponent);
    }

    /**
     * Handles the next button press event, which advances the game to the next level.
     * It checks if there is a next level available and updates the model accordingly.
     * If there is no next level, it displays a warning message. Finally, it redraws
     * the level component and notifies sound observers about the new level event.
     *
     * @return {@code true} if the next level was successfully loaded, {@code false} otherwise.
     */
    @Override
    public boolean nextButtonPressed() {

        int index = model.getLevel() + 1;
        if (index < 0 || index >= maxNOfLevels) {
            warningMessage("You are already at the last level!");
            return false;
        }

        notifySoundObservers("newLvl");
        changeLevel(index);
        return true;
    }

    /**
     * Handles the previous button press event, which goes back to the previous level.
     * It checks if there is a previous level available and updates the model accordingly.
     * If there is no previous level, it displays a warning message. Finally, it redraws
     * the level component and notifies sound observers about the previous level event.
     *
     * @return {@code true} if the previous level was successfully loaded, {@code false} otherwise.
     */
    @Override
    public boolean previousButtonPressed() {

        int index = model.getLevel() - 1;
        if (index < 0 || index >= maxNOfLevels) {
            warningMessage("You are already at the first level!");
            return false;
        }

        notifySoundObservers("newLvl");
        changeLevel(index);
        return true;
    }

    /**
     * Sets the model for the controller. This method allows the controller to interact
     * with the game model by storing a reference to it.
     *
     * @param dataModel The game model to be set.
     */
    private void setModel(Model dataModel) {
        this.model = dataModel;
    }

    /**
     * Changes the current level to the given index. It updates the model's level,
     * loads the new level's data, resets the game state, and redraws the level component.
     *
     * @param index The index of the new level.
     */
    public void changeLevel(int index) {

        if (models[index] == model) {
            warningMessage("That level is already selected!");
        } else {
            setModelView(models[index]);
            setModel(models[index]);
            resetWithoutSound();
        }
    }

    /**
     * Handles the save button press event, which triggers the saving of the current game state.
     * It prompts the user to select a file location and saves the game state using the model's
     * saveGame method. If the saving process is successful, it displays a success message;
     * otherwise, it displays an error message.
     */
    public void saveButtonPressed(File savedFile) {

        savedDataName = savedFile.getName();
        ObjectOutputStream out = null;

        try{
            out = new ObjectOutputStream(new FileOutputStream(savedFile.getAbsolutePath()));
        } catch(IOException e) {
            e.printStackTrace();
        }

        try{
            out.writeObject(this.model);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        infoMessage("Leveled saved successfully as " + savedDataName);
    }

    /**
     * Handles the load button press event, which triggers the loading of a saved game state.
     * It prompts the user to select a saved game file, and if the file is valid, it loads
     * the game state using the model's loadGame method. If the loading process is successful,
     * it updates the game state and redraws the level component; otherwise, it displays
     * an error message.
     */
    public void loadButtonPressed(File chosenFile){

        ObjectInputStream in = null;
        try{
            in = new ObjectInputStream(new FileInputStream(chosenFile.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assert in != null;
            this.model = (Model) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        setModelView(this.model);
        try{
            newCenterComponent(model);
        } catch (IOException e) {
            e.printStackTrace();
        }
        redraw(levelComponent);
        infoMessage("Level loaded successfully.");
    }

    /**
     * Adds a sound observer to the list of observers. The sound observer will be notified
     * when specific sound events occur in the game.
     *
     * @param observer The sound observer to be added.
     */
    public void addSoundObserver(SoundObserver observer){
        soundObservers.add(observer);
    }

    /**
     * Removes a sound observer from the list of observers. The sound observer will no longer
     * receive notifications about sound events in the game.
     *
     * @param observer The sound observer to be removed.
     */
    public void removeSoundObserver(SoundObserver observer) {
        soundObservers.remove(observer);
    }

    /**
     * Notifies all sound observers about a sound event in the game. The specific sound event
     * is indicated by the eventType parameter.
     *
     * @param soundKey Name of the type of sound to be notified (defined in {@link Controller}) constructor.
     */
    private void notifySoundObservers(String soundKey) {
        soundManager.KeyPress(soundKey);
    }

    /**
     * Displaying the winning message to the console instead of the graphical view.
     */
    public void consoleGameControllerWin() {
        boolean exit = false;
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        String message = "\nCongratulations!\n"
                + "To reset, enter \"reset\"\n\n"
                + "To change to the previous level, enter \"previous\"\n\n"
                + "To change to the next level, enter \"next\"\n"
                + "Enter command: ";

        printToConsole(message);

        while (!exit) {
            String input = scanner.nextLine();
            if (input != null) {
                if (input.equalsIgnoreCase("reset")) {
                    resetPressed();
                    exit = true;
                }

                else if (input.equalsIgnoreCase("previous")) {
                    exit = previousButtonPressed();
                    if (!exit) {
                        printToConsole("Enter command: ");
                    }
                }

                else if (input.equalsIgnoreCase("next")) {
                    exit = nextButtonPressed();
                    if (!exit) {
                        printToConsole("Enter command: ");
                    }
                }

                else {
                    printToConsole(message);
                }
            }
        }
    }

    /**
     * Controlling the game through the console instead of the graphical view.
     */
    public void consoleGameController() {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        String helpMessage = "Console based view\n"
                + "To move enter either one of these to the console:\n"
                + "\"up\"\n"
                + "\"down\"\n"
                + "\"left\"\n"
                + "\"right\"\n\n"
                + "To reset the level, enter: \"reset\"\n\n"
                + "To change the level, enter: \"change\"\n\n"
                + "To display this message, enter: \"help\"\n"
                + "To display information about the game, enter: \"info\"\n"
                + "To return to the graphical view, enter: \"return\"\n"
                + "To exit/terminate, enter \"exit\"\n";

        printToConsole(helpMessage);
        redraw(levelComponent);
        while (!exit) {
            printToConsole("Enter command: ");
            String input = scanner.nextLine();
            printToConsole("");

            if (input != null) {

                if (input.equalsIgnoreCase("return")) {
                    exit = true;
                }

                else if (input.contentEquals("exit")) {
                    printToConsole("Program terminated");
                    System.exit(0);
                }

                else if (input.equalsIgnoreCase("help")) {
                    printToConsole(helpMessage);
                    redraw(levelComponent);
                }

                else if (input.equalsIgnoreCase("reset")) {
                    resetPressed();
                }

                else if (input.equalsIgnoreCase("up")) {
                    upArrowKeyPressed();
                }

                else if (input.equalsIgnoreCase("down")) {
                    downArrowKeyPressed();
                }

                else if (input.equalsIgnoreCase("left")) {
                    leftArrowKeyPressed();
                }

                else if (input.equalsIgnoreCase("right")) {
                    rightArrowKeyPressed();
                }

                else if (input.equalsIgnoreCase("change")){
                    printToConsole("There are three levels to choose from.\n"
                            + "1, 2 and 3\n"
                            + "Enter new level number: ");
                    int lvl = scanner.nextInt();
                    printToConsole("");
                    changeLevel(lvl-1);
                }

                else if (input.equalsIgnoreCase("info")){
                    printToConsole("Welcome to Sokoban!\n"
                            + "Created by:\nJacob Käki\nRuben Croall\nDouglas Jonsson Lundqvist\n\n"
                            + "Level information:\n"
                            + "Level 1: Easy\n"
                            + "Level 2: Intermediate\n"
                            + "Level 3: Hard\n");
                }

                else {
                    printToConsole("Unknown input! Enter \"help\" to see valid commands.\n");
                }
            }
        }
    }
}