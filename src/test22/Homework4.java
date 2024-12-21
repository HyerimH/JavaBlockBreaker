package test22;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.LinkedList;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

abstract class GameObject {
	int x, y;
	int w, h;

	Color color;

	Color[] borderColors = new Color[2];
	Point2D[] borderGradientPts = new Point2D[2];
	float[] dist = { 0.0f, 1.0f };
	LinearGradientPaint gradientPaint;

	public GameObject(int _x, int _y, int _w, int _h) {
		x = _x;
		y = _y;
		w = _w;
		h = _h;

		borderGradientPts[0] = new Point2D.Float(x, y);
		borderGradientPts[1] = new Point2D.Float(x, y + h);
	}

	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, w, h);

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3));
		g2.setPaint(gradientPaint);
		Rectangle2D rectangle2d = new Rectangle2D.Float(x, y, w, h);
		g2.draw(rectangle2d);
	}

	public void update(double dt) {
	};

	public boolean resolve(GameObject o) {
		return false;
	};

	public boolean isIn(BallObject ball) {
		// float distanceX, distanceY;

		double xmin = x - 13;
		double xmax = x + w + 13;
		double ymin = y - 13;
		double ymax = y + h + 13;

		if (ball.x > xmin && ball.x < xmax && ball.y > ymin && ball.y < ymax)
			return true;
		return false;
	}
}

class WallObject extends GameObject {

	public WallObject(int _x, int _y, int _w, int _h) {
		super(_x, _y, _w, _h);

		color = Color.WHITE;
		borderColors[0] = Color.GRAY;
		borderColors[1] = Color.GRAY;
		gradientPaint = new LinearGradientPaint(borderGradientPts[0], borderGradientPts[1], dist, borderColors);
	}
}

class BlockObject extends GameObject implements Runnable {

	Thread thread;
	int time = 100;

	boolean ballItem = false;

	public BlockObject(int _x, int _y, int _w, int _h, int t) {
		super(_x, _y, _w, _h);

		time *= t;

		color = Color.LIGHT_GRAY;

		borderColors[0] = Color.WHITE;
		borderColors[1] = Color.BLACK;
		gradientPaint = new LinearGradientPaint(borderGradientPts[0], borderGradientPts[1], dist, borderColors);
	}

	@Override
	public void run() {

		int n = 0;
		ballItem = true;

		borderColors[0] = new Color(255, 255, 000);
		borderColors[1] = new Color(204, 204, 000);
		gradientPaint = new LinearGradientPaint(borderGradientPts[0], borderGradientPts[1], dist, borderColors);

		while (true) {
			if (n == 0) {
				color = new Color(255, 255, 000);
				n = 1;
			} else {
				color = new Color(204, 204, 000);
				n = 0;
			}

			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

}

class PaddleObject extends GameObject {

	double vx = 0;
	double paddleSpeed = 5.0;

	public PaddleObject(int _x, int _y, int _w, int _h) {
		super(_x, _y, _w, _h);

		color = Color.LIGHT_GRAY;
		borderColors[0] = Color.LIGHT_GRAY;
		borderColors[1] = Color.BLACK;
		gradientPaint = new LinearGradientPaint(borderGradientPts[0], borderGradientPts[1], dist, borderColors);
	}

	public void keyPressed(int code) {
		if (code == KeyEvent.VK_LEFT) {
			vx = -paddleSpeed;
		} else if (code == KeyEvent.VK_RIGHT) {
			vx = paddleSpeed;
		}
	}

	public void keyReleased(int code) {
		if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT) {
			vx = 0.0;
		}
	}

	@Override
	public void update(double dt) {
		x += vx * dt;

		if (x < 15)
			x = 15;
		if (x > 770 - w)
			x = 770 - w;
	}

	@Override
	public boolean resolve(GameObject o) {
		if (x < 15)
			x = 15;
		if (x > 770 - w)
			x = 770 - w;
		return false;
	}
}

class BallObject extends GameObject {

	int x;
	int y;
	Color color;

	int centerX, centerY;

	double speed;
	double vx, vy;
	double angle = Math.random() * 90.0 - 135.0;
	int preX, preY;

