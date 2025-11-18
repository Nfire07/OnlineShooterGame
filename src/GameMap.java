import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.util.ArrayList;

public class GameMap {
	ArrayList<Integer[][]> gameMaps = new ArrayList<>();
	private int tileSize;
	int mapIndex;
	private int screenWidth;
	private int screenHeight;

	public GameMap(int mapIndex, int screenWidth, int screenHeight) {
		this.mapIndex = mapIndex;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		initializeMaps();

		int tileSizeByWidth = screenWidth / 16;
		int tileSizeByHeight = screenHeight / 13;
		this.tileSize = Math.min(tileSizeByWidth, tileSizeByHeight);
	}

	private void initializeMaps() {
		// Map 1 - Arena Centrale
		Integer[][] map1 = { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1 }, { 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

		// Map 2 - Corridoi Stretti
		Integer[][] map2 = { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 0, 4, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 5, 0 }, { 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1 }, { 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1 },
				{ 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1 }, { 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
				{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

		// Map 3 - Stanze Multiple
		Integer[][] map3 = { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 0, 4, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 }, { 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 1, 0, 0, 0, 5, 0, 0, 1, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

		// Map 4 - Campo Aperto con Ostacoli
		Integer[][] map4 = { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

		// Map 5 - Labirinto
		Integer[][] map5 = { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 0, 4, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 }, { 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1 }, { 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 1 },
				{ 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1 }, { 1, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1 }, { 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
				{ 1, 1, 1, 0, 0, 0, 1, 0, 5, 1, 0, 0, 0, 1, 1, 1 }, { 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1 },
				{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

		gameMaps.add(map1);
		gameMaps.add(map2);
		gameMaps.add(map3);
		gameMaps.add(map4);
		gameMaps.add(map5);
	}

	public void draw(Graphics2D g2d) {
		if (mapIndex < 0 || mapIndex >= gameMaps.size()) {
			return;
		}

		Integer[][] currentMap = gameMaps.get(mapIndex);

		int mapWidth = currentMap[0].length * tileSize;
		int mapHeight = currentMap.length * tileSize;
		int offsetX = (screenWidth - mapWidth) / 2;
		int offsetY = (screenHeight - mapHeight) / 2;

		for (int row = 0; row < currentMap.length; row++) {
			for (int col = 0; col < currentMap[row].length; col++) {
				int x = col * tileSize + offsetX;
				int y = row * tileSize + offsetY;
				int tileType = currentMap[row][col];

				switch (tileType) {
				case 0:
					g2d.setColor(new Color(40, 40, 40));
					g2d.fillRect(x, y, tileSize, tileSize);
					break;
				case 1:
					g2d.setColor(new Color(80, 80, 80));
					g2d.fillRect(x, y, tileSize, tileSize);
					g2d.setColor(new Color(100, 100, 100));
					g2d.drawRect(x, y, tileSize, tileSize);
					break;
				case 4:
					g2d.setColor(new Color(40, 40, 40));
					g2d.fillRect(x, y, tileSize, tileSize);
					g2d.setColor(new Color(0, 100, 200, 100));
					g2d.fillRect(x, y, tileSize, tileSize);
					break;
				case 5:
					g2d.setColor(new Color(40, 40, 40));
					g2d.fillRect(x, y, tileSize, tileSize);
					g2d.setColor(new Color(200, 50, 50, 100));
					g2d.fillRect(x, y, tileSize, tileSize);
					break;
				}

				g2d.setColor(new Color(60, 60, 60, 50));
				g2d.drawRect(x, y, tileSize, tileSize);
			}
		}
	}

	public boolean isSolid(int mapIndex, int x, int y) {
		if (mapIndex < 0 || mapIndex >= gameMaps.size()) {
			return true;
		}

		Integer[][] currentMap = gameMaps.get(mapIndex);
		int col = x / tileSize;
		int row = y / tileSize;

		if (row < 0 || row >= currentMap.length || col < 0 || col >= currentMap[0].length) {
			return true;
		}

		int tileType = currentMap[row][col];
		return tileType == 1 || tileType == 3;
	}

	public int getTileSize() {
		return tileSize;
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	public int getMapWidth(int mapIndex) {
		if (mapIndex < 0 || mapIndex >= gameMaps.size()) {
			return 0;
		}
		return gameMaps.get(mapIndex)[0].length * tileSize;
	}

	public int getMapHeight(int mapIndex) {
		if (mapIndex < 0 || mapIndex >= gameMaps.size()) {
			return 0;
		}
		return gameMaps.get(mapIndex).length * tileSize;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public int[] getPlayerStartPosition() {
		if (mapIndex < 0 || mapIndex >= gameMaps.size()) {
			return null;
		}

		Integer[][] currentMap = gameMaps.get(mapIndex);
		
		int mapWidth = currentMap[0].length * tileSize;
		int mapHeight = currentMap.length * tileSize;
		int offsetX = (screenWidth - mapWidth) / 2;
		int offsetY = (screenHeight - mapHeight) / 2;

		for (int row = 0; row < currentMap.length; row++) {
			for (int col = 0; col < currentMap[row].length; col++) {
				if (currentMap[row][col] == 4) {
					int x = col * tileSize + tileSize/2 + offsetX;
					int y = row * tileSize + tileSize/2 + offsetY;
					return new int[] { x, y };
				}
			}
		}

		return null;
	}

	public int[] getEnemyStartPosition() {
		if (mapIndex < 0 || mapIndex >= gameMaps.size()) {
			return null;
		}

		Integer[][] currentMap = gameMaps.get(mapIndex);
		
		int mapWidth = currentMap[0].length * tileSize;
		int mapHeight = currentMap.length * tileSize;
		int offsetX = (screenWidth - mapWidth) / 2;
		int offsetY = (screenHeight - mapHeight) / 2;

		for (int row = 0; row < currentMap.length; row++) {
			for (int col = 0; col < currentMap[row].length; col++) {
				if (currentMap[row][col] == 5) {
					int x = col * tileSize + tileSize/2 + offsetX;
					int y = row * tileSize + tileSize/2 + offsetY;
					return new int[] { x, y };
				}
			}
		}

		return null;
	}
	
	public ArrayList<Tile> generateTiles(){
		if (mapIndex < 0 || mapIndex >= gameMaps.size()) {
			return null;
		}
		ArrayList<Tile> tiles = new ArrayList<>();
		Integer[][] currentMap = gameMaps.get(mapIndex);

		int mapWidth = currentMap[0].length * tileSize;
		int mapHeight = currentMap.length * tileSize;
		int offsetX = (screenWidth - mapWidth) / 2;
		int offsetY = (screenHeight - mapHeight) / 2;
		
		for (int row = 0; row < currentMap.length; row++) {
			for (int col = 0; col < currentMap[row].length; col++) {
				int x = col * tileSize + offsetX;
				int y = row * tileSize + offsetY;
				int tileType = currentMap[row][col];
				if(tileType == 1) {
					Rectangle[] hitbox = new Rectangle[]{new Rectangle(x, y, tileSize, tileSize)};
					tiles.add(new Tile("Tile("+x+";"+y+")",hitbox,null,x,y));
				}
			}
		}
		return tiles;
	}
	
}