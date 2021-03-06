package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Image;

public class ClearItem extends Sprite{
	public static final int Y_TO_FADE = 400;
	public static final int Y_TO_DIE = 600;
	
	private int step = 12;
	private boolean alive = true;
	
	public ClearItem(int x, int y) {
		super(x, y, 30, 30);
		
	}

	@Override
	public void draw(Graphics2D g) {
		
		Image img = Toolkit.getDefaultToolkit().getImage("warning.png");
		g.drawImage(img, x, y, 30, 30, null);
		
	}

	public void proceed(){
		y += step;
		if(y > Y_TO_DIE){
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