	public BallObject(int _x, int _y, int _w, int _h, double _speed) {
		super(_x, _y, _w, _h);

		x = _x;
		y = _y;

		centerX = x + w / 2;
		centerY = y + h / 2;

		speed = _speed;
		vx = Math.cos(Math.toRadians(angle)) * speed;
		vy = Math.sin(Math.toRadians(angle)) * speed;

		color = Color.WHITE;
		borderColors[0] = Color.WHITE;
		borderColors[1] = Color.BLACK;
		gradientPaint = new LinearGradientPaint(borderGradientPts[0], borderGradientPts[1], dist, borderColors);
	}

	@Override
	public void draw(Graphics g) {

		g.setColor(color);
		g.fillOval(x, y, w, h);

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		// g2.setPaint(gradientPaint);
		g.setColor(Color.black);
		g.drawOval(x, y, w, h);
	}

	@Override
	public void update(double dt) {

		preX = x;
		preY = y;
		x = x + (int) (vx * dt);
		y = y + (int) (vy * dt);
	};

	@Override
	public boolean resolve(GameObject o) {

		// 벽, 벽돌, 패들에 isIn이 있어야함
		// 창 밖으로 떨어지는 거 고려
		// 밖으로 떨어지면 false, 아니면 true 리턴

		if (!(o instanceof GameObject))
			return false;

		if (o instanceof BallObject)
			return false;

		if (!o.isIn(this))
			return false;

		if (o instanceof PaddleObject && y > o.y + o.h + 5)
			return false;

		double xmin = o.x - w / 2;
		double xmax = o.x + o.w + w / 2;
		double ymin = o.y - w / 2;
		double ymax = o.y + o.h + w / 2;

		if (preX < xmin || preX > xmax) {
			vx = -vx;
		}
		if (preY < ymin || preY > ymax) {
			if (o instanceof PaddleObject) {
				vx = Math.cos(Math.toRadians(90.0 * (double) ((x - o.x) / (double) o.w) - 135.0)) * speed;
			}
			vy = -vy;
		}
		return true;
	};

}

@SuppressWarnings("serial")
class BackGroundPanel extends JPanel {

	boolean running = false;

	int width = 0, height = 0;
	static int highScore = 0;
	static int yourScore = 0;

	public BackGroundPanel(int w, int h) {
		setBackground(Color.BLACK);
		width = w;
		height = h;
	}

	public void threadState(boolean state) {
		running = state;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Color startColor = Color.LIGHT_GRAY;
		Color endColor = Color.BLACK;

		Point2D center = new Point2D.Float(getWidth() / 2f, getHeight() / 2f + 15);
		float radius = Math.min(getWidth(), getHeight()) * 0.6f;

		RadialGradientPaint radialGradientPaint = new RadialGradientPaint(center, radius, new float[] { 0.0f, 1.0f },
				new Color[] { startColor, endColor });

		Graphics2D g2d = (Graphics2D) g;

		g2d.setPaint(radialGradientPaint);
		g2d.fillOval((int) (center.getX() - radius), (int) (center.getY() - radius), (int) (2 * radius),
				(int) (2 * radius));
	}

}

@SuppressWarnings("serial")
class Homework4IntroPanel extends BackGroundPanel implements Runnable {

	JLabel hwName = new JLabel("<html><br><br>Java Programming Homework #5</html>");
	JLabel gameName = new JLabel("<html><br>BLOCK BREAKER</html>");
	JLabel myName = new JLabel("22011838 김하연");
	JLabel startInfo = new JLabel("PRESS SPACEBAR TO PLAY!");

	JLabel[] introInfo = new JLabel[7];

	Clip clip = null;
	boolean playing = false;

	public Homework4IntroPanel(int w, int h) {
		super(w, h);

		setLayout(new GridLayout(0, 1));

		for (int i = 0; i < 7; i++) {
			introInfo[i] = new JLabel();
			introInfo[i].setHorizontalAlignment(JLabel.CENTER);
			add(introInfo[i]);
		}

		introInfo[1].setFont(new Font("Bodoni MT Black", Font.PLAIN, 30));
		introInfo[1].setText("Java Programming Homework #5");
		introInfo[1].setForeground(Color.WHITE);

		introInfo[2].setFont(new Font("Algerian", Font.PLAIN, 90));
		introInfo[2].setText("BLOCK BREAKER");
		introInfo[2].setForeground(Color.WHITE);

		introInfo[4].setFont(new Font("Elephant", Font.BOLD, 25));
		introInfo[4].setText("<html><br>PRESS SPACEBAR TO PLAY !</html>");
		introInfo[4].setForeground(Color.red);

		Thread thread = new Thread(this);
		thread.start();
	}

