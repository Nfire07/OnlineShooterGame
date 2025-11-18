import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameAudio {
    public static ArrayList<Sound> soundList = new ArrayList<Sound>();
    private static ExecutorService audioExecutor = Executors.newFixedThreadPool(4); 
    
    public static void addSound(Sound s) {
        if(s != null) {
            soundList.add(s);
        }
    }
    
    public static void playSound(int index) {
        audioExecutor.submit(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(soundList.get(index).soundFile));
                
                clip.addLineListener(event -> {
                    if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
                
                clip.start();
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            }
        });
    }
    
    public static void shutdown() {
        audioExecutor.shutdown();
    }
}