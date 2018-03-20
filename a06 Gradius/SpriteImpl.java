import java.awt.*;
import java.awt.geom.*;

public abstract class SpriteImpl implements Sprite {

	// drawing
	private Shape shape;
	private final Color border;
	private final Color fill;

	// movement
	private float dx, dy;
	private final Rectangle2D bounds;
	private final boolean isBoundsEnforced;

	//Constructs the ship sprite
	protected SpriteImpl(Shape shape, Rectangle2D bounds, boolean boundsEnforced, Color border, Color fill) {
		this.shape = shape;
		this.bounds = bounds;
		this.isBoundsEnforced = boundsEnforced;
		this.border = border;
		this.fill = fill;
	}

	//constructs the asteroid sprite
	protected SpriteImpl(Shape shape, Rectangle2D bounds, boolean boundsEnforced, Color fill) {
		this(shape, bounds, boundsEnforced, fill, Color.BLACK);
	}

	//method to get shape
	public Shape getShape() {
		return shape;
	}

	//sets the distance moved
	public void setVelocity(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}

	//Moves the sprites and checks if move is valid. Places back to before spot if not valid
	public void move() {
		shape =  AffineTransform.getTranslateInstance(dx, dy).createTransformedShape(shape);
		if(!isInBounds()){
			shape =  AffineTransform.getTranslateInstance(-1*dx, -1*dy).createTransformedShape(shape);
		} 
	}

	//returns true when object is not in frame
	public boolean isOutOfBounds() {
		if(shape.intersects(0, 0, bounds.getWidth()-30, Gradius.HEIGHT)){
			return false;
		} else{
			//System.out.println("DELETED!");
			return true;

		}
	}

	public boolean isInBounds() {
		return isInBounds(bounds, shape);
	}

	//returns true if shape is contained by frame
	private static boolean isInBounds(Rectangle2D bounds, Shape s) {
		return bounds.contains(s.getBounds());
	}

	//Sets draw metho for sprites
	public void draw(Graphics2D g2) {
		g2.setColor(fill);
		g2.fill(shape);
		g2.setColor(border);
		g2.draw(shape);
	}

	public boolean intersects(Sprite other) {
		return intersects(other.getShape());
	}

	//retruns false if the bounding boxes do no intersect if intersect, do another test
	private boolean intersects(Shape other) {
		if(shape.intersects(other.getBounds())){
			return intersects(new Area(other), new Area(shape));
		} else{
			return false;
		}
	}

	//test to check if either of the area is empty and return's true if false
	private static boolean intersects(Area a, Area b) {
		if(a.isEmpty() || b.isEmpty()){
			return false;
		} else {
			return true;
		}

	}
}
