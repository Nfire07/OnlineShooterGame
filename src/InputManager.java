import java.util.HashSet;
import java.util.Set;

public class InputManager {
    private Set<Integer> pressedKeys;
    private Set<Integer> justPressedKeys;
    private Set<Integer> justReleasedKeys;
    
    public InputManager() {
        pressedKeys = new HashSet<>();
        justPressedKeys = new HashSet<>();
        justReleasedKeys = new HashSet<>();
    }
    
    public void keyPressed(int keyCode) {
        if (!pressedKeys.contains(keyCode)) {
            justPressedKeys.add(keyCode);
        }
        pressedKeys.add(keyCode);
    }
    
    public void keyReleased(int keyCode) {
        pressedKeys.remove(keyCode);
        justReleasedKeys.add(keyCode);
    }
    
    public void update() {
        justPressedKeys.clear();
        justReleasedKeys.clear();
    }
    
    public boolean isKeyDown(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
    
    public boolean isKeyJustPressed(int keyCode) {
        return justPressedKeys.contains(keyCode);
    }
    
    public boolean isKeyJustReleased(int keyCode) {
        return justReleasedKeys.contains(keyCode);
    }
    
    public void clear() {
        pressedKeys.clear();
        justPressedKeys.clear();
        justReleasedKeys.clear();
    }
}