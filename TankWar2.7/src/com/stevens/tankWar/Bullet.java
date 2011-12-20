package com.stevens.tankWar;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Bullet {
	
	public static final int XSPEED=10;
	public static final int YSPEED=10;
	public static final int WIDTH=10;
	public static final int HIGHT=10;
	
	private int x;
	private int y;
	private Direction dir;
	private TankClient tc;
	private boolean live=true;
	private boolean good;
	
	private static Map<String,Image> bulletImages=new HashMap<String,Image>();
	private static Toolkit tk=Toolkit.getDefaultToolkit();
	
	static{
		
		bulletImages.put("L", tk.getImage(Explosion.class.getClassLoader().getResource("images/missileL.gif")));
		bulletImages.put("LU", tk.getImage(Explosion.class.getClassLoader().getResource("images/missileLU.gif")));
		bulletImages.put("LD", tk.getImage(Explosion.class.getClassLoader().getResource("images/missileLD.gif")));
		bulletImages.put("R", tk.getImage(Explosion.class.getClassLoader().getResource("images/missileR.gif")));
		bulletImages.put("RU", tk.getImage(Explosion.class.getClassLoader().getResource("images/missileRU.gif")));
		bulletImages.put("RD", tk.getImage(Explosion.class.getClassLoader().getResource("images/missileRD.gif")));
		bulletImages.put("U", tk.getImage(Explosion.class.getClassLoader().getResource("images/missileU.gif")));
		bulletImages.put("D", tk.getImage(Explosion.class.getClassLoader().getResource("images/missileD.gif")));				
	}
	public Bullet(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public Bullet(int x, int y, boolean good,Direction dir, TankClient tc) {
		this(x,y,dir);
		this.good=good;
		this.tc=tc;
	}


	public void draw(Graphics g){

		move();
		
		if(good)
		{	
			Color c=g.getColor();
			g.setColor(Color.CYAN);
//		else
//			g.setColor(Color.BLACK);
			g.fillOval(x, y, WIDTH, HIGHT);
			g.setColor(c);
			return;
		}
		switch(dir)
		{
			case L:
				g.drawImage(bulletImages.get("L"), x, y, null);
				break;
			case LU:
				g.drawImage(bulletImages.get("LU"), x, y, null);
				break;
			case LD:
				g.drawImage(bulletImages.get("LD"), x, y, null);
				break;
			case R:
				g.drawImage(bulletImages.get("R"), x, y, null);
				break;
			case RU:
				g.drawImage(bulletImages.get("RU"), x, y, null);
				break;
			case RD:
				g.drawImage(bulletImages.get("RD"), x, y, null);
				break;
			case U:
				g.drawImage(bulletImages.get("U"), x, y, null);
				break;
			case D:
				g.drawImage(bulletImages.get("D"), x, y, null);
				break;				
		}
		
	}
	
	public void move(){
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
			}
			if(x<0||y<0||x>TankClient.GAME_WIDTH||y>TankClient.GAME_HEIGHT)
				//tc.getBullets().remove(this);
				live=false;
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
	
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HIGHT);
	}
	
	public boolean hitTank(Tank tank){
		if(this.getRect().intersects(tank.getRect())&&tank.isLive()&&this.good!=tank.isGood())
		{			
			if(tank.isGood()){
				tank.setLife(tank.getLife()-20);
				if(tank.getLife()<=0)
					tank.setLive(false);					
			}
			else
				tank.setLive(false);
			live=false;
			Explosion e=new Explosion(x,y,tc);
			tc.getExplosions().add(e);
			return true;
		}
		return false;			
	}
	
	public boolean hitTanks(List<Tank> tanks){
		for(Tank tank: tanks){
			if(hitTank(tank))
				return true;
		}
		return false;
	}
	
	public boolean hitWall(Wall wall){
		if(this.getRect().intersects(wall.getRect()))
		{
			live=false;
			return true;
		}
		return false;
	}
	
}
