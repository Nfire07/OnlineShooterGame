import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameAudio {
    public static ArrayList<Sound> soundList = new ArrayList<Sound>();
    private static ExecutorService audioExecutor = Executors.newFixedThreadPool(4);
    private static final int MAX_POOL_SIZE = 8;
    private static ArrayList<ClipPool> clipPools = new ArrayList<>();
    public static boolean AUDIO_DISABLED = true;
    
    static class ClipPool {
        private ArrayList<Clip> availableClips = new ArrayList<>();
        private Sound sound;
        private int maxSize;
        
        public ClipPool(Sound sound, int maxSize) {
            this.sound = sound;
            this.maxSize = maxSize;
        }
        
        public synchronized Clip getClip() {
            if(!availableClips.isEmpty()) {
                return availableClips.remove(availableClips.size() - 1);
            }
            
            try {
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(sound.soundFile));
                return clip;
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                System.err.println("Errore nel creare clip audio: " + e.getMessage());
                return null;
            }
        }
        
        public synchronized void returnClip(Clip clip) {
            if(clip != null && availableClips.size() < maxSize) {
                clip.setFramePosition(0);
                availableClips.add(clip);
            } else if(clip != null) {
                clip.close();
            }
        }
        
        public synchronized void cleanup() {
            for(Clip clip : availableClips) {
                clip.close();
            }
            availableClips.clear();
        }
    }
    
    public static void addSound(Sound s) {
        if(s != null) {
            soundList.add(s);
            clipPools.add(new ClipPool(s, MAX_POOL_SIZE));
        }
    }
    
    public static void playSound(int index) {
    	if(AUDIO_DISABLED)
    		return;
        if(index < 0 || index >= clipPools.size()) { 
            System.err.println("Indice suono non valido: " + index);
            return;
        }
        
        audioExecutor.submit(() -> {
            ClipPool pool = clipPools.get(index);
            Clip clip = pool.getClip();
            
            if(clip != null) {
                clip.addLineListener(event -> {
                    if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                        pool.returnClip(clip);
                    }
                });
                
                clip.start();
            }
        });
    }
    
    public static void shutdown() {
        audioExecutor.shutdown();
        try {
            if(!audioExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                audioExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            audioExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        for(ClipPool pool : clipPools) {
            pool.cleanup();
        }
    }
}