import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player extends GameObject {
    float speed = 300.0f; 
    float currentVelocityX = 0;
    float currentVelocityY = 0;
    float acceleration = 2000.0f; 
    float deceleration = 1500.0f;
    int hp = 100;
    int hpBarWidth = 100;
    int hpBarHeight = 20;
    
    double fov = 30;
    int rayLength = 300;
    int ammo = 3;
    float fireRate = 0.1f; 
    float timeSinceLastShot = 0;
    
    public Point leftRayEnd;
    public Point rightRayEnd;
    public double angleToMouse;
    
    ArrayList<Bullet> bullets;
    InputManager inputManager;
        
    public Player(String ObjectName, Rectangle[] hitbox, Image[] sprite, float x, float y, InputManager inputManager) {
        super(ObjectName, hitbox, sprite, x, y);
        bullets = new ArrayList<Bullet>();
        this.inputManager = inputManager;
    }
    
    public int getCenterX() {
        return (int)x + sprite[currentSprite].getWidth(null) / 2;
    }
    
    public int getCenterY() {
        return (int)y + sprite[currentSprite].getHeight(null) / 2;
    }

    public void shootBullet(Point p) {
        if(ammo - bullets.size() > 0 && timeSinceLastShot >= fireRate) {
            bullets.add(new Bullet(new Rectangle(getCenterX(), getCenterY(), 20, 5), p, Color.decode("#4bbf8f")));
            GameAudio.playSound(0);
            timeSinceLastShot = 0;
        }
    }

    public void drawRay(Graphics2D g2d, Point mouse) {
        double dx = mouse.x - this.getCenterX();
        double dy = mouse.y - this.getCenterY();
        angleToMouse = Math.atan2(dy, dx);
        double fovRadians = Math.toRadians(fov);
        double leftAngle = angleToMouse - fovRadians / 2;
        double rightAngle = angleToMouse + fovRadians / 2;
        
        leftRayEnd = new Point(
            this.getCenterX() + (int)(Math.cos(leftAngle) * rayLength),
            this.getCenterY() + (int)(Math.sin(leftAngle) * rayLength)
        );
        
        rightRayEnd = new Point(
            this.getCenterX() + (int)(Math.cos(rightAngle) * rayLength),
            this.getCenterY() + (int)(Math.sin(rightAngle) * rayLength)
        );
        
        g2d.setColor(Color.WHITE);
        g2d.drawLine(getCenterX(), getCenterY(), leftRayEnd.x, leftRayEnd.y);
        g2d.drawLine(getCenterX(), getCenterY(), rightRayEnd.x, rightRayEnd.y);
        
        int[] xPoints = { getCenterX(), leftRayEnd.x, rightRayEnd.x };
        int[] yPoints = { getCenterY(), leftRayEnd.y, rightRayEnd.y };
        g2d.setColor(new Color(255, 255, 0, 50));
        g2d.fillPolygon(xPoints, yPoints, 3);
    }
    
    public Polygon getFOVTriangle() {
        int[] xPoints = { getCenterX(), leftRayEnd.x, rightRayEnd.x };
        int[] yPoints = { getCenterY(), leftRayEnd.y, rightRayEnd.y };
        return new Polygon(xPoints, yPoints, 3);
    }
    
    public boolean triangleIntersectsRectangle(Polygon triangle, Rectangle rect) {
        for (int i = 0; i < 3; i++) {
            if (rect.contains(triangle.xpoints[i], triangle.ypoints[i])) {
                return true;
            }
        }
        
        Point[] rectPoints = {
            new Point(rect.x, rect.y),
            new Point(rect.x + rect.width, rect.y),
            new Point(rect.x, rect.y + rect.height),
            new Point(rect.x + rect.width, rect.y + rect.height)
        };
        
        for (Point p : rectPoints) {
            if (triangle.contains(p)) {
                return true;
            }
        }
        
        for (int i = 0; i < 3; i++) {
            int next = (i + 1) % 3;
            Line2D triEdge = new Line2D.Float(
                triangle.xpoints[i], triangle.ypoints[i],
                triangle.xpoints[next], triangle.ypoints[next]
            );
            if (triEdge.intersects(rect)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isPointBlockedByTile(Point point, List<GameObject> gameObjects) {
        Line2D rayLine = new Line2D.Float(getCenterX(), getCenterY(), point.x, point.y);
        
        for(GameObject obj : gameObjects) {
            if(obj instanceof Tile) {
                Tile tile = (Tile)obj;
                if(tile.hitbox.length > 0 && rayLine.intersects(tile.hitbox[0])) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public double getDistanceToPoint(Point p) {
        int dx = getCenterX() - p.x;
        int dy = getCenterY() - p.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public void drawHP(Graphics2D g2d) {
        int currentBarWidth = (int)((hp / 100.0f) * hpBarWidth);
        int barRelativePos = 30;
        
        g2d.setColor(Color.RED);
        g2d.fillRect(getCenterX() - hpBarWidth/2, getCenterY() + barRelativePos, hpBarWidth, hpBarHeight);
        
        g2d.setColor(Color.GREEN);
        g2d.fillRect(getCenterX() - hpBarWidth/2, getCenterY() + barRelativePos, currentBarWidth, hpBarHeight);
        
        g2d.setColor(Color.WHITE);
        g2d.drawRect(getCenterX() - hpBarWidth/2, getCenterY() + barRelativePos, hpBarWidth, hpBarHeight);
    }
    
    public void checkRayIntersections(List<GameObject> gameObjects) {
        if(leftRayEnd == null || rightRayEnd == null) return;
        
        Polygon fovTriangle = getFOVTriangle();
        
        for(GameObject obj : gameObjects) {
            if(obj instanceof Enemy) {
                Enemy enemy = (Enemy)obj;
                enemy.setVisible(false);
                
                if (triangleIntersectsRectangle(fovTriangle, enemy.hitbox[0])) {
                    Point enemyCenter = new Point(
                        enemy.hitbox[0].x + enemy.hitbox[0].width / 2,
                        enemy.hitbox[0].y + enemy.hitbox[0].height / 2
                    );
                    
                    if (!isPointBlockedByTile(enemyCenter, gameObjects)) {
                        enemy.setVisible(true);
                    }
                }
            }
        }
    }

    @Override
    public void UpdatePosition(float deltaTime) {
        this.hitbox = updateHitbox();
        Main.pw.println((int)this.x + ";" + (int)this.y);
        Main.pw.println((int)this.hp);
        float inputX = 0;
        float inputY = 0;
        
        if(inputManager.isKeyDown(java.awt.event.KeyEvent.VK_W)) {
            inputY -= 1;
        }
        if(inputManager.isKeyDown(java.awt.event.KeyEvent.VK_S)) {
            inputY += 1;
        }
        if(inputManager.isKeyDown(java.awt.event.KeyEvent.VK_A)) {
            inputX -= 1;
        }
        if(inputManager.isKeyDown(java.awt.event.KeyEvent.VK_D)) {
            inputX += 1;
        }
        
        float inputMagnitude = (float)Math.sqrt(inputX * inputX + inputY * inputY);
        if(inputMagnitude > 0) {
            inputX /= inputMagnitude;
            inputY /= inputMagnitude;
        }
        
        if(inputMagnitude > 0) {
            currentVelocityX += inputX * acceleration * deltaTime;
            currentVelocityY += inputY * acceleration * deltaTime;
            
            float currentSpeed = (float)Math.sqrt(currentVelocityX * currentVelocityX + 
                                                  currentVelocityY * currentVelocityY);
            if(currentSpeed > speed) {
                currentVelocityX = (currentVelocityX / currentSpeed) * speed;
                currentVelocityY = (currentVelocityY / currentSpeed) * speed;
            }
        } else {
            float currentSpeed = (float)Math.sqrt(currentVelocityX * currentVelocityX + 
                                                  currentVelocityY * currentVelocityY);
            
            if(currentSpeed > 0) {
                float decelerationAmount = deceleration * deltaTime;
                if(decelerationAmount > currentSpeed) {
                    currentVelocityX = 0;
                    currentVelocityY = 0;
                } else {
                    float factor = (currentSpeed - decelerationAmount) / currentSpeed;
                    currentVelocityX *= factor;
                    currentVelocityY *= factor;
                }
            }
        }
        
        timeSinceLastShot += deltaTime;
        
        x += currentVelocityX * deltaTime;
        y += currentVelocityY * deltaTime;
        
        bullets.removeIf(bullet -> {
            bullet.updateBullet(deltaTime);
            return bullet.bulletExploded();
        });
    }
    
    public void checkForHit(GameObject o) {
        if(o instanceof Enemy) {
            Enemy enemy = (Enemy)o;
            ArrayList<Bullet> hittingBullets = new ArrayList<>();
            
            for(Bullet b : bullets) {
                if(enemy.checkForBulletPenetration(b.hitbox) && enemy.hp > 0) {
                    enemy.hp -= b.damage;
                    if(GameObject.debugMode)
                    	System.out.println("Enemy HP: " + enemy.hp);
                    b.setHitted(true);
                }
                if(b.hasHitted()) {
                    hittingBullets.add(b);
                }
            }
            bullets.removeAll(hittingBullets);
        } else if(o instanceof Tile) {
            Tile tile = (Tile)o;
            ArrayList<Bullet> hittingBullets = new ArrayList<>();
            
            for(Bullet b : bullets) {
                if(tile.checkForIntersection(b.hitbox)) {
                    b.setHitted(true);
                }
                if(b.hasHitted()) {
                    hittingBullets.add(b);
                }
            }
            bullets.removeAll(hittingBullets);
        }
    }
}