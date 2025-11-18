import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class GameGraphics extends JPanel{
	public ArrayList<GameObject> gameObjects;
	public Point mousePosition;
	public GameMap gameMap;
	
	public GameGraphics(ArrayList<GameObject> gameObjects, GameMap gameMap) {
		this.gameObjects = gameObjects;
		this.gameMap = gameMap;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		gameMap.draw(g2d);

		gameObjects.forEach(o -> {
			//o.drawHitbox(g2d,Color.GREEN);
			if(o.isVisible()) {
				o.drawSprite(g2d);
				try {
					((Enemy)o).drawHP(g2d);
				}catch(ClassCastException e) {}
			}
		});
		
		Player p = (Player)gameObjects.get(0);
		
		
		if(mousePosition!=null) {
			p.drawRay(g2d, mousePosition);
		}
		p.drawHP(g2d);
		if(!p.bullets.isEmpty()) {
			p.bullets.forEach(o -> o.draw(g2d));
		}
		
		
		
	}

}
