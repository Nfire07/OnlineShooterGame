import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main {
    public static final int GAME_MAP = 0;
	
    public static Image imageLoader(String filePath) {
        Image i = null;
        if(GameObject.debugMode == true)
            System.out.println("[imageLoader]Loading: " + filePath + "\n");
        try {
            i = ImageIO.read(new File(filePath));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return i;
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
    	
    	BufferedReader br;
    	String port = "";
		String ip = "";
		try {
			br = new BufferedReader(new FileReader(new File("settings.conf")));
			port = br.readLine();
			ip = br.readLine();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JTextField portField = new JTextField((port==null || port.isEmpty()) ? "5000" : port);
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
    	
    	JTextField ipField = new JTextField((ip==null || ip.isEmpty()) ? "127.0.0.1" : ip);
    	ipField.setFont(new Font("Verdana", Font.BOLD, 30));
    	ipField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    	ipField.setBackground(Color.DARK_GRAY);
    	ipField.setForeground(Color.WHITE);
    	ipField.setCaretColor(Color.WHITE);
    	ipField.setMaximumSize(new Dimension(500, 150));
    	ipField.setAlignmentX(JTextField.LEFT_ALIGNMENT);
    	
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
    	rightPanel.add(applyButton);
    	rightPanel.add(Box.createVerticalGlue());
    	
    	rightPanel.setVisible(false);
    	
    	centerPanel.add(rightPanel);
    	
    	mainPanel.add(centerPanel, BorderLayout.CENTER);
    	mainMenu.add(mainPanel, BorderLayout.CENTER);
    	
    	boolean[] settingsToggle = new boolean[1];
    	settingsToggle[0] = false;
    	
    	startButton.addActionListener((e) -> {
    	    mainMenu.setVisible(false);
    	    mainMenu.dispose();
    	    
    	    Window w = new Window();
    	    GameTimer timer = new GameTimer();
    	    GameObject.setDebugMode(false);
    	    final double TARGET_FPS = 60.0;
    	    final double TARGET_FRAME_TIME = 1000.0 / TARGET_FPS;
    	            
    	    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    	        GameAudio.shutdown();
    	    }));
    	    
    	    GameAudio.playSound(0);
    	    
    	    new Thread(() -> {
    	        while(true) {
    	            long frameStart = System.currentTimeMillis();
    	            
    	            float deltaTime = timer.update();
    	            
    	            w.inputManager.update();
    	            w.gameObjects.forEach(obj -> obj.UpdatePosition(deltaTime));
    	            
    	            Player p = (Player)w.gameObjects.get(0);
    	            
    	            w.gameObjects.forEach(obj -> {
    	                Tile t = null;
    	                try {
    	                    t = (Tile)obj;
    	                } catch (Exception ex) {}
    	                if(t != null)
    	                    t.repulse(p);
    	            });

    	            p.checkRayIntersections(w.gameObjects);
    	            w.gameObjects.forEach(obj -> {
    	                p.checkForHit(obj);
    	            });
    	            
    	            w.g.repaint();
    	            
    	            long frameTime = System.currentTimeMillis() - frameStart;
    	            long sleepTime = (long)(TARGET_FRAME_TIME - frameTime);
    	            
    	            if(sleepTime > 0) {
    	                try {
    	                    Thread.sleep(sleepTime);
    	                } catch (InterruptedException ex) {
    	                    ex.printStackTrace();
    	                }
    	            }
    	            
    	            if(GameObject.debugMode) {
    	                System.out.println("FPS: " + timer.getFPS() + " | DeltaTime: " + 
    	                                   String.format("%.4f", deltaTime) + "s");
    	            }
    	        }
    	    }).start();
    	});
    	
    	settingsButton.addActionListener((e) -> {
    		settingsToggle[0] = !settingsToggle[0];
    		if(settingsToggle[0]) {
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