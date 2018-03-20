import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;

public class LaserImpl  extends SpriteImpl implements Ship {

	private final static Color FILL = Color.RED;
	private final static Color BORDER = Color.RED;

	private final static int HEIGHT = 20;
	private final static int WIDTH = HEIGHT;

	//super constructor
	public LaserImpl(int x, int y, Rectangle2D moveBounds) {
		super(new Rectangle2D.Double(x, y, 5, 2), moveBounds, true, BORDER, FILL);
		
	}
}
