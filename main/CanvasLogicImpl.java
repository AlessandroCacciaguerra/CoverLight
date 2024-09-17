import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class CanvasLogicImpl {
	
	private Map<Rectangle, Point2D> gEmitters;
	private Vector<Rectangle> gUtilizers;
	private Vector<Rectangle> gWalls;
	
	public CanvasLogicImpl() {
		this.gEmitters = new HashMap<Rectangle,Point2D>();
		this.gUtilizers = new Vector<Rectangle>();
		this.gWalls = new Vector<Rectangle>();
	}
	
	public void copyCanvas(CanvasLogicImpl copyPanel) {
		this.gEmitters = copyPanel.getEmitters();
		this.gUtilizers = copyPanel.getUtilizers();
		this.gWalls = copyPanel.getWalls();
	}
	
	public void setNewEmitter(Rectangle rect, Point2D point) {
		gEmitters.put(rect, point);
	}
	
	public Map<Rectangle, Point2D> getEmitters() {
		return this.gEmitters;
	}
	
	public void removeEmitter(Rectangle rect) {
		gEmitters.remove(rect);
	}
	
	public void setNewUtilizer(Rectangle rect) {
		gUtilizers.add(rect);
	}
	
	public Vector<Rectangle> getUtilizers() {
		return this.gUtilizers;
	}
	
	public void removeUtilizer(Rectangle rect) {
		gUtilizers.remove(rect);
	}
	
	public void setNewWall(Rectangle rect) {
		gWalls.add(rect);
	}
	
	public Vector<Rectangle> getWalls() {
		return this.gWalls;
	}
	
	public void removeWall(Rectangle rect) {
		gWalls.remove(rect);
	}
}
