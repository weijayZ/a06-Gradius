import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.stream.Stream;
import javax.swing.JComponent;
import javax.swing.Timer;

//import javafx.scene.shape.Rectangle;

@SuppressWarnings("serial")
public class GradiusComp extends JComponent {

	private final static int GAME_TICK = 1000 / 60;
	private final static int ASTEROID_MAKE_TICK = 1000/4;

	private final static int SHIP_INIT_X = 10;
	private final static int SHIP_INIT_Y = Gradius.HEIGHT/3;
	private final static int SHIP_VEL_BASE = 2;
	private final static int SHIP_VEL_FAST = 4;
	//My variables
	private Ship ship;
	private Timer[] gameTick = new Timer[4];
	private Collection<Asteroid> aList;
	private Collection<Laser> lList;
	public  static Rectangle viewBound;
	private boolean end = false;
	private int score = 0;
	private int speed = 1;
	private int hScore = 0;
	private int health = 1000;
	private int boost = 1000;
	private int shotCooldown = 0;

	//Constructs parts of game component
	public GradiusComp() {
		viewBound = new Rectangle(getX(), getY(), getWidth(),getHeight());
		gameTick[0] = new Timer(1000/60, (a) -> {update();}); //timer that updats the game 60 times in 1 second
		gameTick[1] = new Timer(1000/((speed*speed)*4), (a) -> {makeAsteroids(speed);}); // timer that makes asteroids 
		gameTick[2] = new Timer(1000/5, (a) -> {score++;healer();});
		gameTick[3] = new Timer(10000, (a -> {speed++;}));
		addKeyListener(new ShipKeyListener());
		aList = new HashSet<Asteroid>();
		lList = new ArrayList<Laser>();

	}

	//generates health over time slowly
	private void healer(){
		if(health < 1000){
			health += 2;
		}

		if(boost < 1000){
			boost += 15;
		}

		if(shotCooldown < 100){
			shotCooldown++;
		}
	}
	//adds asteroids to the aList hashset
	private void makeAsteroids(int spd){
		aList.add(AsteroidFactory.getInstance().makeAsteroid(spd));
	}

	private void makeLaser(){
		lList.add(LaserFactory.getInstance().makeLaser((int)ship.getShape().getBounds().getX(), (int)ship.getShape().getBounds().getY()));
	}
	//update that moves, removes and checks ships and asteroids
	private void update(){
		requestFocusInWindow();
		ship.move();
		aList.parallelStream()
			.forEach(o -> o.move());
		lList.parallelStream()
			.forEach(o -> o.move());
		aList.removeIf(i -> i.isOutOfBounds() == true);
		lList.removeIf(i -> i.isOutOfBounds() == true);  
		
		for (Asteroid a : aList){
			if(a.intersects(ship) && ship.intersects(a)){
				health -= speed*10;
			}
		}
		for(Laser l : lList){
			aList.removeIf(i -> i.intersects(l) == true);
		}

		if(health <= 0){
			end = true;
		}
		repaint();
	}

	//Draw the score
	public void stats(Graphics2D g){
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g.setColor(Color.RED);
		g.fill(new Rectangle2D.Double(0, 2, health/3, 15));
		g.fill(new Rectangle2D.Double(0, 18, boost/4, 15));
		
		g.drawString("LEVEL: " + Integer.toString(speed), SHIP_INIT_X, getHeight()-29);
		g.drawString("SCORE: " + Integer.toString(score), SHIP_INIT_X, getHeight()-10);
		g.drawString("HIGHSCORE: " + Integer.toString(hScore), getWidth()*3/4  + 30, getHeight()-10);

		g.setColor(Color.WHITE);
		g.setFont(new Font("TimesRoman", Font.BOLD, 13));
		g.drawString("HEALTH: " + Integer.toString(health) +"/10000", 5, 13);
		g.drawString("BOOST", 5, 30);
		g.fill(new Rectangle2D.Double(0, 33, shotCooldown, 3));
	}

