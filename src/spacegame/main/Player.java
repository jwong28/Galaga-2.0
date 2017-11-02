package spacegame.main;

import java.awt.Graphics;
import java.awt.Rectangle;

import spacegame.main.classes.EntityA;
import spacegame.main.classes.EntityB;
import spacegame.main.libs.Animation;


public class Player  extends GameObject implements EntityA
{

	
	private double velx;
	private double vely;
	
	private Textures tex;
	
	Animation anim;
	Game game;
	Controller c;
	
	public Player (double x, double y, Textures tex, Game game, Controller c )
	{
		super(x,y);
		this.tex = tex;
		this.game = game;
		this.c = c;
		
		anim = new Animation(5, tex.player[0], tex.player[1], tex.player[2]);
	}
	
	public void tick()
	{
		x += velx;
		y += vely;
		
		if(x<=0)
			x=0;
		if(x>=640 - 22)
			x=640 - 22;
		if(y<=0)
			y=0;
		if(y>=480 - 32)
			y=480 - 32;
		
		for(int i = 0; i<game.eb.size();i++)
		{
			EntityB tempEnt = game.eb.get(i);
			
			if(Physics.Collision(this, tempEnt))
			{
				c.removeEntity(tempEnt);
				Game.health -= 20;
				game.setEnemykilled(game.getEnemykilled() + 1);

			}
		}
		
		anim.runAnimation();
	}
	
	public void render(Graphics g)
	{
		anim.drawAnimation(g, x, y, 0);
	}
	
	public double getx()
	{
		return x;
	}
	
	public double gety()
	{
		return y;
	}
	
	public void setx(double x)
	{
		this.x = x;
	}
	
	public void sety(double y)
	{
		this.y = y;
	}
	
	public void setvelx(double velx)
	{
		this.velx = velx;
	}
	
	public void setvely(double vely)
	{
		this.vely = vely;
	}

	public Rectangle getBounds() {	
		return new Rectangle((int)x,(int)y,32,32);
	}	
}
