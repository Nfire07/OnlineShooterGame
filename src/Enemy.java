import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Enemy extends GameObject {
    int hp = 100;
    int hpBarWidth = 100;
    int hpBarHeight = 20;
    public static ArrayList<Bullet> bullets = new ArrayList<>(); 
    
    private Point pendingPosition = null;
    private Integer pendingHp = null;
    private ArrayList<Bullet> pendingBullets = new ArrayList<>();

    public Enemy(String ObjectName, Rectangle[] hitbox, Image[] sprite, int x, int y) {
        super(ObjectName, hitbox, sprite, x, y);
    }

    @Override
    public void UpdatePosition(float deltaTime) {
        if (pendingPosition != null) {
            this.x = pendingPosition.x - sprite[currentSprite].getWidth(null) / 2;
            this.y = pendingPosition.y - sprite[currentSprite].getHeight(null) / 2;
            pendingPosition = null;
        }
        
        if (pendingHp != null) {
            this.hp = pendingHp;
            if (this.hp < 0) this.hp = 0;
            if (this.hp > 100) this.hp = 100;
            pendingHp = null;
        }
        
        if (!pendingBullets.isEmpty()) {
            bullets.clear();
            bullets.addAll(pendingBullets);
            pendingBullets.clear();
        }
        
        this.hitbox = this.updateHitbox();
        
        bullets.removeIf(bullet -> {
            bullet.updateBullet(deltaTime);
            return bullet.bulletExploded();
        });
    }
    
    public void updateEnemyPosition(Point position) {
        this.pendingPosition = position;
    }
    
    public void updateEnemyHp(int hp) {
        this.pendingHp = hp;
    }
    
    public void updateEnemyBullets(ArrayList<Bullet> newBullets) {
        this.pendingBullets.clear();
        this.pendingBullets.addAll(newBullets);
    }

    public int getCenterX() {
        return (int) x + sprite[currentSprite].getWidth(null) / 2;
    }

    public int getCenterY() {
        return (int) y + sprite[currentSprite].getHeight(null) / 2;
    }

    public void drawHP(Graphics2D g2d) {
        int currentBarWidth = (int) ((hp / 100.0f) * hpBarWidth);
        int barRelativePos = 30;

        g2d.setColor(Color.RED);
        g2d.fillRect(getCenterX() - hpBarWidth / 2, getCenterY() + barRelativePos, hpBarWidth, hpBarHeight);

        g2d.setColor(Color.GREEN);
        g2d.fillRect(getCenterX() - hpBarWidth / 2, getCenterY() + barRelativePos, currentBarWidth, hpBarHeight);

        g2d.setColor(Color.WHITE);
        g2d.drawRect(getCenterX() - hpBarWidth / 2, getCenterY() + barRelativePos, hpBarWidth, hpBarHeight);
    }
    
    public boolean checkForBulletPenetration(Rectangle bulletHitbox) {
        return hitbox[0].intersects(bulletHitbox);
    }
}