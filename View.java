import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The abstract View class represents the view component in the MVC pattern used for the game.
 * It provides abstract methods to be implemented by the {@link Controller} class.
 */
public abstract class View implements Serializable {

    /**
     * Returns the main component to be displayed in the center of the frame.
     *
     * @param dataModel the data model containing the data to be displayed
     * @return the main component to be displayed
     * @throws IOException if an I/O error occurs while loading the component
     */
    public abstract JComponent centerComponent(Model dataModel) throws IOException;

    /**
     * Notifies the view that the "Reset" button has been pressed.
     */
    public abstract void resetPressed();

    /**
     * Notifies the view that the up arrow key has been pressed.
     */
    public abstract void upArrowKeyPressed();

    /**
     * Notifies the view that the down arrow key has been pressed.
     */
    public abstract void downArrowKeyPressed();

    /**
     * Notifies the view that the left arrow key has been pressed.
     */
    public abstract void leftArrowKeyPressed();

    /**
     * Notifies the view that the right arrow key has been pressed.
     */
    public abstract void rightArrowKeyPressed();

    /**
     * Checks if the "Next" button has been pressed.
     *
     * @return true if the "Next" button has been pressed, false otherwise
     */
    public abstract boolean nextButtonPressed();

    /**
     * Checks if the "Previous" button has been pressed.
     *
     * @return true if the "Previous" button has been pressed, false otherwise
     */
    public abstract boolean previousButtonPressed();

    /**
     * Changes the level to the specified index.
     *
     * @param index the index of the level to be changed to
     */
    public abstract void changeLevel(int index);

    /**
     * Notifies the view that the "Save" button has been pressed.
     */
    public abstract void saveButtonPressed(File savedFile);

    /**
     * Notifies the view that the "Load" button has been pressed.
     */
    public abstract void loadButtonPressed(File chosenFile);

    /**
     * Plays the game through the console.
     */
    public abstract void consoleGameController();

    /**
     * Displays the winning message through the console.
     */
    public abstract void consoleGameControllerWin();

    /**
     * Array of {@link Model}s that represents the levels of the game.
     */
    private final Model[] models;

    /**
     * Current {@link Model} being displayed.
     */
    private Model currentModel;

    /**
     * {@code JFrame} displaying all components.
     */
    private final JFrame frame;

    /**
     * {@code JDialog} displayed when win condition is met.
     */
    private final JDialog dialogWin;

    /**
     * {@link LevelComponent} containing all icons to display
     * according to current map grid in the center.
     */
    private JComponent centerComponent;

    /**
     * {@code JLabel} displaying the current level.
     */
    private final JLabel currentLevel;

    /**
     * {@code JFileChooser} choosing a file.
     */
    private final JFileChooser fileChooser;
    /**
     * Constructs a View object with the given data model.
     *
     * @param dataModel the array of data models
     * @throws IOException if an I/O error occurs while constructing the view
     */
    public View(Model[] dataModel) throws IOException {

        this.models = dataModel;
        this.currentModel = dataModel[0];

        Color backgroundColor = new Color(222, 214, 173);

        this.fileChooser = new JFileChooser("save_files/");
        fileChooser.setBackground(backgroundColor);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".dat", "dat");
        fileChooser.setFileFilter(filter);

        this.frame = new JFrame();
        frame.setBackground(backgroundColor);
        frame.setLayout(new BorderLayout());

        this.currentLevel = new JLabel("Level " + (currentModel.getLevel() + 1) + " ");

        JLabel resetMessage = new JLabel("Press \"r\" to reset");

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(resetMessage, BorderLayout.WEST);
        topPanel.add(currentLevel, BorderLayout.EAST);
        topPanel.setBackground(backgroundColor);

