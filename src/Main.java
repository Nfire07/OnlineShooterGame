import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Main {
	public static int GAME_MAP = 0;
	public static Socket socket = null;
	public static PrintWriter pw = null;
	public static BufferedReader br = null;
	public static Point startingPos = null;
	private static volatile boolean running = true;
	private static volatile boolean gameEnded = false;

	public static Image imageLoader(String filePath) {
		Image i = null;
		if (GameObject.debugMode == true)
			System.out.println("[imageLoader]Loading: " + filePath + "\n");
		try {
			i = ImageIO.read(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return i;
	}

	private static void showEndGameWindow(String result) {
		SwingUtilities.invokeLater(() -> {
			JFrame endGameWindow = new JFrame("Game Over");
			endGameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			endGameWindow.setSize(500, 300);
			endGameWindow.setLocationRelativeTo(null);
			endGameWindow.setUndecorated(true);
			endGameWindow.getContentPane().setBackground(Color.DARK_GRAY);
			endGameWindow.setLayout(new BorderLayout());

			JLabel resultLabel = new JLabel(result, JLabel.CENTER);
			resultLabel.setFont(new Font("Verdana", Font.BOLD, 40));
			resultLabel.setBorder(BorderFactory.createEmptyBorder(50, 20, 20, 20));

			Color textColor;
			if (result.contains("VICTORY")) {
				textColor = new Color(0, 200, 0);
			} else if (result.contains("DEFEAT")) {
				textColor = new Color(200, 0, 0);
			} else {
				textColor = new Color(200, 200, 0);
			}
			resultLabel.setForeground(textColor);

			JButton exitButton = new JButton("Exit");
			exitButton.setBackground(Color.decode("#aa0000"));
			exitButton.setForeground(Color.WHITE);
			exitButton.setFont(new Font("Verdana", Font.BOLD, 24));
			exitButton.setBorder(null);
			exitButton.setFocusable(false);
			exitButton.setPreferredSize(new Dimension(200, 60));
			exitButton.addActionListener(e -> {
				endGameWindow.dispose();
				System.exit(0);
			});

			JPanel buttonPanel = new JPanel();
			buttonPanel.setBackground(Color.DARK_GRAY);
			buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
			buttonPanel.add(exitButton);

			endGameWindow.add(resultLabel, BorderLayout.CENTER);
			endGameWindow.add(buttonPanel, BorderLayout.SOUTH);
			endGameWindow.setVisible(true);
		});
	}

	public static void main(String[] args) {
		JFrame mainMenu = new JFrame();

		mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainMenu.setSize(Window.screen);
		mainMenu.setLayout(new BorderLayout());
		mainMenu.setLocationRelativeTo(null);
		mainMenu.setUndecorated(true);
		mainMenu.getContentPane().setBackground(Color.DARK_GRAY);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.DARK_GRAY);

		JLabel title = new JLabel("Online Shooter Game", JLabel.CENTER);
		title.setFont(new Font("Verdana", Font.BOLD, 50));
		title.setForeground(Color.WHITE);
		title.setPreferredSize(new Dimension(mainMenu.getWidth(), 120));
		title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
		mainPanel.add(title, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new GridLayout(1, 2, 0, 0));
		centerPanel.setBackground(Color.DARK_GRAY);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBackground(Color.DARK_GRAY);
		leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

		JButton startButton = createButton("Start Game", "#00aa00");
		JButton settingsButton = createButton("Show Settings", "#aaaa00");
		JButton exitButton = createButton("Exit", "#aa0000");

		leftPanel.add(Box.createVerticalGlue());
		leftPanel.add(startButton);
		leftPanel.add(Box.createVerticalGlue());
		leftPanel.add(settingsButton);
		leftPanel.add(Box.createVerticalGlue());
		leftPanel.add(exitButton);
		leftPanel.add(Box.createVerticalGlue());

		centerPanel.add(leftPanel);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setBackground(Color.DARK_GRAY);
		rightPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

		JLabel portLabel = new JLabel("Port:");
		portLabel.setFont(new Font("Verdana", Font.BOLD, 30));
		portLabel.setForeground(Color.WHITE);
		portLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		String port = "";
		String ip = "";
		String nickname = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("settings.conf")));
			port = br.readLine();
			ip = br.readLine();
			nickname = br.readLine();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JTextField portField = new JTextField((port == null || port.isEmpty()) ? "5000" : port);
		portField.setFont(new Font("Verdana", Font.BOLD, 30));
		portField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		portField.setBackground(Color.DARK_GRAY);
		portField.setForeground(Color.WHITE);
		portField.setCaretColor(Color.WHITE);
		portField.setMaximumSize(new Dimension(500, 150));
		portField.setAlignmentX(JTextField.LEFT_ALIGNMENT);

		JLabel ipLabel = new JLabel("Server IP:");
		ipLabel.setFont(new Font("Verdana", Font.BOLD, 30));
		ipLabel.setForeground(Color.WHITE);
		ipLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		JTextField ipField = new JTextField((ip == null || ip.isEmpty()) ? "127.0.0.1" : ip);
		ipField.setFont(new Font("Verdana", Font.BOLD, 30));
		ipField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		ipField.setBackground(Color.DARK_GRAY);
		ipField.setForeground(Color.WHITE);
		ipField.setCaretColor(Color.WHITE);
		ipField.setMaximumSize(new Dimension(500, 150));
		ipField.setAlignmentX(JTextField.LEFT_ALIGNMENT);

		JLabel nicknameLabel = new JLabel("Nickname:");
		nicknameLabel.setFont(new Font("Verdana", Font.BOLD, 30));
		nicknameLabel.setForeground(Color.WHITE);
		nicknameLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		JTextField nicknameField = new JTextField((nickname == null || nickname.isEmpty()) ? "" : nickname);
		nicknameField.setFont(new Font("Verdana", Font.BOLD, 30));
		nicknameField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		nicknameField.setBackground(Color.DARK_GRAY);
		nicknameField.setForeground(Color.WHITE);
		nicknameField.setCaretColor(Color.WHITE);
		nicknameField.setMaximumSize(new Dimension(500, 150));
		nicknameField.setAlignmentX(JTextField.LEFT_ALIGNMENT);

		JButton applyButton = createButton("Apply", "#0088ff");

		rightPanel.add(Box.createVerticalGlue());
		rightPanel.add(portLabel);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		rightPanel.add(portField);
		rightPanel.add(Box.createVerticalGlue());
		rightPanel.add(ipLabel);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		rightPanel.add(ipField);
		rightPanel.add(Box.createVerticalGlue());
		rightPanel.add(nicknameLabel);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		rightPanel.add(nicknameField);
		rightPanel.add(Box.createVerticalGlue());
		rightPanel.add(applyButton);
		rightPanel.add(Box.createVerticalGlue());

		rightPanel.setVisible(false);

		centerPanel.add(rightPanel);

		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainMenu.add(mainPanel, BorderLayout.CENTER);

		boolean[] settingsToggle = new boolean[1];
		settingsToggle[0] = false;

		String[] passedIp = new String[1];
		passedIp[0] = ip;
		int[] passedPort = new int[1];
		passedPort[0] = Integer.parseInt(port);
		String[] passedNickname = new String[1];
		passedNickname[0] = nickname;

		startButton.addActionListener((e) -> {
			mainMenu.setVisible(false);
			mainMenu.dispose();

			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				running = false;
				GameAudio.shutdown();
				try {
					if (socket != null && !socket.isClosed()) {
						socket.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}));

			new Thread(() -> {
				try {
					socket = new Socket(passedIp[0], passedPort[0]);
					pw = new PrintWriter(socket.getOutputStream(), true);
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				if (socket == null) {
					System.out.println("Connection Refused or unavailable");
					System.exit(-1);
				}

				if (pw == null) {
					System.out.println("Error creating PrintWriter");
					System.exit(-1);
				}

				if (br == null) {
					System.out.println("Error creating BufferedReader");
					System.exit(-1);
				}

				pw.println(passedNickname[0]);
				pw.println(Window.screen.width + "x" + Window.screen.height);

				try {
					final JFrame[] waitingWindow = new JFrame[1];
					new Thread(() -> {
						waitingWindow[0] = new JFrame("Waiting...");
						waitingWindow[0].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						waitingWindow[0].setSize(300, 100);
						waitingWindow[0].setLocationRelativeTo(null);
						waitingWindow[0].setResizable(false);

						JLabel waiting = new JLabel("Waiting for the other player...");
						waiting.setBounds(0, 0, 300, 100);
						waiting.setHorizontalAlignment(JLabel.CENTER);
						waiting.setVerticalAlignment(JLabel.CENTER);
						waitingWindow[0].add(waiting);
						waitingWindow[0].setVisible(true);
					}).start();

					String line;
					Main.GAME_MAP = Integer.parseInt(br.readLine());
					String[] pos = br.readLine().split(";");
					Main.startingPos = new Point(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]));

					while ((line = br.readLine()) != null && !line.equals("START"));

					if (waitingWindow[0] != null) {
						waitingWindow[0].dispose();
					}

					Window w = new Window();
					GameTimer timer = new GameTimer();
					GameObject.setDebugMode(false);
					
					Enemy enemy = (Enemy) w.gameObjects.get(1);
					new Thread(() -> {
						try {
							while (running && !socket.isClosed() && !gameEnded) {
								String posLine = br.readLine();
								if (posLine != null && !posLine.isEmpty()) {
									String[] enemyPos = posLine.split(";");
									if (enemyPos.length == 2) {
										Point enemyPosition = new Point(
											Integer.parseInt(enemyPos[0].trim()),
											Integer.parseInt(enemyPos[1].trim())
										);
										enemy.updateEnemyPosition(enemyPosition);
									}
								}
								
								String hpLine = br.readLine();
								if (hpLine != null && !hpLine.isEmpty()) {
									int enemyHp = Integer.parseInt(hpLine.trim());
									enemy.updateEnemyHp(enemyHp);
								}
								
								ArrayList<Bullet> enemyBullets = new ArrayList<>();
								String bulletLine;
								while ((bulletLine = br.readLine()) != null && !bulletLine.isEmpty()) {
									enemyBullets.add(new Bullet(bulletLine, Color.decode("#bf4b8f")));
								}
								enemy.updateEnemyBullets(enemyBullets);
							}
						} catch (IOException ex) {
							if (running && !gameEnded) {
								System.err.println("Error receiving data from server: " + ex.getMessage());
							}
						}
					}).start();

					final double TARGET_FPS = 60.0;
					final double TARGET_FRAME_TIME = 1000.0 / TARGET_FPS;

					while (running && !gameEnded) {
						long frameStart = System.currentTimeMillis();

						float deltaTime = timer.update();

						w.inputManager.update();
						w.gameObjects.forEach(obj -> obj.UpdatePosition(deltaTime));

						Player p = (Player) w.gameObjects.get(0);

						w.gameObjects.forEach(obj -> {
							Tile t = null;
							try {
								t = (Tile) obj;
							} catch (Exception ex) {
							}
							if (t != null)
								t.repulse(p);
						});

						p.checkRayIntersections(w.gameObjects);
						w.gameObjects.forEach(obj -> {
							p.checkForHit(obj);
						});
						
						ArrayList<Bullet> hittingBullets = new ArrayList<>();
						for (Bullet b : Enemy.bullets) {
							if (p.hitbox[0].intersects(b.hitbox) && !b.hasHitted()) {
								p.hp -= Bullet.damage;
								b.setHitted(true);
								if (GameObject.debugMode)
									System.out.println("Player HP: " + p.hp);
							}
							if (b.hasHitted()) {
								hittingBullets.add(b);
							}
						}
						Enemy.bullets.removeAll(hittingBullets);

						if (p.hp <= 0 || enemy.hp <= 0) {
							gameEnded = true;
							running = false;
							
							SwingUtilities.invokeLater(() -> {
								w.setVisible(false);
								w.dispose();
							});
							
							String result;
							if (p.hp <= 0 && enemy.hp <= 0) {
								result = "DRAW!";
							} else if (p.hp <= 0) {
								result = "DEFEAT!";
							} else {
								result = "VICTORY!";
							}
							
							showEndGameWindow(result);
							
							try {
								if (socket != null && !socket.isClosed()) {
									socket.close();
								}
							} catch (IOException ex) {
								ex.printStackTrace();
							}
							
							break;
						}

						w.g.repaint();

						long frameTime = System.currentTimeMillis() - frameStart;
						long sleepTime = (long) (TARGET_FRAME_TIME - frameTime);

						if (sleepTime > 0) {
							try {
								Thread.sleep(sleepTime);
							} catch (InterruptedException ex) {
								ex.printStackTrace();
							}
						}

						if (GameObject.debugMode) {
							System.out.println("FPS: " + timer.getFPS() + " | DeltaTime: "
									+ String.format("%.4f", deltaTime) + "s");
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}).start();
		});

		settingsButton.addActionListener((e) -> {
			settingsToggle[0] = !settingsToggle[0];
			if (settingsToggle[0]) {
				settingsButton.setText("Hide Settings");
				rightPanel.setVisible(true);
			} else {
				settingsButton.setText("Show Settings");
				rightPanel.setVisible(false);
			}
			centerPanel.revalidate();
			centerPanel.repaint();
		});

		applyButton.addActionListener((e) -> {
			try {
				PrintWriter pw = new PrintWriter("settings.conf");
				pw.println(portField.getText());
				pw.println(ipField.getText());
				pw.println(nicknameField.getText());
				passedIp[0] = ipField.getText();
				passedPort[0] = Integer.parseInt(portField.getText());
				passedNickname[0] = nicknameField.getText();
				pw.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		});

		exitButton.addActionListener((e) -> {
			mainMenu.dispose();
			System.exit(0);
		});

		mainMenu.setVisible(true);
	}

	private static JButton createButton(String text, String colorHex) {
		JButton button = new JButton(text);
		button.setBackground(Color.decode(colorHex));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Verdana", Font.BOLD, 24));
		button.setBorder(null);
		button.setFocusable(false);
		button.setAlignmentX(JButton.CENTER_ALIGNMENT);
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
		button.setPreferredSize(new Dimension(500, 150));
		return button;
	}
}