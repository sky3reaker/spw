package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Image;

public class NewEnemy extends Sprite{
    public static final int Y_TO_FADE = 400;
    public static final int Y_TO_DIE = 600;

    private int step = 10;
    private boolean alive = true;

    public NewEnemy(int x, int y) {
        super(x, y, 20, 20);

    }

    @Override
    public void draw(Graphics2D g) {

        /*g.setColor(Color.RED);
        g.fillRect(x, y, width, height);*/
		Image img = Toolkit.getDefaultToolkit().getImage("newenemy.png");
		g.drawImage(img, x, y, 20, 20, null);

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