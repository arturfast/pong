package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Player extends Paddle {
	
	Player() {
		super();
		setX(10);
		setY(Game.SCREEN_HEIGHT / 2 - (getHeight() / 2));
		setName("Player 1");
	}
	
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.white);
		g.fillRect(x, y, width, height);

	}
}
