import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;

public class ShipImpl  extends SpriteImpl implements Ship {

	private final static Color FILL = Color.BLACK;
	private final static Color BORDER = Color.RED;

	private final static int HEIGHT = 20;
	private final static int WIDTH = HEIGHT;

	//super constructor
	public ShipImpl(int x, int y, Rectangle2D moveBounds) {
		super(new Polygon( new int[] {x,x,x+WIDTH}, new int[] {y, y+HEIGHT,y+HEIGHT/2}, 3), moveBounds, true, BORDER, FILL);
		
	}
}
