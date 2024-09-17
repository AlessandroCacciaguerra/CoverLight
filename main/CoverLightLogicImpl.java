import java.awt.Point;
import java.awt.geom.Line2D;

public class CoverLightLogicImpl {
	private static final int DIM_SQUARE = 50;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 800;
    private static final String LOW = "Low";
    private static final String AVERAGE = "Average";
    private static final String HIGH = "High";
    private static final int WALL_WIDTH_LOW = 4;
    private static final int WALL_WIDTH_AVERAGE = 6;
    private static final int WALL_WIDTH_HIGH = 8;
	private FloorLogicImpl floorLogic;
	private CaptionGUI captionGUI;
	private TutorialGUILogicImpl tutorialGUI; 
	
	public CoverLightLogicImpl(int WIDTH,int  HEIGHT,int  DIM_SQUARE) {
		captionGUI = new CaptionGUI(WIDTH, HEIGHT, DIM_SQUARE);
		floorLogic = new FloorLogicImpl();
		tutorialGUI = new TutorialGUILogicImpl();
	}
	
	public CaptionGUI getCaption() {
		return this.captionGUI;
	}
	
	public FloorLogicImpl getFloorLogic() {
		return this.floorLogic;
	}
	
	public TutorialGUILogicImpl getTutorialGUI() {
		return this.tutorialGUI;
	}
	
	public static boolean validatePosition(Point val) {
        return val.x >= 0 && val.x <= WIDTH && val.y >= 0 && val.y <= WIDTH;
    }

    public static int getWallWidth(String impact) {
        switch (impact) {
            case LOW:
                return WALL_WIDTH_LOW;
            case AVERAGE:
                return WALL_WIDTH_AVERAGE;
            case HIGH:
                return WALL_WIDTH_HIGH;
            default:
                return WALL_WIDTH_AVERAGE;
        }
    }

    public static boolean validateWallPosition(Line2D val) {
        return (val.getX1() >= 0 && val.getX1() <= WIDTH && val.getY1() >= 0 && val.getY1() <= HEIGHT
            && val.getX2() >= 0 && val.getX2() <= WIDTH && val.getY2() >= 0 && val.getY2() <= HEIGHT) && // between the edges
            (val.getX1() % DIM_SQUARE + val.getX2() % DIM_SQUARE + val.getY1() % DIM_SQUARE + val.getY2() % DIM_SQUARE == 0) // divisible by DIM_SQUARE
            && (val.getX1() == val.getX2() ^ val.getY1() == val.getY2()); // not null and parallel to an axis
    }
}