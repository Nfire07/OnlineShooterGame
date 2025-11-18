public class GameTimer {
    private long lastTime;
    private float deltaTime;
    private int fps;
    private int frameCount;
    private long fpsTimer;
    
    public GameTimer() {
        this.lastTime = System.nanoTime();
        this.deltaTime = 0;
        this.fps = 0;
        this.frameCount = 0;
        this.fpsTimer = System.currentTimeMillis();
    }
    
    public float update() {
        long currentTime = System.nanoTime();
        deltaTime = (currentTime - lastTime) / 1_000_000_000.0f;
        lastTime = currentTime;
        
        // Calcola FPS
        frameCount++;
        if (System.currentTimeMillis() - fpsTimer > 1000) {
            fps = frameCount;
            frameCount = 0;
            fpsTimer = System.currentTimeMillis();
        }
        
        if (deltaTime > 0.1f) {
            deltaTime = 0.1f;
        }
        
        return deltaTime;
    }
    
    public float getDeltaTime() {
        return deltaTime;
    }
    
    public float getDeltaTimeMs() {
        return deltaTime * 1000;
    }
    
    public int getFPS() {
        return fps;
    }
}