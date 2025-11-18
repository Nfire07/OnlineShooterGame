import java.awt.Image;
import java.awt.Rectangle;

public class Tile extends GameObject {
    
    public Tile(String ObjectName, Rectangle[] hitbox, Image[] sprite, float x, float y) {
        super(ObjectName, hitbox, null, x, y);
    }
    
    @Override
    public void UpdatePosition(float deltaTime) {
        this.hitbox = this.updateHitbox();
    }
    
    public boolean checkForIntersection(Rectangle hitbox) {
    	return this.hitbox[0].intersects(hitbox);
    }
    
    public void repulse(Player player) {
        if (this.hitbox == null || this.hitbox.length == 0) {
            return;
        }

        Rectangle tileRect = this.hitbox[0];
        Rectangle playerRect = player.hitbox[0];

        if (!tileRect.intersects(playerRect)) {
            return;
        }

        float overlapLeft = (playerRect.x + playerRect.width) - tileRect.x;
        float overlapRight = (tileRect.x + tileRect.width) - playerRect.x;
        float overlapTop = (playerRect.y + playerRect.height) - tileRect.y;
        float overlapBottom = (tileRect.y + tileRect.height) - playerRect.y;

        float minOverlap = Math.min(
            Math.min(overlapLeft, overlapRight),
            Math.min(overlapTop, overlapBottom)
        );

        if (minOverlap == overlapLeft) {
            player.x = player.x - overlapLeft;
            if (player.currentVelocityX > 0) player.currentVelocityX = 0;
        } else if (minOverlap == overlapRight) {
            player.x = player.x + overlapRight;
            if (player.currentVelocityX < 0) player.currentVelocityX = 0;
        } else if (minOverlap == overlapTop) {
            player.y = player.y - overlapTop;
            if (player.currentVelocityY > 0) player.currentVelocityY = 0;
        } else if (minOverlap == overlapBottom) {
            player.y = player.y + overlapBottom;
            if (player.currentVelocityY < 0) player.currentVelocityY = 0;
        }
        
        player.hitbox = player.updateHitbox();
    }
}