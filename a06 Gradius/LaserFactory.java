import java.awt.Rectangle;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import java.awt.Polygon;

public class LaserFactory {

	private final static LaserFactory instance = new LaserFactory();

	private static Rectangle startBounds;
	private static Rectangle moveBounds;

	private LaserFactory() {}

	public static LaserFactory getInstance() {
		return instance;
	}
	
	//Sets move bounds to be the union of frame
	public void setMoveBounds(Rectangle r) {
		moveBounds = r;
	}

	//Returns Lasers made randomly in startbound with random sizes
	public Laser makeLaser(int x, int y) {
		return new LaserImpl(x+10, y+9, 15, 2, 10);
	}

	//Class that constructs the Laser Sprites
	private static class LaserImpl extends SpriteImpl implements Laser {
		
		public LaserImpl(int x, int y, int w, int h, float v) {
			super(new Rectangle(x, y, w, h), moveBounds, true, Color.RED, Color.RED);
			super.setVelocity(v, 0);
			// TODO
			
		}
	}
}
