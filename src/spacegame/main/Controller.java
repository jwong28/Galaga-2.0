package spacegame.main;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;

import spacegame.main.classes.EntityA;
import spacegame.main.classes.EntityB;

public class Controller {

	private LinkedList<EntityA> ea = new LinkedList<EntityA>();
	private LinkedList<EntityB> eb = new LinkedList<EntityB>();
	EntityA enta;
	EntityB entb;
	private Textures tex;
	Random r = new Random();
	private Game game;
	
	
	public Controller(Textures tex, Game game)
	{
		this.tex = tex;
		this.game = game;
	}
	
	public void createEnemy(int enemycount)
	{
		for (int i=0; i< enemycount; i++)
		{
			addEntity(new Enemy(r.nextInt(640), -10, tex, game, this));
		}
	}
	
	public void tick()
	{
		//A
		for(int i=0; i <ea.size();i++)
		{
			enta = ea.get(i);
			enta.tick();
		}
		//B
		for(int i=0; i <eb.size();i++)
		{
			entb = eb.get(i);
			entb.tick();
		}		
		
	}
	
	public void render(Graphics g)
	{
		//A
		for(int i=0; i <ea.size();i++)
		{
			enta = ea.get(i);
			enta.render(g);
		}
		//B
		for(int i=0; i <eb.size();i++)
		{
			entb = eb.get(i);
			entb.render(g);
		}
	}
	
	public void addEntity(EntityA ent)
	{
		ea.add(ent);
	}
	public void removeEntity(EntityA ent)
	{
		ea.remove(ent);
	}
	public void addEntity(EntityB ent)
	{
		eb.add(ent);
	}
	public void removeEntity(EntityB ent)
	{
		eb.remove(ent);
	}
	
	public LinkedList<EntityA>getEntityA()
	{
		return ea;
	}
	public LinkedList<EntityB>getEntityB()
	{
		return eb;
	}
	
}
