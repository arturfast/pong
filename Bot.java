package pong;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bot extends Paddle {
	
	Bot() {
		super();
		setX(Game.SCREEN_WIDTH - getWidth() - 10);
		setY(Game.SCREEN_HEIGHT / 2 - getHeight() / 2);
		//setX(Game.SCREEN_WIDTH - getWidth() - 50);
		setName("Bot");
	}
	
	@Override
	void draw(Graphics2D g) {
		g.setColor(Color.white);
		g.fillRect(x, y, width, height);
	}
	
}
