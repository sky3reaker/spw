package f2.spw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Timer;


public class GameEngine implements KeyListener, GameReporter{
	GamePanel gp;
		
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<NewEnemy> newenemies = new ArrayList<NewEnemy>();
	private SpaceShip v;	
	
	private Timer timer;
	
	private long score = 0;
	private int hp = 100;
	private int count = 0;
	private double difficulty = 0.05;
	
	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;		
		
		gp.sprites.add(v);
		
		timer = new Timer(50, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();
				processNew();
			}
		});
		timer.setRepeats(true);
		
	}
	
	public void start(){
		timer.start();
	}
	
	private void generateEnemy(){
		Enemy e = new Enemy((int)(Math.random()*350), 30);
		gp.sprites.add(e);
		enemies.add(e);
	}
	
	private void process(){
		if(Math.random() < difficulty){
			generateEnemy();
		}
		
		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();

			
			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
				score += 500;
			}
		}
		
		
		gp.updateGameUI(this);
		gp.boxhp(hp,count);
		
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		for(Enemy e : enemies){
			er = e.getRectangle();
			if(er.intersects(vr)){
				hp -= 10;
				count++;
				if(hp == 0)
					die();
				return;
			}
		}
		
		gp.updateGameUI(this);
		gp.boxhp(hp,count);
	}

		private void generateNewEnemy(){
		NewEnemy ne = new NewEnemy((int)(Math.random()*350), 30);
		gp.sprites.add(ne);
		newenemies.add(ne);
	}
		
		private void processNew(){
		if(Math.random() < difficulty){
			generateNewEnemy();
		}
		
		Iterator<NewEnemy> e_iter_New = newenemies.iterator();
		while(e_iter_New.hasNext()){
			NewEnemy ne = e_iter_New.next();
			ne.proceed();

			
			if(!ne.isAlive()){
				e_iter_New.remove();
				gp.sprites.remove(ne);
				score += 5000;
			}
		}
		
		gp.updateGameUI(this);
		gp.boxhp(hp,count);
		
		Rectangle2D.Double vrnew = v.getRectangle();
		Rectangle2D.Double ernew;
		for(NewEnemy ne : newenemies){
			ernew = ne.getRectangle();
			if(ernew.intersects(vrnew)){
				hp -= 10;
				count++;
				if(hp == 0)
					die();
				return;
			}
		}
		gp.updateGameUI(this);
		gp.boxhp(hp,count);
		
	}
	
	public void die(){
		gp.updateGameUI(this);
		gp.boxhp(hp,count);
		timer.stop();
	}
	
	void controlVehicle(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			v.move(-1);
			break;
		case KeyEvent.VK_RIGHT:
			v.move(1);
			break;
		case KeyEvent.VK_UP:
			v.move_u(-1);
			break;
		case KeyEvent.VK_DOWN:
			v.move_u(1);
			break;
		case KeyEvent.VK_D:
			difficulty += 0.1;
			break;
		}
	}

	public long getScore(){
		return score;
	}
	public int getHp(){
		return hp;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		controlVehicle(e);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//do nothing
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//do nothing		
	}
}
