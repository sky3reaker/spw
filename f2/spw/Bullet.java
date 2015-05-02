package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet extends Sprite{
	
	public static final int begin = 0;
	
	private int step = 12;
	private boolean alive = true;
	
	public Bullet(int x, int y) {
		super(x, y, 5, 7);
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(x, y, width, height);
	}

	public void proceed(){
		y -= step;
		if(y < begin){
			alive = false;
		}
	}
	
	public boolean isAlive(){
		return alive;
	}
	
public void checkAlive(){
		alive = false;
}
}