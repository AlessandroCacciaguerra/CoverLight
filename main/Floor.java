import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashMap;

public class Floor {
	private static final int DIM_SQUARE = 50;
	private static final int WIDTH = 1000; 
	private static final int HEIGHT = 800; 

	private CanvasPanel drawPanel;
	private HashMap<Emitters, Boolean> emitters;	//Emitters map
	private HashMap<Utilizers,Boolean> utilizers;	//Utilizers map
	private HashMap<Walls, Boolean> walls;			//Walls map
	private double[][] intensity;
	
	public Floor() {
		this.drawPanel = new CanvasPanel();
		this.emitters = new HashMap<Emitters, Boolean>();
		this.walls = new HashMap<Walls, Boolean>();
		this.utilizers = new HashMap<Utilizers, Boolean>();
		this.intensity = new double[HEIGHT/DIM_SQUARE][WIDTH/DIM_SQUARE];
	}
	
	public void setFloor(Floor f) {
		this.drawPanel = f.getCanvas();
		this.emitters = f.getEmitters();
		this.walls = f.getWalls();
		this.utilizers = f.getUtilizers();
		for (int i = 0; i < HEIGHT/DIM_SQUARE; i++) {
			for (int j = 0; j < WIDTH/DIM_SQUARE; j++) {
				this.intensity[i][j] = f.getIntensity(i, j);
			}
		}
	}
	
//Canvas	
	public CanvasPanel getCanvas() {
		return this.drawPanel;
	}
	
	public void setNewGraphicalEmitter(Rectangle rect, Point2D point) {
		this.drawPanel.getLogic().setNewEmitter(rect, point);
	}
	
	public void setNewGraphicalUtilizer(Rectangle rect) {
		this.drawPanel.getLogic().setNewUtilizer(rect);
	}
	
	public void setNewGraphicalWall(Rectangle rect) {
		this.drawPanel.getLogic().setNewWall(rect);
	}
	
	public void removeGraphicalEmitter(Rectangle rect) {
		this.drawPanel.getLogic().removeEmitter(rect);
	}
	
	public void removeGraphicalUtilizer(Rectangle rect) {
		this.drawPanel.getLogic().removeUtilizer(rect);
	}
	
	public void removeGraphicalWall(Rectangle rect) {
		this.drawPanel.getLogic().removeWall(rect);
	}
	
//Emitters
	public HashMap<Emitters, Boolean> getEmitters(){
		return this.emitters;
	}
	
	public Boolean setNewEmitter(Emitters emitter){
		Boolean flag = null;
		for(Emitters entry : getEmitters().keySet()) { //Compenetration check
			if (emitter.getPosition().distance(entry.getPosition()) < DIM_SQUARE / 2) {
				flag = emitters.get(entry);
				break;
			}
		}
		this.emitters.put(emitter, true);
		Rectangle rect = new Rectangle(emitter.getPosition().x - DIM_SQUARE / 2,emitter.getPosition().y - DIM_SQUARE / 2,DIM_SQUARE,DIM_SQUARE);					
		setNewGraphicalEmitter(rect, emitter.getAngles());
		return flag;
	}
	
	public String removeEmitter(Point emit) {
		if (CoverLightLogicImpl.validatePosition(emit)) {
			for (Emitters entry : getEmitters().keySet()) {
				if (entry.getPosition().equals(emit)) {
					Rectangle rect = new Rectangle(emit.x-DIM_SQUARE/2,emit.y-DIM_SQUARE/2,DIM_SQUARE,DIM_SQUARE);
					removeGraphicalEmitter(rect);
					String text = "(" + emit.x + " " + emit.y + ") " + entry.getmW() + " mW  "
							+ entry.getMHz() + " MHz  [" + entry.getAngles().x + "°-"
							+ entry.getAngles().y + "°]\n";
					this.emitters.remove(entry);
					return text;
				}
			}
		}
		return "";
	}
	
	public boolean enableDisableEmitter(Emitters entry) {
		boolean flag = !this.emitters.replace(entry, !this.emitters.get(entry));
		Rectangle rect = new Rectangle(entry.getPosition().x-DIM_SQUARE/2,entry.getPosition().y-DIM_SQUARE/2,DIM_SQUARE,DIM_SQUARE);
		if(flag) {
			setNewGraphicalEmitter(rect, entry.getAngles());
		} else {
			removeGraphicalEmitter(rect);
		}
		return flag;
	}
	
//Utilizers
	public HashMap<Utilizers, Boolean> getUtilizers(){
		return this.utilizers;
	}
	
	public Boolean setNewUtilizer(Point util){
		Boolean flag = null;
		Utilizers utilizer = new Utilizers(util);
		for (Utilizers entry : getUtilizers().keySet()) {
			if (entry.getPosition().equals(util)) {
				flag = utilizers.get(entry);
				break;
			}
		}
		this.utilizers.put(utilizer, true);
		int x = util.x - util.x % DIM_SQUARE;
		int y = util.y - util.y % DIM_SQUARE;
		Rectangle rect = new Rectangle(x, y, DIM_SQUARE, DIM_SQUARE);
		setNewGraphicalUtilizer(rect);
		return flag;
	}
	