	public void playBGM() {
		if (playing)
			return;

		try {
			File audioFile = new File("res/bgm.wav");
			//URL audioFile = getClass().getClassLoader().getResource("src/bgm.wav");
			final AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			clip = AudioSystem.getClip();
			clip.stop();
			clip.open(audioStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			playing = true;
		} catch (Exception e) {
			return;
		}
	}

	public void stopBGM() {
		if (!playing)
			return;
		clip.stop();
		playing = false;
	}

	@Override
	public void run() {
		int n = 0;

		while (true) {
			if (running) {
				if (n == 0) {
					introInfo[4].setText("<html><br>PRESS SPACEBAR TO PLAY !</html>");
					n = 1;
				} else {
					introInfo[4].setText("");
					n = 0;
				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					return;
				}
			} else {
				Thread.yield();
			}
		}
	}
}

@SuppressWarnings("serial")
class Homework4PlayPanel extends BackGroundPanel implements Runnable {

	/*블럭 끝: 350
	패들: x=325, y=700, w=150, h=30*/
	int stage = 0;
	int blockNum;

	// 벽
	WallObject[] walls = new WallObject[3];

	// 벽돌
	int startX = 14, startY = 14;
	int endX, endY;
	int interval;
	LinkedList<BlockObject> blocks = new LinkedList<BlockObject>();

	// 패들
	PaddleObject paddleObject;

	// 공
	LinkedList<BallObject> balls = new LinkedList<BallObject>();
	
	Clip breakClip = null, paddleClip = null, deadClip = null;

	boolean gameStart = false;

	public Homework4PlayPanel(int w, int h) {
		super(w, h);

		setLayout(null);

		endX = width - 28;
		endY = 454;
		interval = (int) ((endX - startX) * 0.006 / (stage * 3.0 + 1));

		walls[0] = new WallObject(1, 1, width - 15, 10);
		walls[1] = new WallObject(1, 11, 10, height - 49);
		walls[2] = new WallObject(width - 26, 11, 11, height - 49);

		paddleObject = new PaddleObject(325, 700, 150, 25);
		
		Thread thread = new Thread(this);
		thread.start();
	}

	public void breakEffect() {
		try {
			File audioFile = new File("res/effect.wav");
			final AudioInputStream breakAudioStream = AudioSystem.getAudioInputStream(audioFile);
			breakClip = AudioSystem.getClip();
			breakClip.stop();
			breakClip.open(breakAudioStream);
			breakClip.start();
		} catch (Exception e) {
			return;
		}
	}
	
	public void paddleEffect() {
		try {
			File audioFile = new File("res/clickSound.wav");
			final AudioInputStream breakAudioStream = AudioSystem.getAudioInputStream(audioFile);
			breakClip = AudioSystem.getClip();
			breakClip.stop();
			breakClip.open(breakAudioStream);
			breakClip.start();
		} catch (Exception e) {
			return;
		}
	}
	
	public void deadEffect() {
		try {
			File audioFile = new File("res/beepbeep.wav");
			final AudioInputStream breakAudioStream = AudioSystem.getAudioInputStream(audioFile);
			breakClip = AudioSystem.getClip();
			breakClip.stop();
			breakClip.open(breakAudioStream);
			breakClip.start();
		} catch (Exception e) {
			return;
		}
	}

	public void addBlock() {
		blocks.clear();

		blockNum = (stage * 3) * (stage * 3);

		int blockX = startX + interval, blockY = startY + interval;
		float blockW = (float) ((endX - startX - (stage * 3 * 2) * interval) / (float) (stage * 3.0));
		float blockH = (float) ((endY - startY - (stage * 3 * 2) * interval) / (float) (stage * 3.0));
		for (int i = 0; i < blockNum; i++) {
			if (i % (stage * 3) == 0) {
				blockX = startX + interval;
			}

			blocks.add(new BlockObject(blockX, blockY, (int) blockW, (int) blockH, i % 5 + 3));

			blockX += blockW + interval * 2;
			if (i % (stage * 3) == (stage * 3 - 1))
				blockY += blockH + interval * 2;
		}

		Random random = new Random();
		int itemBlockNum = random.nextInt(blockNum / 3 + 1) + 1;

		for (int i = 0; i < itemBlockNum; i++) {
			int num = random.nextInt(blockNum);
			blocks.get(num).thread = new Thread(blocks.get(num));
			blocks.get(num).thread.start();
		}

	}

