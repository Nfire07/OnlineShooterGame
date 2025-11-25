import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

public class Enemy extends GameObject {
	int hp = 100;
	int hpBarWidth = 100;
	int hpBarHeight = 20;

	public Enemy(String ObjectName, Rectangle[] hitbox, Image[] sprite, int x, int y) {
		super(ObjectName, hitbox, sprite, x, y);
	}

	@Override
	public void UpdatePosition(float deltaTime) {
		Point enemyPosition = getEnemyPosition();
		this.x = enemyPosition.x;
		this.y = enemyPosition.y;
		this.updateHp();
		this.hitbox = this.updateHitbox();
	}

	private Point getEnemyPosition() {
		Point enemyPosition = null;
		try {
			String[] pos = Main.br.readLine().split(";");
			enemyPosition = new Point(Integer.parseInt(pos[0]),Integer.parseInt(pos[1]));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return enemyPosition;
	}
	
	private void updateHp() {
		try {
			this.hp = Integer.parseInt(Main.br.readLine());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
