package com.stevens.tankWar;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
/**
 * game��������
 * @author Fang Bao
 *
 */
public class TankClient extends Frame{
	/**
	 * ��Ϸ������
	 */
	public static final int GAME_WIDTH=800;
	/**
	 * ��Ϸ����߶�
	 */
	public static final int GAME_HEIGHT=600;
	
	private Tank myTank=new Tank(400,550,true,Direction.STOP,this);
	private Wall w1=new Wall(200,300,200,40,this);
	private Wall w2=new Wall(500,130,30,240,this);
	private Blood blood=new Blood(600,500,10,10,this);
	
	private List<Bullet> bullets=new ArrayList<Bullet>();
	private List<Explosion> explosions=new ArrayList<Explosion>();
	private List<Tank> tanks=new ArrayList<Tank>();
	
	private Image offScreenImage;
	
	/**
	 * paint����ʵ�ֶԴ�����������Ļ�
	 */
	public void paint(Graphics g) {
		g.drawString("�����е��ڵ�:"+bullets.size()+"ö", 10, 50);
		g.drawString("���ڲ�����ը:"+explosions.size()+"ö", 10, 70);
		g.drawString("���ео�����:"+tanks.size()+"��", 10, 90);
		g.drawString("����̹��Ѫ��:"+myTank.getLife()+"Ѫ", 10, 110);
		
		int repreducedCount=Integer.parseInt(PropertiesManage.getPro().getProperty("repreducedCount"));
		if(tanks.size()<=0){
			for(int i=0;i<repreducedCount;i++){
				Tank tank=new Tank(90+70*i,50,false,Direction.D,this);
				tanks.add(tank);
			}
		}
		myTank.collidesWithWall(w1);
		myTank.collidesWithWall(w2);
		myTank.collidesWithTanks(tanks);
		myTank.eatBlood(blood);
		myTank.draw(g);
		w1.draw(g);
		w2.draw(g);
		blood.draw(g);
		if(tanks.size()!=0){
			for(int i=0;i<tanks.size();i++)
			{
				Tank tank=tanks.get(i);
				tank.collidesWithWall(w1);
				tank.collidesWithWall(w2);
				tank.collidesWithTanks(tanks);
				
				tank.draw(g);
			}
		}
		if(bullets.size()!=0)
		{	
//			for(Bullet b:bullets){
//				if(!b.isLive())
//					bullets.remove(b);
//				else
//					b.draw(g);
//			}
			
//			for(int i=0;i<bullets.size();i++)
//			{
//				Bullet b=bullets.get(i);
//				if(!b.isLive())
//					bullets.remove(b);
//				else
//					b.draw(g);
//			}
			Iterator<Bullet> iter=bullets.iterator();
			while(iter.hasNext()){
				Bullet b=iter.next();
				b.hitTanks(tanks);
				b.hitTank(myTank);
				b.hitWall(w1);
				b.hitWall(w2);
				if(!b.isLive())				
					iter.remove();
				else
					b.draw(g);
			}			
		}
	
		if(explosions.size()!=0){
			for(int i=0;i<explosions.size();i++){
				Explosion e=explosions.get(i);
				e.draw(g);
			}
		}
	}
	
	
	public List<Tank> getTanks() {
		return tanks;
	}


	public void setTanks(List<Tank> tanks) {
		this.tanks = tanks;
	}


	public List<Bullet> getBullets() {
		return bullets;
	}


	public void setBullets(List<Bullet> bullets) {
		this.bullets = bullets;
	}

	

	public List<Explosion> getExplosions() {
		return explosions;
	}


	public void setExplosions(List<Explosion> explosions) {
		this.explosions = explosions;
	}


	public void update(Graphics g) {
		if(offScreenImage==null)
			offScreenImage=this.createImage(GAME_WIDTH,GAME_HEIGHT);
		Graphics gOffScreen=offScreenImage.getGraphics();
		
		Color c=gOffScreen.getColor();
		gOffScreen.setColor(Color.PINK);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	public void launchFrame(){
		
		int initCount=Integer.parseInt(PropertiesManage.getPro().getProperty("ininTankCount"));
		for(int i=0;i<initCount;i++){
			Tank tank=new Tank(90+70*i,50,false,Direction.D,this);
			tanks.add(tank);
		}
		this.setLocation(200,100);
		this.setSize(GAME_WIDTH,GAME_HEIGHT);
		this.setBackground(Color.GREEN);
		this.setVisible(true);
		this.setTitle("TankWar");
		this.setResizable(false);
		
		this.addKeyListener(new KeyMonitor());
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}			
		}
		);
		new Thread(new PaintThread()).start();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TankClient().launchFrame();
	}
	
	private class KeyMonitor extends KeyAdapter{
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
	}
	private class PaintThread implements Runnable{

		public void run() {
			while(true)
			{
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
