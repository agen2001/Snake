import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.util.Random;

public class SnakePanel extends JPanel implements ActionListener{

	static final int FRAME_WIDTH = 600;
	static final int FRAME_HEIGHT = 600;
	static final int SIZE = 25;
	static final int SNAKE_UNITS = (FRAME_WIDTH*FRAME_HEIGHT)/SIZE;
	static final int DELAY = 75;
	final int x[] = new int[SNAKE_UNITS];
	final int y[] = new int[SNAKE_UNITS];
	int bodyParts = 3;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	SnakePanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		random = new Random();
		this.setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();

		//Plays the music
		File file  = new File("Japanese Trap & Bass Type Beat.wav");
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
		Clip film = AudioSystem.getClip();
		film.open(audioStream);
		film.start();
	}
	
	public void startGame() {
		
		createApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if(running) {
		g.setColor(Color.red);
		g.fillOval(appleX, appleY, SIZE, SIZE);
		
		for(int i = 0; i < bodyParts; i++) {
			if(i == 0) {
				g.setColor(Color.blue);
				g.fillRect(x[i], y[i], SIZE, SIZE);
			}
			else {
				g.setColor(new Color(0, 0, 153));
				g.fillRect(x[i], y[i], SIZE, SIZE);
			   }
			}
		g.setColor(Color.white);
		g.setFont(new Font("Ariale", Font.BOLD,40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten,  (FRAME_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}

	}
	
	public void createApple() {
		
		appleX = random.nextInt((int)(FRAME_WIDTH/SIZE)) * SIZE;
		appleY = random.nextInt((int)(FRAME_HEIGHT/SIZE)) * SIZE;
		
	}
	
	public void moveSnake() {
		
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
			
		}
		switch(direction) {
		case 'U':
			y[0] = y[0] - SIZE;
			break;
		case 'D':
			y[0] = y[0] + SIZE;
			break;
		case 'L':
			x[0] = x[0] - SIZE;
			break;
		case 'R':
			x[0] = x[0] + SIZE;
			break; 
			
		}
		
	}
	
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			createApple();
		}
		
	}
	
	public void checkCollisions() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		
		for(int i = bodyParts; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])){
				running = false;
			}
		}
		
		if(x[0] < 0) {
			running = false;
			timer.stop();
		}
		
		if(x[0] > FRAME_WIDTH) {
			running = false;
		}
		
		if(y[0] < 0) {
			running = false;
		}
		
		if(y[0] > FRAME_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD,40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("You Scored: " + applesEaten,  (FRAME_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/3, g.getFont().getSize());
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD,75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("GAME OVER!!!",  (FRAME_WIDTH - metrics2.stringWidth("GAME OVER!!!"))/2,  FRAME_HEIGHT/2);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD,30));
		g.drawString("Press space bar to play again", (FRAME_WIDTH - metrics2.stringWidth("GAME OVER!!!")) - 0,  FRAME_HEIGHT - 100);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(running) {
			moveSnake();
			checkApple();
			try {
				checkCollisions();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		
		public void keyPressed(KeyEvent k) {
			
			switch(k.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
				
				}
		
			if(k.getKeyCode() == KeyEvent.VK_ENTER) {
				timer.stop();
			}
			
			if(k.getKeyCode() == KeyEvent.VK_R) {
				timer.start(); 
			}
			
			if(k.getKeyCode() == KeyEvent.VK_SPACE) {
				if(!running) {
					running = true;
					bodyParts = 3;
					createApple();
					applesEaten = 0;
					x[0] = 0;
					y[0] = 0;
					direction = 'R';
					timer.restart();
					try {
						new SnakePanel();
					} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				repaint();	
			}
		}
	}
}