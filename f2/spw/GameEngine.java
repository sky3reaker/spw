package f2.spw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.WindowEvent;

import javax.swing.Timer;


public class GameEngine implements KeyListener, GameReporter{
	GamePanel gp;
		
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<NewEnemy> newenemies = new ArrayList<NewEnemy>();
	private ArrayList<Item> item = new ArrayList<Item>();
	private ArrayList<Bullet> bullet = new ArrayList<Bullet>();
	private ArrayList<ClearItem> citem = new ArrayList<ClearItem>();
	private SpaceShip v;	
	
	private Timer timer;
	
	private long score = 0;
	private int hp = 100;
	private int count = 0;
	private double difficulty = 0.2;
	private double chance = 0.03;
	private double genc = 0.03;
	private	double genNewEnemy = 0.2;
	private int life = 1;
	private int type_item = 0;
	private boolean have_item = false;

	int type = 0;
	private boolean G_over=false;
	
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


	private void genCitem(){
		ClearItem ci = new ClearItem((int)(Math.random()*350), 30);
		gp.sprites.add(ci);
		citem.add(ci);
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
	//----------------newitem---------------------/
		if(Math.random() < genc)
			genCitem();
		
		
		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();
			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
				//score += 500;
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
				//score += 500;
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
	//-----------------newitem-------------///
		Iterator<ClearItem> e_iter_citem = citem.iterator();
		while(e_iter_citem.hasNext()){
			ClearItem ci = e_iter_citem.next();
			ci.proceed();
			if(!ci.isAlive()){
				e_iter_citem.remove();
				gp.sprites.remove(ci);
			}
		}

	
		gp.updateGameUI(this);
		gp.boxhp(hp,count);
		gp.btmItem(type);
		
		Rectangle2D.Double vrnew = v.getRectangle();
		Rectangle2D.Double ernew;
		Rectangle2D.Double er;
		Rectangle2D.Double er_item;
		Rectangle2D.Double er_citem;
		Rectangle2D.Double br;
		
		//----------------item-----------------------/
		for(Item tem : item){
			er_item = tem.getRectangle();
			if(er_item.intersects(vrnew)){

				if(!have_item){
					type=1;
					have_item = true;
				}else{
					refillHp();
				}
				
				
				tem.checkAlive();
				return;
			}
		}
		//----------------clear_item-----------------------/
		for(ClearItem c : citem){
			er_citem = c.getRectangle();
			if(er_citem.intersects(vrnew)){
				score += 10000;
				if(!have_item){
					type=2;
					have_item = true;
				}else{
					clearen();
				}
				
				c.checkAlive();
				return;
			}
		}
		//----------------newEnemy-----------------------/
		
		for(NewEnemy ne : newenemies){
			ernew = ne.getRectangle();
			vrnew = v.getRectangle();
			for(Bullet b : bullet){
				br = b.getRectangle();
				if(br.intersects(ernew)){
					score += 100;
					gp.sprites.remove(b);
					b.checkAlive();
					ne.checkAlive();
				}
		   }
		   if(ernew.intersects(vrnew)){
				  hp -= 10;
				  count++;
				  score += 500;
				  ne.checkAlive();
				    if(hp == 0){
						if(life == 0){
							die();
						}
					life -= 1;
					reSethp();
				    }
				return;
			    }
		}
		
		//-----------------Enemy--------------------/
		
		for(Enemy e : enemies){
			er = e.getRectangle();
			vrnew = v.getRectangle();
			for(Bullet b : bullet){
				br = b.getRectangle();
				if(br.intersects(er)){
					score += 100;
					gp.sprites.remove(b);
					b.checkAlive();
					e.checkAlive();
				}
		    }
		    if(er.intersects(vrnew)){
				 	 hp -= 10;
				  	count++;
				  	score += 500;
					e.checkAlive();
					if(hp == 0){
						if(life == 0){
							die();
						}
						life -= 1;
						reSethp();
					}
				return;
			    }
		}
		
		gp.updateGameUI(this);
		gp.boxhp(hp,count);
		gp.btmItem(type);
		
	}
	
	public void die(){
		gp.updateGameUI(this);
		gp.boxhp(hp,count);
		life = 1;
		reSethp();
		G_over = true;
		gp.gameOver(this);
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
		case KeyEvent.VK_Q:
			if(have_item){
				if(type==1){
					refillHp();
				}else if(type==2){
					clearen();
				}
				type=0;
				have_item=false;
			}

			break;
		case KeyEvent.VK_ENTER:
			if(G_over){
				playAg();
			}

			break;
		case KeyEvent.VK_ESCAPE:
			if(G_over){
				Main.frame.dispatchEvent(new WindowEvent(Main.frame, WindowEvent.WINDOW_CLOSING));
			}

			break;
			
		}
	}
	
	public void reSethp(){
		hp = 100;
		count = 0;

		
	}
	
	public void playAg(){
		life = 1;
		G_over = false;
		score = 0;
		type = 0;
		have_item = false;
		clearen();
		v.setPosition();
		gp.updateGameUI(this);
		gp.boxhp(hp,count);
		timer.start();
		
	}

	public void refillHp(){
		//if(hp >= 70 || count >= 3){
			reSethp();
		//}
		/*else{
			hp += 30;
			count -= 3;
		}*/
	}
	
	public void clearen(){
		for(NewEnemy ne : newenemies){
			ne.checkAlive();
		}
		for(Enemy e : enemies){
			e.checkAlive();
		}
		for(ClearItem c : citem){
			c.checkAlive();
		}
		for(Item tem : item){
			tem.checkAlive();
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