	public String removeUtilizer(Point util){
		if (CoverLightLogicImpl.validatePosition(util)) {
			for (Utilizers entry : getUtilizers().keySet()) {
				if (entry.getPosition().equals(util)) {
					Rectangle rec = new Rectangle(util.x - util.x % DIM_SQUARE,util.y - util.y % DIM_SQUARE,DIM_SQUARE,DIM_SQUARE);
					removeGraphicalUtilizer(rec);
					String text = "(" + util.x + " " + util.y + ")    ";
					this.utilizers.remove(entry);
					return text;
				}
			}
		}
		return "";
	}
	
	public boolean enableDisableUtilizer(Utilizers entry) {
		int x = entry.getPosition().x - entry.getPosition().x % DIM_SQUARE;
		int y = entry.getPosition().y - entry.getPosition().y % DIM_SQUARE;
		boolean flag = !this.utilizers.replace(entry, !this.utilizers.get(entry));
		Rectangle rec = new Rectangle(x, y, DIM_SQUARE, DIM_SQUARE);
		if(flag) {
			setNewGraphicalUtilizer(rec);
		} else {
			removeGraphicalUtilizer(rec);
		}
		return flag;
	}
	
//Walls
	public HashMap<Walls, Boolean> getWalls(){
		return this.walls;
	}
	
	public Boolean setNewWall(Line2D wall, String impactSel) {
		Boolean flag = null;
		for (Walls entry : getWalls().keySet()) { // compenetration check
			if (wall.intersectsLine(entry.getPosition())	//intersection
					&& (!(wall.getX1() == wall.getX2() ^ entry.getPosition().getX1() == entry.getPosition().getX2()))	//collinearity
					&& (Math.max(Math.max(wall.getP1().distance(entry.getPosition().getP1()),wall.getP1().distance(entry.getPosition().getP2())),Math.max(entry.getPosition().getP1().distance(wall.getP2()),entry.getPosition().getP2().distance(wall.getP2())))
							< (wall.getP1().distance(wall.getP2())+entry.getPosition().getP1().distance(entry.getPosition().getP2())))) {	//each contains more than the other's end
				flag = walls.get(entry);
				return flag;
			}
		}
		Walls walls = new Walls(wall, impactSel);
		this.walls.put(walls, true);
		int impact = CoverLightLogicImpl.getWallWidth(walls.getImpact());
		Rectangle rect = new Rectangle((int) Math.min(wall.getX1(), wall.getX2()), (int) Math.min(wall.getY1(), wall.getY2()),
				(int) Math.max(Math.abs(wall.getX2()-wall.getX1()), impact), (int) Math.max(Math.abs(wall.getY2()-wall.getY1()), impact));
		setNewGraphicalWall(rect);
		return flag;
	}

	public String removeWall(Line2D wall){
		if (CoverLightLogicImpl.validateWallPosition(wall)) {
			for (Walls entry : getWalls().keySet()) {
				if (entry.getPosition().getP1().equals(wall.getP1())
				&& entry.getPosition().getP2().equals(wall.getP2())) {
					int impact = CoverLightLogicImpl.getWallWidth(entry.getImpact());
					Rectangle rect = new Rectangle((int) Math.min(entry.getPosition().getX1(), entry.getPosition().getX2()), (int) Math.min(entry.getPosition().getY1(), entry.getPosition().getY2()),
							(int) Math.max(Math.abs(entry.getPosition().getX2()-entry.getPosition().getX1()), impact), (int) Math.max(Math.abs(entry.getPosition().getY2()-entry.getPosition().getY1()), impact));
					removeGraphicalWall(rect);
					String text = "(" + (int) wall.getX1() + " " + (int) wall.getY1() + ")-(" + (int) wall.getX2() + " " + (int) wall.getY2() + ") " + entry.getImpact() + "    ";
					this.walls.remove(entry);
					return text;
				}
			}
		}
		return "";
	}
	
	public String enableDisableWall(Line2D wall) {
		String text = "";
		if (CoverLightLogicImpl.validateWallPosition(wall)) {
			for (Walls entry : getWalls().keySet()) {
				if (entry.getPosition().getP1().equals(wall.getP1())
						&& entry.getPosition().getP2().equals(wall.getP2())) {
					boolean flag = !this.walls.replace(entry, !this.walls.get(entry));
					text = "(" + (int) wall.getX1() + " " + (int) wall.getY1() + ")-(" + (int) wall.getX2() + " " + (int) wall.getY2() + ") " + entry.getImpact() + "    ";
					int impact = CoverLightLogicImpl.getWallWidth(entry.getImpact());
					Rectangle rect = new Rectangle((int) Math.min(wall.getX1(), wall.getX2()), (int) Math.min(wall.getY1(), wall.getY2()),
							(int) Math.max(Math.abs(wall.getX2()-wall.getX1()), impact), (int) Math.max(Math.abs(wall.getY2()-wall.getY1()), impact));
					if(flag) {
						setNewGraphicalWall(rect);
					} else {
						removeGraphicalWall(rect);
					}
					break;
				}
			}
		}
		return text;
	}
	
//intensity
	public double getIntensity(int i, int j) {
		return intensity[i][j];
	}
	
	public void setIntensity(int i, int j, double result) {
		this.intensity[i][j] = result;
	}
}
