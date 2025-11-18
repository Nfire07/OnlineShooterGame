import java.awt.*;

public abstract class GameObject {
    public String ObjectName;
    public Rectangle[] hitbox;
    public Rectangle[] initialHitboxOffset;
    public Image[] sprite;
    public static boolean debugMode = false;
    public int currentSprite = 0;
    public float x, y;
    public boolean isVisible=false;

    public GameObject(String ObjectName, Rectangle[] hitbox, Image[] sprite, float x, float y) {
        this.ObjectName = ObjectName;
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        
        this.initialHitboxOffset = new Rectangle[hitbox.length];
        for(int i = 0; i < hitbox.length; i++) {
            initialHitboxOffset[i] = new Rectangle(
                hitbox[i].x - (int)x,
                hitbox[i].y - (int)y,
                hitbox[i].width,
                hitbox[i].height
            );
        }
        
        this.hitbox = hitbox;
    }

    public static void setDebugMode(boolean debugMode) {
        GameObject.debugMode = debugMode;
    }

    public Rectangle[] updateHitbox() {
        Rectangle[] newHitbox = new Rectangle[hitbox.length];
        
        for(int i = 0; i < newHitbox.length; i++) {
            newHitbox[i] = new Rectangle(
                (int)x + initialHitboxOffset[i].x,
                (int)y + initialHitboxOffset[i].y,
                initialHitboxOffset[i].width,
                initialHitboxOffset[i].height
            );
        }
        
        return newHitbox;
    }

    public void updateSprite() {
        currentSprite = (currentSprite + 1) % sprite.length;
    }

    public boolean checkCollisionWith(GameObject g) {
        for(int i = 0; i < hitbox.length; i++) {
            for(int j = 0; j < g.hitbox.length; j++) {
                if(hitbox[i].intersects(g.hitbox[j]))
                    return true;
            }
        }
        return false;
    }

    public void drawHitbox(Graphics g, Color c) {
        if(debugMode == true) {
            g.setColor(c);
            for(int i = 0; i < hitbox.length; i++) {
                g.drawRect(hitbox[i].x, hitbox[i].y, hitbox[i].width, hitbox[i].height);
            }
        }
    }

    public void drawSprite(Graphics g) {
        g.drawImage(sprite[currentSprite], (int)x, (int)y, null);
    }

    public String toString() {
        if(debugMode == true) {
            String s = ObjectName + " ";
            s += "XY: (" + x + ";" + y + ")\nHITBOX COUNT: " + hitbox.length + "\n";

            for(int i = 0; i < hitbox.length; i++) {
                s += "[" + (i+1) + "]: " + hitbox[i].toString() + "\n";
            }

            s += "SPRITE COUNT: " + sprite.length + "\n";

            for(int i = 0; i < sprite.length; i++) {
                s += "Image[" + (i+1) + "]: " + "width=" + sprite[i].getWidth(null) + 
                     ",height=" + sprite[i].getHeight(null) + "\n";
            }

            s += "\n";
            return s;
        }
        return "";
    }
    
    public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
    
    public boolean isVisible() {
		return isVisible;
	}

    public abstract void UpdatePosition(float deltaTime);

}