import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class Bullet {
    Rectangle hitbox;
    float duration = 1.5f; 
    float bulletSpeed = 500;
    public static int damage = 10;
    double angle;
    Point target;
    boolean hitted = false;
    Color bulletColor;
    public int WIDTH = 20;
    public int HEIGHT = 5;
    
    public Bullet(Rectangle hitbox, Point target,Color bulletColor) {
        this.hitbox = hitbox;
        this.target = target;
        this.bulletColor = bulletColor;
        double dx = target.x - hitbox.x;
        double dy = target.y - hitbox.y;
        this.angle = Math.atan2(dy, dx);
    }
    
    public Bullet(String desc,Color bulletColor) {
    	String[] splitted = desc.split(",");
    	int x = Integer.parseInt(splitted[0]);
    	int y = Integer.parseInt(splitted[1]);
    	int px = Integer.parseInt(splitted[2]);
    	int py = Integer.parseInt(splitted[3]);
    	this.hitted = Boolean.parseBoolean(splitted[4]);

    	this.hitbox = new Rectangle(x,y,WIDTH,HEIGHT);
        this.target = new Point(px,py);
        this.bulletColor = bulletColor;
        double dx = target.x - hitbox.x;
        double dy = target.y - hitbox.y;
        this.angle = Math.atan2(dy, dx);
    }
    
    boolean hasHitted() {
    	return hitted;
    }
    
    void setHitted(boolean hitted) {
    	this.hitted=hitted;
    }
    
    public Point getTarget() {
		return target;
	}
    
    
    public void draw(Graphics2D g2d) {
        AffineTransform old = g2d.getTransform();
        g2d.translate(hitbox.x, hitbox.y);
        g2d.rotate(angle);
        g2d.setColor(bulletColor);
        g2d.fillRect(-hitbox.width/2, -hitbox.height/2, hitbox.width, hitbox.height);
        g2d.setTransform(old);
    }
    
    public void updateBullet(float deltaTime) {
        hitbox.x += (int)(Math.cos(angle) * bulletSpeed * deltaTime);
        hitbox.y += (int)(Math.sin(angle) * bulletSpeed * deltaTime);
        
        duration -= deltaTime;
    }
    
    public boolean bulletExploded() {
        return duration <= 0;
    }
    
    public String toString() {
    	return this.hitbox.x + "," + this.hitbox.y + "," + this.getTarget().x + "," +this.getTarget().y + "," + this.hasHitted();
    }
}
