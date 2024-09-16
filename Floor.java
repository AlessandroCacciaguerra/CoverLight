package MyClasses;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.HashMap;

public class Floor {
	private static final int DIM_SQUARE = 50;
	private static final int WIDTH = 1000; 
	private static final int HEIGHT = 800; 

	private CanvasPanel drawPanel;
	private HashMap<Emitters, Boolean> emitters;	//mappa di emittenti
	private HashMap<Utilizers,Boolean> utilizers;	//mappa di utilizzatori
	private HashMap<Walls, Boolean> walls;			//mappa di muri
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
		this.drawPanel.setNewEmitter(rect, point);
	}
	
	public void setNewGraphicalUtilizer(Rectangle rect) {
		this.drawPanel.setNewUtilizer(rect);
	}
	
	public void setNewGraphicalWall(Rectangle rect) {
		this.drawPanel.setNewWall(rect);
	}
	
	public void removeGraphicalEmitter(Rectangle rect) {
		this.drawPanel.removeEmitter(rect);
	}
	
	public void removeGraphicalUtilizer(Rectangle rect) {
		this.drawPanel.removeUtilizer(rect);
	}
	
	public void removeGraphicalWall(Rectangle rect) {
		this.drawPanel.removeWall(rect);
	}
	
//Emitter	
	public HashMap<Emitters, Boolean> getEmitters(){
		return this.emitters;
	}
	
	public void setNewEmitter(Emitters emitter){
		this.emitters.put(emitter, true);
	}
	
	public void removeEmitter(Emitters emitter){
		this.emitters.remove(emitter);
	}
	
	public boolean enableDisableEmitter(Emitters entry) {
		return !this.emitters.replace(entry, !this.emitters.get(entry));
	}
	
//Utilizer	
	public HashMap<Utilizers, Boolean> getUtilizers(){
		return this.utilizers;
	}
	
	public void setNewUtilizer(Utilizers entry){
		this.utilizers.put(entry, true);
	}
	
	public void removeUtilizer(Utilizers entry){
		this.utilizers.remove(entry);
	}
	
	public boolean enableDisableUtilizer(Utilizers entry) {
		return !this.utilizers.replace(entry, !this.utilizers.get(entry));
	}
	
//Wall	
	public HashMap<Walls, Boolean> getWalls(){
		return this.walls;
	}
	
	public void setNewWall(Walls entry) {
		this.walls.put(entry, true);
	}

	public void removeWall(Walls entry){
		this.walls.remove(entry);
	}
	
	public boolean enableDisableWall(Walls entry) {
		return !this.walls.replace(entry, !this.walls.get(entry));
	}
	
//intensity
	public double getIntensity(int i, int j) {
		return intensity[i][j];
	}
	
	public void setIntensity(int i, int j, double risultato) {
		this.intensity[i][j] = risultato;
	}
}