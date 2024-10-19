package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;

public class Ball implements Cloneable {
	int x = (int) Game.SCREEN_WIDTH / 2, y = (int) Game.SCREEN_HEIGHT / 2;
	final int WIDTH = 30, HEIGHT = 30;
	int initalX = Game.SCREEN_WIDTH / 2 - getWidth() / 2;
	int initalY = Game.SCREEN_HEIGHT / 2 - getHeight() / 2;
	int xVel, yVel;
	int trim;
	Ball lastState;

	Ball(Player paddle, Bot bot) {
		reset();

		// Debugging
	//	initalX = Game.SCREEN_WIDTH - getWidth() - 2;
	// 	initalY = Game.SCREEN_HEIGHT - getHeight() - 3;

//		initalX = Game.SCREEN_WIDTH / 2;
		// initalY = Game.SCREEN_HEIGHT / 2 - 100;
		initalX = 1;
		initalY = 1;

		// Debugging
		xVel = 0;
		yVel = 10;

		x = initalX;
		y = initalY;

	}

	void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(x, y, WIDTH, HEIGHT);
	}

	void move(Player player, Bot bot) {
		trim = (int) Math.abs(player.getYVel()) + Math.abs(getYVel()) + 1;
		boolean disabled = false;

		Paddle paddle = player;

		for (int i = 0; i < 2; i++) {

			if (i == 1) {
				paddle = bot;
			}

			if (checkTopCollision(paddle)) {
				// paddle.checkAndFixTopCollision(getHeight());
				System.out.println("\nyVel before: " + yVel);
				System.out.println("Y before: " + (getY() + getHeight()));
				System.out.println("Y-Paddle before: " + paddle.getY());
				System.out.println("yVel-Paddle before: " + paddle.getYVel());
				yVel = yVel + (paddle.getY() - (getY() + getHeight()));
				//y = paddle.getY() - getHeight();
				//yVel = -yVel;
				System.out.println("\nyVel after: " + yVel);
				System.out.println("Y after: " + (getY() + getHeight()));
				System.out.println("Y-Paddle after: " + paddle.getY());
				System.out.println("yVel-Paddle after: " + paddle.getYVel());
//				System.out.println("Y: " + tmp);
//				System.out.println("Padde Y: " + paddle.y);
//				System.out.println("Changed from: " + tmp + " to :; " + y );

			}

			if (checkVerticalCollision() && checkTopCollision(player)) {
				System.out.println("Endeckt");
				int paddletmp = paddle.getY();
				paddle.setY(getHeight());
				int tmp = y;
				y = 0;
				yVel = -yVel;
				System.out.println("Y: " + tmp);
				System.out.println("Paddle changed from: " + paddletmp);
				System.out.println("To; " + paddle.y);
				System.out.println("Changed from: " + tmp + " to :; " + y);
				disabled = true;
			}
			if (checkBottomCollision(paddle)) {
				paddle.checkAndFixBottomCollision(Game.SCREEN_HEIGHT - getHeight());
				y = paddle.getY() + paddle.getHeight();
				yVel = -yVel;
			}

			if (checkPaddleCollision(paddle)) {
				Random rndm = new Random();

				if (yVel < 0) {
					yVel = -(3 + rndm.nextInt(5));
				} else {
					yVel = 3 + rndm.nextInt(5);
				}

				if (xVel < 0) {
					xVel = -(3 + rndm.nextInt(5));
				} else {
					xVel = 3 + rndm.nextInt(5);
				}

				x = lastState.getX();

				xVel = -xVel;
			}

			if (y + getHeight() >= Game.SCREEN_HEIGHT) {
				System.out.println("BottomWallCollision" + " y: " + y + " vel: " + yVel);
				y = Game.SCREEN_HEIGHT - getHeight() - 1;
				yVel = -yVel;
			} else if (y <= 0) {
				System.out.println("TopWallCollision" + " y: " + y + " vel: " + yVel);
				y = 1;
				yVel = -yVel;
			}

		}

		try {
			lastState = (Ball) this.clone();
		} catch (Exception e) {
			System.err.println("RROROR!");
		}

		// System.out.println("NOT BALL " + y);
		// System.out.println("NOT PADDLE " + player.y);
		if (disabled) return;
		x += xVel;
		y += yVel;

	}

	boolean checkVerticalCollision() {
		// Vertical Check
		if (y + getHeight() >= Game.SCREEN_HEIGHT) {
			return true;
		} else if (y <= 0) {
			return true;
		}
		return false;
	}

	public boolean checkVerticalWallCollision(int y) {
		if (y + getHeight() >= Game.SCREEN_HEIGHT) {
			return true;
		} else if (y <= 0) {
			return true;
		}
		return false;
	}

	boolean checkHorizontalPlayerCollision(Paddle player) {

		if (x <= player.getX() + player.getWidth() && y <= player.getY() + player.getHeight() - trim
				&& y >= player.getY()
				|| x <= player.getX() + player.getWidth()
						&& y + getHeight() <= player.getY() + player.getHeight() - trim
						&& y + getHeight() >= player.getY() + trim) {

			return true;
		}
		return false;
	}

	boolean checkPaddleCollision(Paddle paddle) {

		if (y <= paddle.getY() + paddle.getHeight() - trim && y >= paddle.getY() + trim
				|| y + getHeight() <= paddle.getY() + paddle.getHeight() - trim
						&& y + getHeight() >= paddle.getY() + trim) {
			if (x >= paddle.getX() && x <= paddle.getX() + paddle.getWidth()
					|| x + getWidth() >= paddle.getX() && x + getWidth() <= paddle.getX() + paddle.getWidth())
				return true;
		}

		return false;
	}

	boolean checkBotCollision(Paddle bot) {

		if (x + getWidth() >= bot.getX() && y <= bot.getY() + bot.getHeight() - trim && y >= bot.getY() + trim
				|| x + getWidth() >= bot.getX() && y + getHeight() <= bot.getY() + bot.getHeight() - trim
						&& y + getHeight() >= bot.getY() + trim) {
			return true;
		}

		return false;
	}

	boolean checkTopCollision(Paddle paddle) {
		if (y + getHeight() <= paddle.getY() + trim && y + getHeight() >= paddle.getY()) {
			if (x >= paddle.getX() && x <= paddle.getX() + paddle.getWidth()
					|| x + getWidth() >= paddle.getX() && x + getWidth() <= paddle.getX() + paddle.getWidth())
				return true;
		}
		return false;
	}

	boolean checkBottomCollision(Paddle paddle) {

		if (y >= paddle.getY() + paddle.getHeight() - trim && y <= paddle.getY() + paddle.getHeight()) {
			if (x >= paddle.getX() && x <= paddle.getX() + paddle.getWidth()
					|| x + getWidth() >= paddle.getX() && x + getWidth() <= paddle.getX() + paddle.getWidth())
				return true;
		}

		return false;
	}

	boolean checkVerticalWallCollision() {
		if (y + getHeight() >= Game.SCREEN_HEIGHT) {
			return true;
		} else if (y <= 0) {
			return true;
		}

		return false;
	}

	boolean checkHorizontalWallCollision() {

		if (x <= 0) {
			return true;
		} else if (x + WIDTH >= Game.SCREEN_WIDTH) {
			return true;
		}

		return false;
	}

	void reset() {
		x = initalX;
		y = initalY;
		Random rndm = new Random();
		y = getHeight() + 20 + rndm.nextInt(Game.SCREEN_HEIGHT - getHeight() - 20);
		System.out.println("Randomvel y : " + y);

		xVel = Game.genRandomVel(4, 8);
	//	yVel = Game.genRandomVel(4, 8);
		System.out.printf("xVel: %s \nyVel: %s", xVel, yVel);

	}

	int getWidth() {
		return WIDTH;
	}

	int getHeight() {
		return HEIGHT;
	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	int getYVel() {
		return yVel;
	}

}
