package spacegame.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;

import spacegame.main.classes.EntityA;
import spacegame.main.classes.EntityB;

public class Game extends Canvas implements Runnable 
{

	private static final long serialVersionUID = 1L;
	public static final int width = 320;
	public static int height = width / 12 * 9;
	public static final int scale = 2;
	public final String title = "Space Game";
	
	private boolean running = false;
	private Thread thread;
	
	//buffers loads image before projected
	private BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
	private BufferedImage spriteSheet = null;
	private BufferedImage background = null;
	
	private boolean isShooting = false;
	
	private int enemycount = 1;
	private int enemykilled = 0;
	
	private Player p;
	private Controller c;
	private Textures tex;
	private Menu menu;
	
	public LinkedList<EntityA> ea;
	public LinkedList<EntityB> eb;
	
	public static int health = 100 * 2;
	
	public static enum STATE
	{
		MENU,
		GAME
	};
	
	public static STATE State = STATE.MENU;
	
	public void init()
	{
		requestFocus();
		BufferedImageLoader loader = new BufferedImageLoader();
		try{
			spriteSheet = loader.loadImage("/basic sprite sheet.png");
			background = loader.loadImage("/background.png");
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		this.addKeyListener(new KeyInput(this));
		this.addMouseListener(new MouseInput());
		
		tex = new Textures(this);
		c = new Controller(tex, this);
		p = new Player(200, 200, tex, this,c);
		menu = new Menu();
		
		ea = c.getEntityA();
		eb = c.getEntityB();
		
		c.createEnemy(enemycount);
	}
	
	private synchronized void start()
	//synchronized deals with thread
	{
		if(running)
			return;
		running = true;
		//if called again, then it would exit
		thread = new Thread(this);
		thread.start();
	}
	
	private synchronized void stop()
	{
		if(!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}
	
	public void run ()
	{
		init();
		//game loop
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		while (running)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1)
			{
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer >1000)
			{
				timer += 1000;
				System.out.println(updates + " Ticks, FPS " + frames);
				//resetting it so fps won't gain
				updates = 0;
				frames= 0;
			}
		}
		stop();
	}
	
	private void tick()
	{
		if(State == STATE.GAME)
		{
			p.tick();
			c.tick();
		}
		if(enemykilled >= enemycount)
		{
			enemycount += 2;
			enemykilled = 0;
			c.createEnemy(enemycount);
		}
	}
	
	private void render()
	{
		//buffer strategy
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null)
		{
			createBufferStrategy(3); //3 buffers
			return;
		}
		Graphics g = bs.getDrawGraphics(); //draws out buffers
		//to draw
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		g.drawImage(background, 0, 0, null);
		
		if(State == STATE.GAME)
		{
			p.render(g);
			c.render(g);
		
			g.setColor(Color.gray);
			g.fillRect(5, 5, 200, 25);
			g.setColor(Color.green);
			g.fillRect(5, 5, health, 25);
			g.setColor(Color.white);
			g.drawRect(5, 5, 200, 25);		
		}
		else if(State == STATE.MENU)
		{
			menu.render(g);
		}
		
		g.dispose();
		bs.show();
	}
	
	public void keyPressed (KeyEvent e)
	{
		int key = e.getKeyCode();
		
		if(State == STATE.GAME)
		{
		if(key == KeyEvent.VK_RIGHT)
		{
			p.setvelx(5);
		}else if(key == KeyEvent.VK_LEFT)
		{
			p.setvelx(-5);	
		}else if(key == KeyEvent.VK_UP)
		{
			p.setvely(-5);	
		}else if(key == KeyEvent.VK_DOWN)
		{
			p.setvely(5);	
		}else if(key == KeyEvent.VK_SPACE && !isShooting)
		{
			isShooting = true;
			c.addEntity(new Bullet(p.getx(),p.gety(),tex, this));
		}
		}
	}
	public void keyReleased (KeyEvent e)
	{
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_RIGHT)
		{
			p.setvelx(0);
		}else if(key == KeyEvent.VK_LEFT)
		{
			p.setvelx(0);	
		}else if(key == KeyEvent.VK_UP)
		{
			p.setvely(0);	
		}else if(key == KeyEvent.VK_DOWN)
		{
			p.setvely(0);	
		}else if(key == KeyEvent.VK_SPACE)
		{
			isShooting = false;
		}
	}

	public static void main (String args[])
	{
		Game game = new Game();
		
		game.setPreferredSize(new Dimension(width * scale, height * scale));
		game.setMaximumSize(new Dimension(width * scale, height * scale));
		game.setMinimumSize(new Dimension(width * scale, height * scale));
		
		JFrame frame = new JFrame(game.title);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
	}

	public BufferedImage getSpriteSheet()
	{
		return spriteSheet;
	}
	
	public int getEnemycount() {
		return enemycount;
	}

	public void setEnemycount(int enemycount) {
		this.enemycount = enemycount;
	}

	public int getEnemykilled() {
		return enemykilled;
	}

	public void setEnemykilled(int enemykilled) {
		this.enemykilled = enemykilled;
	}

}
