package MyClasses;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPanel;

public class CanvasPanel extends JPanel {
	private static final int HEIGHT = 800;
	private static final int WIDTH = 1000;
	private static final int DIM_SQUARE = 50;
	private static final int MIN_INT = -90;
	private static final float DIM_DASH = DIM_SQUARE/5;
	private static final BasicStroke BLACK_DASH = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {DIM_DASH,DIM_DASH}, 0);
	private static final BasicStroke WHITE_DASH = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {DIM_DASH,DIM_DASH}, DIM_DASH);
	private static final long serialVersionUID = -76517907681793702L;
	private Map<Rectangle, Point2D> gEmitters;
	private Vector<Rectangle> gUtilizers;
	private Vector<Rectangle> gWalls;
	private Dimension area;
	Color[][] rectangles = new Color[HEIGHT/DIM_SQUARE][WIDTH/DIM_SQUARE];
	Integer[][] values = new Integer[HEIGHT/DIM_SQUARE][WIDTH/DIM_SQUARE];
	boolean flag = false;
	
	public CanvasPanel() {
		gEmitters = new HashMap<Rectangle,Point2D>();
		gUtilizers = new Vector<Rectangle>();
		gWalls = new Vector<Rectangle>();
		area = new Dimension(0, 0);
	}
	
	public void copyCanvas(CanvasPanel d) {
		gEmitters = d.gEmitters;
		gUtilizers = d.gUtilizers;
		gWalls = d.gWalls;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
		if(rectangles[0][0] != null) {
			for(int i = DIM_SQUARE / 2; i < HEIGHT; i += DIM_SQUARE) {
				for(int j = DIM_SQUARE / 2; j < WIDTH; j += DIM_SQUARE) {
			        g2d.setColor(rectangles[i/DIM_SQUARE][j/DIM_SQUARE]);
			        g2d.fillRect(j-DIM_SQUARE/2+1, i-DIM_SQUARE/2+1, DIM_SQUARE-2, DIM_SQUARE-2);
				}
			}
		}
		
		for (var entry : gEmitters.entrySet()) {
			g.setColor(Color.BLACK);
			if(entry.getValue().getX()+entry.getValue().getY()==360) {
				g.fillArc(entry.getKey().x, entry.getKey().y, DIM_SQUARE, entry.getKey().height, 0, 360);
			} else {
				g.fillArc(entry.getKey().x, entry.getKey().y, DIM_SQUARE, entry.getKey().height,
						(int) entry.getValue().getX(), (360- (int) entry.getValue().getX()+ (int) entry.getValue().getY())%360);
			}
		}
		
		Rectangle rect;
        for (int i = 0; i < gUtilizers.size(); i++) {
            rect = gUtilizers.elementAt(i);
            int x = rect.x - rect.x % DIM_SQUARE;
    		int y = rect.y - rect.y % DIM_SQUARE;
    		g2d.setColor(Color.BLACK);
    		g2d.setStroke(BLACK_DASH);
    		g2d.drawRect(x, y, DIM_SQUARE, DIM_SQUARE);
    		g2d.setColor(Color.WHITE);
    		g2d.setStroke(WHITE_DASH);
    		g2d.drawRect(x, y, DIM_SQUARE, DIM_SQUARE);
        }
        
        Rectangle rectW;
        for (int i = 0; i < gWalls.size(); i++) {
            rectW = gWalls.elementAt(i);
            switch(Math.min(rectW.width, rectW.height)) {
            	case 5:
            		g2d.setColor(Color.GRAY);
            		break;
            	case 7:
            		g2d.setColor(Color.DARK_GRAY);
            		break;
            	case 9:
            		g2d.setColor(Color.BLACK);
            		break;
        		default:
        			return;
            }
    		g2d.fillRect(rectW.x, rectW.y, rectW.width, rectW.height);
        }

		if(rectangles[0][0] != null) {
	        if(flag) {
	    		for(int i = DIM_SQUARE / 2; i < HEIGHT; i += DIM_SQUARE) {
	    			for(int j = DIM_SQUARE / 2; j < WIDTH; j += DIM_SQUARE) {
				        g2d.setColor(Color.WHITE);
						g2d.drawString(String.valueOf(values[i/DIM_SQUARE][j/DIM_SQUARE]+MIN_INT), j-DIM_SQUARE/5, i-DIM_SQUARE/10);
			        }
				}
			}
	        flag = false;
		}
	}
	
	public Dimension getArea() {
		return this.area;
	}
	
	public void setNewRect(int i, int j, Color colour, int result) {
		rectangles[i][j] = colour;
		if(result != 0) {
			values[i][j] = result;
			flag = true;
		} else {
			flag = false;
		}
	}
	
	public void setNewEmitter(Rectangle rect, Point2D point) {
		gEmitters.put(rect, point);
	}
	
	public void removeEmitter(Rectangle rect) {
		gEmitters.remove(rect);
	}
	
	public void setNewUtilizer(Rectangle rect) {
		gUtilizers.add(rect);
	}
	
	public void removeUtilizer(Rectangle rect) {
		gUtilizers.remove(rect);
	}
	
	public void setNewWall(Rectangle rect) {
		gWalls.add(rect);
	}
	
	public void removeWall(Rectangle rect) {
		gWalls.remove(rect);
	}
	
	public void redraw(Graphics g) {
		g.clearRect(0, 0, this.getArea().width, this.getArea().height);
		gEmitters.clear();
		gWalls.removeAllElements();
		gUtilizers.removeAllElements();
	}
	
	public void setValori(Integer[][] values) {
		if(values != null) {
			flag = true;
			this.values = values;
		} else {
			flag = false;
		}
	}
}