	public void itemEffect(int ballIdx, int x, int y) {

		double vx = balls.get(ballIdx).vx - 2;
		double vy = balls.get(ballIdx).vy;

		for (int i = 0; i < 2; i++) {
			balls.add(new BallObject(x, y, 12, 12, 5));
			balls.getLast().vx = vx;
			balls.getLast().vy = vy;
			vx += 4;
		}
	}

	public void setScore() {
		yourScore = 0;
	}

	public void nextStage() {

		stage++;
		balls.clear();
		balls.add(new BallObject(395, 680, 12, 12, 5));
		paddleObject.x = 325;
		paddleObject.y = 700;

		if (stage > 1) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				return;
			}
		}

		blockNum = (stage * 3) * (stage * 3);
		addBlock();
	}

	public void resetGame() {

		stage = 0;
		blockNum = 0;

		if (paddleObject != null) {
			paddleObject.x = 325;
			paddleObject.y = 700;
		}

		balls.clear();
		balls.add(new BallObject(395, 680, 12, 12, 5));
	}

	public void gameOver() {
		Component targetComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
		Homework4Frame frame = (Homework4Frame) targetComponent;
		frame.display = 2;
	}

	public void keyPressed(KeyEvent e) {
		paddleObject.keyPressed(e.getKeyCode());
	}

	public void keyReleased(KeyEvent e) {
		paddleObject.keyReleased(e.getKeyCode());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (int i = 0; i < 3; i++)
			walls[i].draw(g);

		for (int i = 0; i < blockNum; i++)
			blocks.get(i).draw(g);

		paddleObject.draw(g);

		for (BallObject b : balls)
			b.draw(g);
	}

	@Override
	public void run() {
		while (true) {
			if (running) {
				// 1. update
				if (blockNum == 0) {
					nextStage();
				}

				paddleObject.update(1.5);

				for (int i = 0; i < balls.size(); i++) {
					balls.get(i).update(1);
					if (balls.get(i).y > walls[1].y + walls[1].h)
						balls.remove(i);
				}

				if (balls.size() == 0) {
					deadEffect();
					gameOver();
				}

				// 2. resolve
				paddleObject.resolve(walls[1]);
				paddleObject.resolve(walls[2]);

				for (int i = 0; i < balls.size(); i++) {
					for (int j = 0; j < 3; j++)
						balls.get(i).resolve(walls[j]);
					for (int j = 0; j < blocks.size(); j++) {
						if (balls.get(i).resolve(blocks.get(j))) {
							if (blocks.get(j).ballItem)
								itemEffect(i, balls.get(i).x, balls.get(i).y);
							breakEffect();
							blocks.remove(j);
							blockNum--;
							yourScore += 10;
						}
					}
					if(balls.get(i).resolve(paddleObject)) paddleEffect();
				}

				// 3. render
				repaint();

				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					return;
				}
			} else {
				resetGame();
				Thread.yield();
			}
		}
	}
}

@SuppressWarnings("serial")
class Homework4GameOverPanel extends BackGroundPanel implements Runnable {

	JLabel[] endInfo = new JLabel[8];

	ImageIcon smileIcon = new ImageIcon("res/smail.png");
	Image smileImage = smileIcon.getImage();

	ImageIcon cryIcon = new ImageIcon("res/cry.png");
	Image cryImage = cryIcon.getImage();

	boolean newRecord = false;
	float imageAlpha = 255.0f;