        this.centerComponent  = centerComponent(currentModel);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerComponent, BorderLayout.CENTER);
        frame.setFocusable(true);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_UP -> upArrowKeyPressed();
                    case KeyEvent.VK_DOWN -> downArrowKeyPressed();
                    case KeyEvent.VK_LEFT -> leftArrowKeyPressed();
                    case KeyEvent.VK_RIGHT -> rightArrowKeyPressed();
                    case KeyEvent.VK_R -> resetPressed();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        String[] levels = new String[models.length];
        for (int i = 0; i < models.length; i++) {
            levels[i] = new String("Level " + (models[i].getLevel() + 1));
        }

        JComboBox<String> levelSelector = new JComboBox<String>(levels);
        levelSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeLevel(levelSelector.getSelectedIndex());
            }
        });

        levelSelector.setRequestFocusEnabled(false);

        JLabel levelSelectorMessage = new JLabel("Level Selector:");

        JButton menuButton = new JButton("Menu");
        menuButton.setRequestFocusEnabled(false);

        JDialog dialogMenu = new JDialog(frame, "Menu", Dialog.ModalityType.DOCUMENT_MODAL);
        dialogMenu.setBounds(132, 132, 200, 150);

        Container dialogMenuContainer = dialogMenu.getContentPane();
        dialogMenuContainer.setLayout(new FlowLayout());
        dialogMenuContainer.setBackground(backgroundColor);

        JButton infoButton = new JButton("Info");
        infoButton.setRequestFocusEnabled(false);
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Welcome to Sokoban!\n"
                        + "Created by:\nJacob Käki\nRuben Croall\nDouglas Jonsson Lundqvist\n\n"
                        + "Level information:\n"
                        + "Level 1: Easy\n"
                        + "Level 2: Intermediate\n"
                        + "Level 3: Hard\n"
                        + "Level 4: Very Hard\n");
            }
        });
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.showSaveDialog(null);
                File savedFile = fileChooser.getSelectedFile();
                if (!savedFile.getAbsolutePath().toLowerCase().endsWith(".dat")) {
                    savedFile = new File(savedFile.getAbsolutePath() + ".dat");
                }

                saveButtonPressed(savedFile);
            }
        });

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String extension = "";
                boolean correctFileExtension = false;

                while (!correctFileExtension) {
                    fileChooser.showOpenDialog(null);

                    int i = fileChooser.getSelectedFile().getPath().lastIndexOf('.');
                    if (i > 0) {
                        extension = fileChooser.getSelectedFile().getPath().substring(i+1);
                    }

                    if (extension.equals("dat")) {
                        correctFileExtension = true;
                    } else {
                        extension = "";
                        warningMessage("Please select a .dat file!");
                    }
                }

                loadButtonPressed(fileChooser.getSelectedFile());
            }
        });

        dialogMenuContainer.add(infoButton, BorderLayout.SOUTH);
        dialogMenuContainer.add(saveButton, BorderLayout.NORTH);
        dialogMenuContainer.add(loadButton, BorderLayout.CENTER);

        JCheckBox displayConsoleCheckBox = new JCheckBox("Toggle console display");
        displayConsoleCheckBox.setBackground(backgroundColor);
        displayConsoleCheckBox.setRequestFocusEnabled(false);
        displayConsoleCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consoleGameView();
            }
        });

        dialogMenuContainer.add(displayConsoleCheckBox);

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogMenu.setLocationRelativeTo(frame);
                dialogMenu.setVisible(true);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(menuButton);
        bottomPanel.add(levelSelectorMessage);
        bottomPanel.add(levelSelector);
        bottomPanel.setBackground(backgroundColor);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        dialogWin = new JDialog(frame, "Congratulations!", Dialog.ModalityType.DOCUMENT_MODAL);
        dialogWin.setBounds(132, 132, 350, 100);

        Container dialogContainer = dialogWin.getContentPane();
        dialogContainer.setLayout(new FlowLayout());
        dialogContainer.setBackground(backgroundColor);
        dialogContainer.add(new JLabel("Congratulations!"));

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new FlowLayout());
        dialogPanel.setBackground(backgroundColor);

        JButton nextButton = new JButton("Next Level");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nextButtonPressed()) {
                    dialogWin.dispose();
                }
            }
        });

        JButton previousButton = new JButton("Previous Level");
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (previousButtonPressed()) {
                    dialogWin.dispose();
                }
            }
        });

        JButton dialogResetButton = new JButton("Reset");
        dialogResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetPressed();
                dialogWin.dispose();
            }
        });

        dialogPanel.add(dialogResetButton);
        dialogPanel.add(previousButton);
        dialogPanel.add(nextButton);

        dialogContainer.add(dialogPanel, BorderLayout.SOUTH);

        frame.setTitle("Sokoban");

        Image image = ImageIO.read(models[0].getFiles()[2]);
        frame.setIconImage(image);

        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("Panel.background", backgroundColor);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Redraws the view and performs necessary updates.
     *
     * @param levelComponent the main component representing the level
     */
    public void redraw(JComponent levelComponent) {

        if (currentModel.getDisplayToConsole()) {
            GameGrid consoleViewMap = currentModel.getMap().clone();
            GameGrid consoleViewInteractive = currentModel.getInteractive().clone();
            GameGrid consoleView = consoleViewMap;
            for (int i = 0; i < consoleView.getGrid().length; i++) {
                for (int j = 0; j < consoleView.getGrid()[0].length; j++) {
                    if (consoleViewInteractive.getValue(i, j) != null) {
                        consoleView.setValue(i, j, consoleViewInteractive.getValue(i, j));
                    }
                }
            }

            System.out.println(consoleView);
        }

        levelComponent.repaint();
        if (currentModel.getStuck()) {
            warningMessage("Looks like you're stuck!\n"
                    + "The game will restart.");
            currentModel.setStuck(false);
            resetPressed();
        }

        if (currentModel.getWin()) {
            if (currentModel.getDisplayToConsole()) {
                consoleGameControllerWin();
            } else {
                dialogWin.setLocationRelativeTo(frame);
                dialogWin.setVisible(true);
            }
        }
    }

    /**
     * Updates the center component of the view with a new data model.
     *
     * @param dataModel the new data model
     * @throws IOException if an I/O error occurs while loading the new component
     */
    public void newCenterComponent(Model dataModel) throws IOException {
        frame.remove(centerComponent);
        this.centerComponent = centerComponent(dataModel);
        frame.add(centerComponent, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    /**
     * Sets the current data model and updates the current level label.
     *
     * @param dataModel the current data model
     */
    public void setModelView(Model dataModel) {
        this.currentModel = dataModel;
        updateCurrentLvl();
    }

    /**
     * Updates the current level of the game based on the player's progress.
     * This function should be called whenever the player completes a level.
     * It increments the current level by one and updates the UI to reflect the new level.
     */
    private void updateCurrentLvl() {
        currentLevel.setText("Level " + (currentModel.getLevel() + 1));
    }

    /**
     * Displays a warning message to the user.
     *
     * @param message the warning message to be displayed
     */
    public void warningMessage(String message) {

        if (currentModel.getDisplayToConsole()) {
            System.out.println(message + "\n");
        } else {
            JOptionPane.showMessageDialog(frame, message, "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Toggles between console-based and graphical view of the game.
     */
    public void consoleGameView() {

        for (Model m : models) {
            m.setDisplayToConsole(!m.getDisplayToConsole());
        }

        if (!currentModel.getDisplayToConsole()) {
            frame.setVisible(true);
        } else {
            frame.setVisible(false);
            consoleGameController();
        }

        for (Model m : models) {
            m.setDisplayToConsole(!m.getDisplayToConsole());
        }

        frame.setVisible(true);
    }

    /**
     * Writes messages to the console.
     *
     * @param message the message to be written
     */
    public void printToConsole(String message) {
        System.out.println(message);
    }

    /**
     * Displays a information message to the user.
     *
     * @param message the information message to be displayed
     */
    public void infoMessage(String message) {

        if (currentModel.getDisplayToConsole()) {
            System.out.println(message + "\n");
        } else {
            JOptionPane.showMessageDialog(frame, message, "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}