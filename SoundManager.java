import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code SoundManager} class is responsible for managing and playing sounds in the application.
 * It allows adding sounds with associated keys and provides a method to play a sound based on its key.
 * This class implements the {@link SoundObserver} interface to receive key press events and play the
 * corresponding sound associated with the pressed key.
 */
public class SoundManager implements SoundObserver{
    private Map<String, String> soundPaths;

    /**
     * Constructs a SoundManager object.
     */
    public SoundManager(){
        soundPaths = new HashMap<>();
    }

    /**
     * Adds a sound to the SoundManager with the specified key and sound file path.
     *
     * @param key
     *      The key associated with the sound
     * @param soundFilePath
     *      The file path of the sound file
     */
    public void addSound(String key, String soundFilePath){
        soundPaths.put(key, soundFilePath);
    }

    @Override
    public void KeyPress(String key) {
        String soundFilePath = soundPaths.get(key);
        if(soundFilePath != null){
            playSound(soundFilePath);
        }
    }

    /**
     * Plays the sound specified by the sound file path.
     *
     * @param soundFilePath
     *      The file path of the sound to be played
     */
    public void playSound(String soundFilePath) {
        try {
            // Load the sound file from the file system
            File soundFile = new File(soundFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            // Create a Clip to play the sound
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            // Handle any exceptions that occur while loading the sound file
            e.printStackTrace();
        }
    }

}