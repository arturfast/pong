package pong;

import java.awt.Graphics2D;

public abstract class Paddle {
	int x, y, width = 35, height;
	boolean upAccel = false;
	boolean downAccel = false;
	int accel = 1;
	double yVel = 0.0;
	final double GRAVITY = 0;
	byte points = 0;
	int maxYVel = 8;
	int topBorder = 0;
	int bottomBorder = Game.SCREEN_HEIGHT;
	String name;

	Paddle() {
		setWidth(35);
		setHeight(90);
	}

	abstract void draw(Graphics2D g);

	void move() {
		if (upAccel) {
			yVel -= accel;
		} else if (downAccel) {
			yVel += accel;
		} else if (!upAccel && !downAccel) {
			if ((int) yVel != 0) {
				yVel *= GRAVITY;
			}
		}

		if (yVel > getMaxYVel()) {
			yVel = getMaxYVel();
		} else if (yVel < -getMaxYVel()) {
			yVel = -getMaxYVel();
		}

		y += (int) yVel;

		checkAndFixWallCollision();
	}

	void checkAndFixWallCollision() {
		if (y <= 0) {
			y = 0;
			yVel = 0;
		} else if (y + getHeight() >= Game.SCREEN_HEIGHT) {
			y = (int) (Game.SCREEN_HEIGHT - getHeight());
		}
	}

	boolean checkAndFixWallCollision(int topBorder, int bottomBorder) {
		if (y <= topBorder) {
			y = topBorder;
			yVel = 0;
			return true;
		} else if (y + getHeight() >= bottomBorder) {
			y = (int) (bottomBorder - getHeight());
			return true;
		}
		return false;
	}

	boolean checkAndFixBottomCollision(int bottomBorder) {
		if (y + getHeight() >= bottomBorder) {
			y = (int) (bottomBorder - getHeight());
			yVel = 0;
			return true;
		}

		return false;
	}

	boolean checkAndFixTopCollision(int topBorder) {
		if (y <= topBorder) {
			y = topBorder;
			yVel = 0;
			return true;
		}
		return false;
	}

	public void addPoints(int input) {
		points += input;
	}

	void setX(int input) {
		x = input;
	}

	void setY(int d) {
		y = d;
	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	void setWidth(int newWidth) {
		width = newWidth;
	}

	void setHeight(int newHeight) {
		height = newHeight;
	}

	int getWidth() {
		return width;
	}

	int getHeight() {
		return height;
	}

	void setName(String input) {
		name = input;
	}

	String getName() {
		return name;
	}

	int getMaxYVel() {
		return maxYVel;
	}

	double getYVel() {
		return yVel;
	}

	void setTopBorder(int top) {
		topBorder = top;
	}

	void setBottomBorder(int bottom) {
		bottomBorder = bottom;
	}
}
