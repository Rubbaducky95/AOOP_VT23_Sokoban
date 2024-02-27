/**
 * The {@code SoundObserver} interface defines the contract for an observer that receives notifications
 * about key presses.
 */
public interface SoundObserver {

    /**
     * Notifies the observer that a key has been pressed.
     *
     * @param key
     *      The key that has been pressed
     */
    void KeyPress(String key);
}