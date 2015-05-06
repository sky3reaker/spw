package f2.spw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Image;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private BufferedImage bi;
	Graphics2D big;
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();

	public GamePanel() {
		bi = new BufferedImage(400, 600, BufferedImage.TYPE_INT_ARGB);
		big = (Graphics2D) bi.getGraphics();
		big.setBackground(Color.BLACK);
	}

	public void updateGameUI(GameReporter reporter){
		big.clearRect(0, 0, 400, 600);
		
		big.setColor(Color.WHITE);	
		big.setFont(new Font("default", Font.PLAIN, 12));	
		big.drawString(String.format("%08d", reporter.getScore()), 300, 30);
		big.drawString(String.format("HP = %08d", reporter.getHp()), 150, 30);
		big.drawString(String.format("= %d", reporter.getLife()), 65, 30);
		Image img = Toolkit.getDefaultToolkit().getImage("heart.png");
		big.drawImage(img, 20, 15,30, 30, null);

		for(Sprite s : sprites){
			s.draw(big);
		}

		
		repaint();
	}
	
	/*public void heart(){
		Image img = Toolkit.getDefaultToolkit().getImage("heart.png");
		big.drawImage(img, 20, 20, this);
		
	}*/
	public void gameOver(GameReporter reporter){
		big.clearRect(0, 0, 400, 600);
		big.setColor(Color.WHITE);	
		big.setFont(new Font("default", Font.BOLD, 38));
		big.drawString("Game Over", 100, 250);
		big.setFont(new Font("default", Font.PLAIN, 20));
		big.drawString(String.format("Your score is :%d",reporter.getScore()), 100, 300);
		big.drawString("Please ENTER to Play again",100,490);
		big.drawString("Please ESC to Exit",100,510);
		repaint();

	}
	public void boxhp(int down, int count){
		big.setColor(Color.WHITE);
		big.fillRect(20, 76, 20, 208);
		
		big.setColor(Color.black);
		big.fillRect(21, 80, 18, 200);
		
		big.setColor(Color.blue);
		big.fillRect(21, down+((-20)+(30*count)),18, 200-(20*count));
		
		
		
	}

	public void btmItem(int type){
		big.setColor(Color.WHITE);
		big.fillRect( 330, 50, 40, 40);

		big.setColor(Color.black);
		big.fillRect( 331, 54, 37, 34);

		if(type==1){
		Image img = Toolkit.getDefaultToolkit().getImage("eeee.png");
		big.drawImage(img, 331, 54, 37, 34, null);

		}else if(type==2){
		Image img = Toolkit.getDefaultToolkit().getImage("warning.png");
		big.drawImage(img, 331, 54, 37, 34, null);

		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(bi, null, 0, 0);
	}

}
