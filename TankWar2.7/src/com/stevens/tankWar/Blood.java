package com.stevens.tankWar;

import java.awt.*;
public class Blood {
	private int x,y,width,height;
	private boolean live=true;
	private TankClient tc;
	private int[][] pos={{680,500},{650,510},{630,530},{630,550},{610,530},{640,520},{670,510}
							};
	private int step=0;
	
	public Blood(int x, int y, int width, int height, TankClient tc) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.tc = tc;
	}
	
	
	public void draw(Graphics g){
		if(!this.live)
			return;
		Color c=g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, width, height);
		g.setColor(c);
		move();
	}
	
	public void move(){
		step++;
		if(step==pos.length)
			step=0;
		x=pos[step][0];
		y=pos[step][1];
	}

	public Rectangle getRect(){
		return new Rectangle(x,y,width,height);
	}
	
	public boolean isLive() {
		return live;
	}


	public void setLive(boolean live) {
		this.live = live;
	}
	
	
}
