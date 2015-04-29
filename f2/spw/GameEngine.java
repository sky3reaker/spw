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
	private ArrayList<Item> item = new ArrayList<Item>();
	private ArrayList<Bullet> bullet = new ArrayList<Bullet>();
	private SpaceShip v;	
	
	private Timer timer;
	
	private long score = 0;
	private int hp = 100;
	private int count = 0;
	private double difficulty = 0.3;
	private double chance = 0.001;
	private	double genNewEnemy = 0.3;
	private int life = 1;
	
	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;		
		
		gp.sprites.add(v);
		
		timer = new Timer(50, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();
				processbullet();
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
	//----------------newEnemy-----------------------/
	
	private void generateNewEnemy(){
		NewEnemy ne = new NewEnemy((int)(Math.random()*350), 30);
		gp.sprites.add(ne);
		newenemies.add(ne);
	}
	
	//----------------item-----------------------/
	
	private void generateItem(){
		Item tem = new Item((int)(Math.random()*350), 30);
		gp.sprites.add(tem);
		item.add(tem);
		
	}
	
	//----------------bullet-----------------------/
	
	private void generateBullet(){
		Bullet b = new Bullet((v.x)+ (v.width/2),v.y);
		gp.sprites.add(b);
		bullet.add(b);
	}
	
	private void processbullet(){	

		Iterator<Bullet> b_iter_bullet = bullet.iterator();
		while(b_iter_bullet.hasNext()){
			Bullet b = b_iter_bullet.next();
			b.proceed();
			if(!b.isAlive()){
				b_iter_bullet.remove();
				gp.sprites.remove(b);
			}
		}
		
	}
	
	private void process(){
		if(Math.random() < difficulty)
			generateEnemy();
	//----------------item-----------------------/
		if(Math.random() < chance)
			generateItem();
	//----------------newEnemy-----------------------/
		if(Math.random() < genNewEnemy)
			generateNewEnemy();
		
		
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
	//----------------newEnemy-----------------------/
		
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
	//----------------item-----------------------/
		
		Iterator<Item> e_iter_item = item.iterator();
		while(e_iter_item.hasNext()){
			Item tem = e_iter_item.next();
			tem.proceed();
			if(!tem.isAlive()){
				e_iter_item.remove();
				gp.sprites.remove(tem);
			}
		}
	
		gp.updateGameUI(this);
		gp.boxhp(hp,count);
		
		Rectangle2D.Double vrnew = v.getRectangle();
		Rectangle2D.Double ernew;
		Rectangle2D.Double er;
		Rectangle2D.Double er_item;
		Rectangle2D.Double br;
		
		//----------------item-----------------------/
		for(Item tem : item){
			er_item = tem.getRectangle();
			if(er_item.intersects(vrnew)){
				refillHp();
				tem.checkAlive();
				return;
			}
		}
		
		//----------------newEnemy-----------------------/
		
		for(NewEnemy ne : newenemies){
			ernew = ne.getRectangle();
			for(Bullet b : bullet){
				br = b.getRectangle();
				if(br.intersects(ernew)){
					score += 50;
					gp.sprites.remove(b);
					b.checkAlive();
					ne.checkAlive();
				}
			if(ernew.intersects(vrnew)){
				hp -= 10;
				count++;
				ne.checkAlive();
				if(hp == 0){
					if(life == 0){
						die();
					}
					life -= 1;
					reSethp();
				}
				return;
			}}
		}
		
		//-----------------Enemy--------------------/
		
		for(Enemy e : enemies){
			er = e.getRectangle();
			for(Bullet b : bullet){
				br = b.getRectangle();
				if(br.intersects(er)){
					score += 50;
					gp.sprites.remove(b);
					b.checkAlive();
					e.checkAlive();
				}
				if(er.intersects(vrnew)){
				hp -= 10;
				count++;
				e.checkAlive();
				if(hp == 0){
					if(life == 0){
						die();}
						life -= 1;
						reSethp();
				}
				return;
			}}
		}
		
		gp.updateGameUI(this);
		gp.boxhp(hp,count);
		
	}
	
	public void die(){
		gp.updateGameUI(this);
		gp.boxhp(hp,count);
		timer.stop();
		//MyForm();
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
		case KeyEvent.VK_SPACE:
			generateBullet();
			break;
		}
	}
	
	public void reSethp(){
		hp = 100;
		count = 0;
		
	}
	
	public void refillHp(){
		if(hp >= 70 || count >= 3){
			reSethp();
		}
		else{
			hp += 30;
			count -= 3;
		}
	}
	
	
	
	public long getScore(){
		return score;
	}
	public int getHp(){
		return hp;
	}
	public int getLife(){
		return life;
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
