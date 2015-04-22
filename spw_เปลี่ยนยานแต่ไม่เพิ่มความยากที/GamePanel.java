package f2.spw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
		big.drawString(String.format("%08d", reporter.getScore()), 300, 20);
		big.drawString(String.format("HP = %08d", reporter.getHp()), 150, 20);
		for(Sprite s : sprites){
			s.draw(big);
		}
		
		repaint();
	}
	
	public void boxhp(int down){
		big.setColor(Color.WHITE);
		big.fillRect(20, 60, 20, 472);
		
		big.setColor(Color.black);
		big.fillRect(21, 64, 18, 464);
		
		big.setColor(Color.blue);
		big.fillRect(21, 64, 18, (down*8)+64);
		
		
		
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(bi, null, 0, 0);
	}

}
