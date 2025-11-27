import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;

public class Enemy extends GameObject {
    int hp = 100;
    int hpBarWidth = 100;
    int hpBarHeight = 20;
    public static ArrayList<Bullet> bullets = new ArrayList<>(); 
    

    public Enemy(String ObjectName, Rectangle[] hitbox, Image[] sprite, int x, int y) {
        super(ObjectName, hitbox, sprite, x, y);
    }

    @Override
    public void UpdatePosition(float deltaTime) {
        Point enemyPosition = getEnemyPosition();
        if (enemyPosition != null) {
            this.x = enemyPosition.x;
            this.y = enemyPosition.y;
        }
        String readed;
        bullets.removeAll(bullets);
        try {
			while(!(readed = Main.br.readLine()).isEmpty()) {
				bullets.add(new Bullet(readed, Color.decode("#bf4b8f")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        this.updateHp();
        this.hitbox = this.updateHitbox();
        bullets.removeIf(bullet -> {
            bullet.updateBullet(deltaTime);
            if(bullet.hasHitted()) {
            	this.hp -= Bullet.damage;
            }
            return bullet.bulletExploded();
        });
    }

    private Point getEnemyPosition() {
        Point enemyPosition = null;
        try {
            String line = Main.br.readLine();
            if (line != null && !line.isEmpty()) {
                String[] pos = line.split(";");
                if (pos.length == 2) {
                    enemyPosition = new Point(
                        Integer.parseInt(pos[0].trim()),
                        Integer.parseInt(pos[1].trim())
                    );
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading enemy position: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing enemy position: " + e.getMessage());
        }
        return enemyPosition;
    }
    
    private void updateHp() {
        try {
            String hpLine = Main.br.readLine();
            if (hpLine != null && !hpLine.isEmpty()) {
                this.hp = Integer.parseInt(hpLine.trim());
                if (this.hp < 0) this.hp = 0;
                if (this.hp > 100) this.hp = 100;
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing HP: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading HP: " + e.getMessage());
        }
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