	//Draw method for end game
	public void gameOver(Graphics2D g){
		Arrays.stream(gameTick)
		.forEach(a -> a.stop());
		g.setColor(Color.BLACK);
		g.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
		g.setFont(new Font("Imapct", Font.BOLD, 60));
		g.setColor(Color.RED);
		g.drawString("GAME OVER!", (getWidth()/4 + 50), getHeight()/2);
		g.setFont(new Font("TimesRoman", Font.BOLD, 30));
		g.drawString("STATISTICS:", (getWidth()/3 + 60), getHeight()/2 + 40);
		g.drawString("LEVEL: " + Integer.toString(speed), (getWidth()/3 + 60), getHeight()/2 + 70);
		g.drawString("SCORE: " + Integer.toString(score), (getWidth()/3 + 60), getHeight()/2 + 100);
		if(score > hScore){
			hScore = score;
			g.drawString("NEW HIGHSCORE!!!!", (getWidth()/3), getHeight()/2 + 130);
		}
		g.drawString("Ready PLAYER1?   Y/N", (getWidth()/3), getHeight()-10);
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paintComponent(g2);
	}

	//Paints the objects onto the frame
	private void paintComponent(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
		ship.draw(g2);
		aList.stream()
			.forEach(a -> a.draw(g2));
		lList.stream()
			.forEach(a -> a.draw(g2));
		stats(g2);
		if(end){
			gameOver(g2);
		}
	}

	//Constucts ship, sets teh bounds for asteroids and starts all timers
	public void start() {
		ship  = new ShipImpl(SHIP_INIT_X, SHIP_INIT_Y, new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight()));
		AsteroidFactory.getInstance().setStartBounds(new Rectangle(getWidth(), getY(), getWidth(),getHeight()+50));
		AsteroidFactory.getInstance().setMoveBounds(new Rectangle(getX(), getY()-100, getWidth(),getHeight()+100));
		LaserFactory.getInstance().setMoveBounds(new Rectangle(getX(), getY(), getWidth()+30,getHeight()));
		Arrays.stream(gameTick)
			.forEach(a -> a.start());
	}

	//resets the game
	public void reset(){
		end = false;
		score = 0;
		speed = 1;
		health = 1000;
		boost = 1000;
		shotCooldown = 0;
		aList.clear();
		start();
		repaint();

	}

	//Listener that checks key presses
	private class ShipKeyListener extends KeyAdapter {
		private boolean up;
		private boolean down;
		private boolean left;
		private boolean right;

		private void setVelocity(KeyEvent e) {
			final int dp;
			
			setDirection(e);
			if(e.isShiftDown() && boost > 0){
				dp = 8;
				boost-= 100;
			} else {
				dp = 3;
			}

			int dx = 0;
			int dy = 0;

			if(up && !down) {
				dy = -dp;
			} else if(down && !up) {
				dy = dp;
			}
			if(left && !right) {
				dx = -dp;
			} else if(right && !left) {
				dx = dp;
			}
			ship.setVelocity(dx, dy);
		}

		private void setDirection(KeyEvent e) {
			final boolean state;
			switch (e.getID()) {
				case KeyEvent.KEY_PRESSED: state = true; break;
				case KeyEvent.KEY_RELEASED: state = false; break;
				default: return;
			}
			switch (e.getKeyCode()) {
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
				case KeyEvent.VK_KP_UP:
					up = state;
					break;
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_KP_DOWN:
					down = state;
					break;
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_KP_LEFT:
					left = state;
					break;
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_KP_RIGHT:
					right = state;
					break;
				case KeyEvent.VK_SPACE:
					if(end == false && shotCooldown > 0){
					makeLaser();
					shotCooldown -= 5;
					}
					break;
				case KeyEvent.VK_Y:
					if(end){
						reset();
					}
					break;
				case KeyEvent.VK_N:
					if(end){
						System.exit(0);
					}
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			setVelocity(e);
		}
		@Override
		public void keyReleased(KeyEvent e) {
			setVelocity(e);
		}
	}
}
