import java.awt.Point;

public class Emitters {
	private Point position;
	private float potenza;
	private float frequenza;
	private Point angles;
	
	public Emitters(Point coordinates, float power, float frequency, int angStart, int angEnd) {
		position = coordinates;
		potenza = power;
		frequenza = frequency;
		angles = new Point(angStart,angEnd);
	}
	
	public Point getPosition() {
		return position;
	}
	
	public Point getAngles() {
		return angles;
	}
	
	public float getmW() {
		return potenza;
	}
	
	public float getMHz() {
		return frequenza;
	}
}