	public Homework4GameOverPanel(int w, int h) {
		super(w, h);

		setLayout(new GridLayout(0, 1));

		for (int i = 0; i < 8; i++) {
			endInfo[i] = new JLabel();
			endInfo[i].setHorizontalAlignment(JLabel.CENTER);
			add(endInfo[i]);
		}

		endInfo[2].setText("GAME OVER");
		endInfo[2].setFont(new Font("Algerian", Font.PLAIN, 100));
		endInfo[2].setForeground(Color.white);

		endInfo[4].setText("<html>HIGH SCORE: " + highScore + "<br>YOUR SCORE: " + yourScore + "</html>");
		endInfo[4].setFont(new Font("Bodoni MT Black", Font.PLAIN, 30));
		endInfo[4].setForeground(Color.white);

		endInfo[5].setText("PRESS SPACEBAR !");
		endInfo[5].setFont(new Font("Elephant", Font.BOLD, 25));
		endInfo[5].setForeground(Color.red);

		Thread thread = new Thread(this);
		thread.start();
	}

	public void setScore() {
		if (highScore < yourScore) {
			highScore = yourScore;
			newRecord = true;
		} else if (highScore > yourScore)
			newRecord = false;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		endInfo[4].setText("<html>HIGH SCORE: " + highScore + "<br>YOUR SCORE: " + yourScore + "</html>");

		Graphics2D g2 = (Graphics2D) g;
		AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageAlpha / 255.0f);
		g2.setComposite(alphaComposite);

		if (newRecord)
			g.drawImage(smileImage, 330, 260, 150, 150, this);
		else
			g.drawImage(cryImage, 330, 260, 150, 150, this);

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}

	@Override
	public void run() {
		int message = 0;

		while (true) {
			if (running) {

				if (message < 5) {
					endInfo[5].setText("PRESS SPACEBAR !");
				} else {
					endInfo[5].setText("");
				}

				if (imageAlpha > 0)
					imageAlpha -= 5;

				message++;
				if (message == 10)
					message = 0;

				repaint();

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					return;
				}
			} else {
				setScore();
				Thread.yield();
			}
		}
	}

	public void setImageAlpha() {
		imageAlpha = 255.0f;
	}
}

@SuppressWarnings("serial")
class Homework4Frame extends JFrame implements Runnable, KeyListener {

	int display = 0;
	int size = 800;
	Homework4IntroPanel introPanel = new Homework4IntroPanel(size, size);
	Homework4PlayPanel playPanel = new Homework4PlayPanel(size, size);
	Homework4GameOverPanel gameoverPanel = new Homework4GameOverPanel(size, size);

	public Homework4Frame() {
		setTitle("Java Homework4");
		setSize(size, size);
		setResizable(false);

		Thread thread = new Thread(this);
		thread.start();

		add(introPanel);
		addKeyListener(this);

		setFocusable(true);
		requestFocus();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void runningPanel() {
		introPanel.run();
		playPanel.run();
		gameoverPanel.run();
	}

	@Override
	public void run() {

		while (true) {

			getContentPane().removeAll();

			if (display == 0) {
				introPanel.playBGM();
				playPanel.setScore();
				getContentPane().add(introPanel);
				introPanel.threadState(true);
				playPanel.threadState(false);
				gameoverPanel.threadState(false);
			} else if (display == 1) {
				introPanel.stopBGM();
				gameoverPanel.setImageAlpha();
				getContentPane().add(playPanel);
				introPanel.threadState(false);
				playPanel.threadState(true);
				gameoverPanel.threadState(false);
			} else if (display == 2) {
				getContentPane().add(gameoverPanel);
				introPanel.threadState(false);
				playPanel.threadState(false);
				gameoverPanel.threadState(true);
			}

			revalidate();
			repaint();

			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	@Override
	public synchronized void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			display++;
			if (display > 2)
				display = 0;
		} else if (display == 1 && e.getKeyCode() == KeyEvent.VK_LEFT) {
			playPanel.keyPressed(e);
		} else if (display == 1 && e.getKeyCode() == KeyEvent.VK_RIGHT) {
			playPanel.keyPressed(e);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (display == 1 && e.getKeyCode() == KeyEvent.VK_LEFT) {
			playPanel.keyReleased(e);
		} else if (display == 1 && e.getKeyCode() == KeyEvent.VK_RIGHT) {
			playPanel.keyReleased(e);
		}
	}
}

public class Homework4 {

	public static void main(String[] args) {

		@SuppressWarnings("unused")
		Homework4Frame frame = new Homework4Frame();
	}

}
