package com.stevens.tankWar;

import java.awt.*;
import java.awt.Toolkit;
public class Explosion {
	private int x;
	private int y;
	private boolean live=true;
	private TankClient tc;
	
	private static Toolkit tk=Toolkit.getDefaultToolkit();
	private static Image[] images={
		tk.getImage(Explosion.class.getClassLoader().getResource("images/0.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/1.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/2.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/3.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/4.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/5.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/6.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/7.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/8.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/9.gif")),
		tk.getImage(Explosion.class.getClassLoader().getResource("images/10.gif"))
	};
	
	private int step=0;
	private boolean init;
	
	public Explosion(int x, int y, TankClient tc)
	{
		this.x=x;
		this.y=y;
		this.tc=tc;
	}
	
	public void draw(Graphics g){
		if(!init){
			for (int i = 0; i < images.length; i++) {
				g.drawImage(images[i], -20, -20, null);
			}
			init=true;
		}
		if(!live){
			tc.getExplosions().remove(this);
			return;
		}
		if(step==images.length){
			step=0;
			live=false;
			return;
		}
		g.drawImage(images[step], x, y, null);
		step++;
	}
	
}
