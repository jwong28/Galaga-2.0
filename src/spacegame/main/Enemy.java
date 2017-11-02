package spacegame.main;

import java.awt.Graphics; 
import java.awt.Rectangle;
import java.util.Random;

import spacegame.main.classes.EntityA;
import spacegame.main.classes.EntityB;
import spacegame.main.libs.Animation;

public class Enemy extends GameObject implements EntityB {

	Random r = new Random();
	private Textures tex;
	private Game game;
	private Controller c;
	
	private int speed = r.nextInt(3) + 1;
	
	Animation anim;
	
	public Enemy(double x, double y, Textures tex, Game game, Controller c)
	{
		super(x,y);
		this.tex = tex;
		this.game = game;
		this.c = c;
		
		anim = new Animation(5,tex.enemy[0],tex.enemy[1],tex.enemy[2]);
	}
	
	public void tick()
	{
		y +=speed;
		if(y > (Game.height * Game.scale))
		{
			y = 0;
			x = r.nextInt(Game.width * Game.scale);
		}
		
		for(int i = 0; i < game.ea.size(); i++)
		{
			EntityA tempEnt = game.ea.get(i);
			if(Physics.Collision(this, tempEnt))
			{
				c.removeEntity(tempEnt);
				c.removeEntity(this);
				game.setEnemykilled(game.getEnemykilled()+1);
			}
		}
		
		
		
		anim.runAnimation();
	}
	
	public void render(Graphics g)
	{
		anim.drawAnimation(g, x, y, 0);
	}
	
	public double gety()
	{
		return y;
	}
	
	public void sety(double y)
	{
		this.y = y;
	}


	public double getx() {
		return x;
	}

	public Rectangle getBounds() {
		
		return new Rectangle((int)x,(int)y,32,32);
	}
}
