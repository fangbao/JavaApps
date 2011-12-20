package com.stevens.tankWar;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Tank {

	public static final int XSPEED=5;
	public static final int YSPEED=5;
	public static final int WIDTH=30;
	public static final int HIGHT=30;
	
	private TankClient tc;
	private int x;
	private int y;
	private int oldX;
	private int oldY;
	private boolean good;
	private boolean live=true;
	private int life=100;

	private boolean bL;
	private boolean bR;
	private boolean bU;
	private boolean bD;
	//public enum Direction{L,LU,LD,R,RU,RD,U,D,STOP};
	private Direction dir;
	private Direction ptDir=Direction.D;
	
	private static Random r=new Random();
	private int count=0;
	private static Map<String,Image> tankImages=new HashMap<String,Image>();
	private static Toolkit tk=Toolkit.getDefaultToolkit();
	
	static{
		
		tankImages.put("L", tk.getImage(Explosion.class.getClassLoader().getResource("images/tankL.gif")));
		tankImages.put("LU", tk.getImage(Explosion.class.getClassLoader().getResource("images/tankLU.gif")));
		tankImages.put("LD", tk.getImage(Explosion.class.getClassLoader().getResource("images/tankLD.gif")));
		tankImages.put("R", tk.getImage(Explosion.class.getClassLoader().getResource("images/tankR.gif")));
		tankImages.put("RU", tk.getImage(Explosion.class.getClassLoader().getResource("images/tankRU.gif")));
		tankImages.put("RD", tk.getImage(Explosion.class.getClassLoader().getResource("images/tankRD.gif")));
		tankImages.put("U", tk.getImage(Explosion.class.getClassLoader().getResource("images/tankU.gif")));
		tankImages.put("D", tk.getImage(Explosion.class.getClassLoader().getResource("images/tankD.gif")));				
	}
	
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good=good;
	}

	public Tank(int x,int y, boolean good,Direction dir, TankClient tc){
		this(x,y,good);
		this.dir=dir;
		this.tc=tc;
	}
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	
	public boolean isGood() {
		return good;
	}

	public Rectangle getRect(){
		return new Rectangle(x,y,tankImages.get("LU").getWidth(null)+15,tankImages.get("LU").getHeight(null)+15);
	}
	
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public void draw(Graphics g){
		if(!live)
		{
			if(!good)
				tc.getTanks().remove(this);
			return;
		}
		move();
		Color c=g.getColor();
		if(good){
			g.setColor(Color.RED);
			this.drawBlood(g);
		}
			
		g.setColor(c);
		
		switch(ptDir)
		{
			case L:
				g.drawImage(tankImages.get("L"), x, y, null);
				break;
			case LU:
				g.drawImage(tankImages.get("LU"), x, y, null);
				break;
			case LD:
				g.drawImage(tankImages.get("LD"), x, y, null);
				break;
			case R:
				g.drawImage(tankImages.get("R"), x, y, null);
				break;
			case RU:
				g.drawImage(tankImages.get("RU"), x, y, null);
				break;
			case RD:
				g.drawImage(tankImages.get("RD"), x, y, null);
				break;
			case U:
				g.drawImage(tankImages.get("U"), x, y, null);
				break;
			case D:
				g.drawImage(tankImages.get("D"), x, y, null);
				break;				
		}
	}
	
	public void move(){
		this.oldX=x;
		this.oldY=y;
		switch(dir)
		{
			case L:
				x-=XSPEED;
				break;
			case LU:
				x-=XSPEED;
				y-=YSPEED;
				break;
			case LD:
				x-=XSPEED;
				y+=YSPEED;
				break;
			case R:
				x+=XSPEED;
				break;
			case RU:
				x+=XSPEED;
				y-=YSPEED;
				break;
			case RD:
				x+=XSPEED;
				y+=YSPEED;
				break;
			case U:
				y-=YSPEED;
				break;
			case D:
				y+=YSPEED;
				break;
			case STOP:
				break;				
		}
		if(this.dir!=Direction.STOP)
			this.ptDir=dir;
		
		if(x<0) 
			x=0;
		if(y<30)
			y=30;
		if(x+Tank.WIDTH>TankClient.GAME_WIDTH) 
			x=TankClient.GAME_WIDTH-Tank.WIDTH;
		if(y+Tank.HIGHT>TankClient.GAME_HEIGHT)
			y=TankClient.GAME_HEIGHT-Tank.HIGHT;
		
		if(!good){
			Direction []dirs=Direction.values();
			if(count==0){					//count的值，控制更改过方向和打子弹的频率
				count=r.nextInt(12)+5;
				int i=r.nextInt(dirs.length);
				dir=dirs[i];			//敌人更新随机方向
			}
			count--;
			if(r.nextInt(20)>18)
			{
				Bullet b=shoot();
				if(b!=null)
					tc.getBullets().add(b);
			}
		}
	}
	
	public void stay(){
		this.x=this.oldX;
		this.y=this.oldY;
	}
	public void keyPressed(KeyEvent e){
		int key= e.getKeyCode();
		switch(key){
			case KeyEvent.VK_LEFT:
				bL=true;
				break;
			case KeyEvent.VK_RIGHT:
				bR=true;
				break;
			case KeyEvent.VK_UP:
				bU=true;
				break;
			case KeyEvent.VK_DOWN:
				bD=true;
				break;
		}
		locateDirection();
	}
	
	public void keyReleased(KeyEvent e) {
		int key= e.getKeyCode();
		switch(key){
			case KeyEvent.VK_LEFT:
				bL=false;
				break;
			case KeyEvent.VK_RIGHT:
				bR=false;
				break;
			case KeyEvent.VK_UP:
				bU=false;
				break;
			case KeyEvent.VK_DOWN:
				bD=false;
				break;
			case KeyEvent.VK_CONTROL:
				Bullet b=shoot();
				if(b!=null)
					tc.getBullets().add(b);
				break;
			case KeyEvent.VK_A:
				superShoot();
				break;
			case KeyEvent.VK_F2:
				this.live=true;
				this.life=100;
		}
		locateDirection();		
	}
	
	public void locateDirection(){
		if(bL&&!bR&&!bU&&!bD)
			dir=Direction.L;
		else if(bL&&!bR&&bU&&!bD)
			dir=Direction.LU;
		else if(bL&&!bR&&!bU&&bD)
			dir=Direction.LD;
		else if(!bL&&bR&&!bU&&!bD)
			dir=Direction.R;
		else if(!bL&&bR&&bU&&!bD)
			dir=Direction.RU;
		else if(!bL&&bR&&!bU&&bD)
			dir=Direction.RD;
		else if(!bL&&!bR&&bU&&!bD)
			dir=Direction.U;
		else if(!bL&&!bR&&!bU&&bD)
			dir=Direction.D;
		else if(!bL&&!bR&&!bU&&!bD)
			dir=Direction.STOP;		
	}

	public Bullet shoot(){
		if(!live)
			return null;
		int x=this.x+Tank.WIDTH/2-Bullet.WIDTH/2;
		int y=this.y+Tank.HIGHT/2-Bullet.HIGHT/2;
		Bullet b= new Bullet(x,y,good,ptDir,this.tc);
		return b;			
	}
	
	public Bullet shoot(Direction dir){
		if(!live)
			return null;
		int x=this.x+Tank.WIDTH/2-Bullet.WIDTH/2;
		int y=this.y+Tank.HIGHT/2-Bullet.HIGHT/2;
		Bullet b= new Bullet(x,y,good,dir,this.tc);
		return b;		
	}
	public void superShoot(){
		if(!this.isLive())
			return;
		Direction dirs[]= Direction.values();
		for(int i=0;i<8;i++)
			tc.getBullets().add(shoot(dirs[i]));
	}
	public boolean collidesWithWall(Wall wall){
		if(this.getRect().intersects(wall.getRect()))
		{
			stay();
			return true;
		}
		return false;
	}
	
	
	public boolean collidesWithTanks(List<Tank> tanks){
		for(Tank tank:tanks){
			if(this!=tank)
				if(this.live&&tank.isLive()&&this.getRect().intersects(tank.getRect()))
				{
					this.stay();
					tank.stay();
					return true;
				}
		}
		return false;
	}

	public boolean eatBlood(Blood blood){
		if(blood.isLive()&&this.live&&this.getRect().intersects(blood.getRect()))
		{
			this.life=100;
			blood.setLive(false);
			return true;
		}
		return false;
	}
	public void drawBlood(Graphics g){
		Color c=g.getColor();
		g.drawRect(x+5, y-8, Tank.WIDTH, 5);
		int w=Tank.WIDTH*life/100;
		g.fillRect(x+5, y-8, w, 5);
		g.setColor(c);
	}
}
