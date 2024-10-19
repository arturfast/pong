package pong;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JFrame implements KeyListener {
	final static int SCREEN_WIDTH = 800;
	final static int SCREEN_HEIGHT = 500;
	final static int requiredPointsToWin = 500;
	final static byte gameSpeed = 30;
	public static boolean gameStarted = false;
	boolean isWon = false;
	Player player = new Player();
	Bot bot = new Bot();
	Ball ball = new Ball(player, bot);
	Draw draw = new Draw();
	Timer moveTimer;
	Timer repaintTimer;
	KeyEvent lastKeyEvent = null;
	StringBuilder buffer;

	public static void main(String[] args) {
		Game game = new Game();
	}

	Game() {
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		addKeyListener(this);
		add(draw);
		// pack();
		
		// Add restart button that appears when someone has won
		JButton retryButton = new JButton();
		setVisible(true);
		
		
		moveTimer = new Timer(gameSpeed, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (isWon) {
					return;
				}

				// Do not change order! (if you do, Y value of the one below ball.move() will be
				// ahead of the one above it)
				bot.move();
				player.move();
				ball.move(player, bot);
				
				
				if (ball.checkHorizontalWallCollision()) {
					ball.reset();
					if (ball.getX() < Game.SCREEN_WIDTH / 2) {
						bot.addPoints(1);
					} else if (ball.getX() > SCREEN_WIDTH / 2) {
						player.addPoints(1);
					}
				}

				if (checkWin(player)) {
					isWon = true;
					draw.setWinner(player);
					draw.setLooser(bot);

					// Implement a way to stop the timer
					// ...
				} else if (checkWin(bot)) {
					isWon = true;
					draw.setWinner(bot);
					draw.setLooser(player);
					add(retryButton);
					// Implement a way to stop the timer
					// ...
				}
			}
		});

		repaintTimer = new Timer(1, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				draw.repaint();
			}
		});

		moveTimer.setInitialDelay(500);

	}

	static int genRandomVel(int min, int max) {
		int randomVel;

		randomVel = (int) Math.round(max * Math.random());

		if (randomVel < min)
			randomVel = min;

		if (!randomBoolean())
			randomVel = -randomVel;

		return randomVel;
	}

	void startUpdaters() {
		moveTimer.start();
		repaintTimer.start();
	}

	boolean checkWin(Paddle player) {
		if (player.points >= requiredPointsToWin) {
			return true;
		}
		return false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!gameStarted) {
			gameStarted = true;
			startUpdaters();
		}

		switch (e.getKeyCode()) {

		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;

	// Close app when ":q" pressed
		/*case KeyEvent.VK_Q:
			System.out.println(e.getKeyCode());
			if (lastKeyEvent.getKeyCode() == KeyEvent.VK_PERIOD) {
				System.out.println("VIM exit key");
				System.exit(0);
			}

			break; */ 

	// Close app when "q" pressed
		case KeyEvent.VK_Q:
			System.exit(0);
			break;

		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
		case KeyEvent.VK_K:
			player.upAccel = true;
			bot.upAccel = true;
			break;

		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
		case KeyEvent.VK_J:
			player.downAccel = true;
			bot.downAccel = true;
			break;

		}

		lastKeyEvent = e;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {

		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_J:
			player.downAccel = false;
			bot.downAccel = false;
			break;

		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_K:

			player.upAccel = false;
			bot.upAccel = false;
			break;
		}
	}

	class Draw extends JPanel {
		Graphics2D g;
		Paddle looser, winner;

		Draw() {
			setBackground(Color.black);
			setBounds(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		}

		@Override
		protected void paintComponent(Graphics graphics) {
			super.paintComponent(graphics);

			Graphics2D g = (Graphics2D) graphics;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			this.g = g;

			player.draw(g);
			bot.draw(g);
			ball.draw(g);

			if (!gameStarted) {
				drawMenu(g);
			} else {
				drawPointSystem(g);
			}

			if (isWon) {
				drawWinScreen(g, winner, looser);
			}

		}

		void setLooser(Paddle looser) {
			this.looser = looser;
		}

		void setWinner(Paddle winner) {
			this.winner = winner;
		}

		void drawMenu(Graphics g) {
			String phrase = "Press any key to start the game";
			g.setColor(Color.blue);
			g.setFont(new Font("Arial", Font.PLAIN, 40));
			g.drawString(phrase, Game.SCREEN_WIDTH / 2 - g.getFontMetrics().stringWidth(phrase) / 2,
					SCREEN_HEIGHT / 2 - g.getFontMetrics().getHeight());
		}

		void drawPointSystem(Graphics2D g) {
			g.setColor(Color.white);
			// g.setStroke(new BasicStroke(10));
			g.drawLine(Game.SCREEN_WIDTH / 2, 0, Game.SCREEN_WIDTH / 2, Game.SCREEN_HEIGHT);
			g.setFont(new Font("Arial", Font.BOLD, 35));
			g.drawString(String.valueOf(player.points), Game.SCREEN_WIDTH / 2 - 40, 40);
			g.drawString(String.valueOf(bot.points), Game.SCREEN_WIDTH / 2 + 40 - getFont().getSize(), 40);
		}

		void drawWinScreen(Graphics2D g, Paddle winner, Paddle looser) {
			int gaps = 80;
			g.setFont(new Font("Arial", Font.BOLD, 50));

			g.setColor(Color.green);
			g.drawString(winner.getName() + " WON!",
					Game.SCREEN_WIDTH / 2 - g.getFontMetrics().stringWidth(winner.getName() + " WON!") / 2, gaps);

			g.setColor(Color.red);
			g.drawString(looser.getName() + " LOST!",
					Game.SCREEN_WIDTH / 2 - g.getFontMetrics().stringWidth(looser.getName() + " LOST!") / 2,
					gaps * 2 + getFont().getSize());

			g.setColor(Color.white);
			g.drawString("Points: " + winner.points + " | " + looser.points,
					Game.SCREEN_WIDTH / 2
							- g.getFontMetrics().stringWidth("Points: " + winner.points + " | " + looser.points) / 2,
					gaps * 3 + getFont().getSize());
			
		}
	}

	public static boolean randomBoolean() {

		if (Math.round(Math.random()) == 1) {
			return true;
		}
		return false;

	}
}
