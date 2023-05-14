package pong;

public class CollisionHandler {
	
	CollisionHandler() {
		
	}
	
	boolean checkCollision() {
		
		return false;
	}
	
	boolean checkPaddleCollision(Paddle paddle, Ball ball) {

		if (ball.y <= paddle.getY() + paddle.getHeight() - ball.trim && ball.y >= paddle.getY() + ball.trim
				|| ball.y + ball.getHeight() <= paddle.getY() + paddle.getHeight() - ball.trim
						&& ball.y + ball.getHeight() >= paddle.getY() + ball.trim) {
			if (ball.x >= paddle.getX() && ball.x <= paddle.getX() + paddle.getWidth()
					|| ball.x + ball.getWidth() >= paddle.getX() && ball.x + ball.getWidth() <= paddle.getX() + paddle.getWidth())
				return true;
		}

		return false;
	}

	
}
