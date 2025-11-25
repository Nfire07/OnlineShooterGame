import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Window extends JFrame {
    public static final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    public GameGraphics g;
    public ArrayList<GameObject> gameObjects;
    public ArrayList<Image> tempImage;
    public ArrayList<Rectangle> tempHitbox;
    public InputManager inputManager;
    int entitySize = 50;
    
    public Window() {
        gameObjects = new ArrayList<GameObject>();
        tempImage = new ArrayList<Image>();
        tempHitbox = new ArrayList<Rectangle>();
        inputManager = new InputManager();
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(screen);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        
        GameAudio.addSound(new Sound("./assets/shoot.wav"));
        
        // Player
        tempImage.add(Main.imageLoader("./assets/Player.png").getScaledInstance(entitySize, entitySize, Image.SCALE_FAST));
        tempHitbox.add(new Rectangle(0, 0, entitySize, entitySize)); 

        gameObjects.add(new Player("Player",
            (Rectangle[]) tempHitbox.toArray(new Rectangle[0]),
            (Image[]) tempImage.toArray(new Image[0]),
            0, 0, inputManager));  

        tempImage.clear();
        tempHitbox.clear();

        // Enemy
        tempImage.add(Main.imageLoader("./assets/Enemy.png").getScaledInstance(entitySize, entitySize, Image.SCALE_FAST));
        tempHitbox.add(new Rectangle(0, 0, entitySize, entitySize)); 

        gameObjects.add(new Enemy("Enemy",
            (Rectangle[]) tempHitbox.toArray(new Rectangle[0]),
            (Image[]) tempImage.toArray(new Image[0]),
            0, 0));  

        tempImage.clear();
        tempHitbox.clear();

        GameMap gameMap = new GameMap(Main.GAME_MAP, screen.width, screen.height);
        gameObjects.addAll(gameMap.generateTiles());

        int[] playerPos = new int[] {Main.startingPos.x , Main.startingPos.y};
        if (playerPos != null) {
            gameObjects.get(0).x = playerPos[0] - 10;  
            gameObjects.get(0).y = playerPos[1] - 10;
            gameObjects.get(0).hitbox = gameObjects.get(0).updateHitbox();
            gameObjects.get(0).setVisible(true);
        }

        int[] enemyPos = new int[] {-10000 , -10000};
        if (enemyPos != null) {
            gameObjects.get(1).x = enemyPos[0] - 10;  
            gameObjects.get(1).y = enemyPos[1] - 10;
            gameObjects.get(1).hitbox = gameObjects.get(1).updateHitbox();
            gameObjects.get(1).setVisible(false);  
        }
        
        tempImage.clear();
        tempHitbox.clear();
        
        this.g = new GameGraphics(gameObjects,gameMap);
        g.setLocation(0, 0);
        g.setSize(screen);
        g.setBackground(Color.DARK_GRAY);
        
        WindowKeys keys = new WindowKeys(this, gameObjects, inputManager);
        this.addKeyListener(keys);
        g.addMouseListener(keys);
        g.addMouseMotionListener(keys);
        
        this.add(g);
        this.setVisible(true);
    }
}