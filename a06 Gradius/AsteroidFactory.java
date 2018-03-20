import java.awt.Rectangle;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import java.awt.Polygon;

public class AsteroidFactory {

	private final static int ASTEROID_SIZE_MIN = 10;
	private final static int ASTEROID_SIZE_MAX = 40+20;
	private final static int ASTEROID_VEL_MIN = 1;
	private final static int ASTEROID_VEL_MAX = 3;

	private final static AsteroidFactory instance = new AsteroidFactory();

	private static Rectangle startBounds;
	private static Rectangle moveBounds;

	private AsteroidFactory() {}

	public static AsteroidFactory getInstance() {
		return instance;
	}
	
	//Sets start bounds
	public void setStartBounds(Rectangle r) {
		startBounds = r;
	}

	//Sets move bounds to be the union of framezie plus a bit to the left
	public void setMoveBounds(Rectangle r) {
		moveBounds = r.union(startBounds);
		moveBounds.translate(-startBounds.width/2, 0);
	}

	//Returns asteroids made randomly in startbound with random sizes
	public Asteroid makeAsteroid(int spd) {
		return new AsteroidImpl(startBounds.width, random(startBounds.y, startBounds.height-50), random(ASTEROID_SIZE_MIN, ASTEROID_SIZE_MAX),  random(ASTEROID_SIZE_MIN, ASTEROID_SIZE_MAX), random(ASTEROID_VEL_MIN,ASTEROID_VEL_MAX + spd));
	}

	//Jeremy's random method
	private static int random(int min, int max) {
		if(max-min == 0) { return min; }
		Random rand = java.util.concurrent.ThreadLocalRandom.current();
		return min + rand.nextInt(max + 1);
	}

	//Class that constructs the Asteroid Sprites
	private static class AsteroidImpl extends SpriteImpl implements Asteroid {
		private final static Color COLOR = Color.DARK_GRAY;
		
		public AsteroidImpl(int x, int y, int w, int h, float v) {
			//super(new Ellipse2D.Float(x, y, w, h), moveBounds, true, Color.DARK_GRAY);
			super(new Polygon( new int[] {x,(x-w/3), x, (x+w/3), (x+2*w/3), (x+w*5/4), (x+w*3/2), (x+w*4/3), (x+w*2/3), (x+w/3)}, 
							new int[] {y,(y+w/2), (y+w), (y+w*6/5), (y+w*5/4), (y+w), (y+w/2), (y-w/5), (y-w/4)}, 9),
							moveBounds, true, Color.RED);
			super.setVelocity(-v, 0);
			// TODO
			
		}
	}
}
