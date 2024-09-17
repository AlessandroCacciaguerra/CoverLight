import java.awt.geom.Line2D;

public class Walls {
	private Line2D position;
	private String impact;
	
	public Walls(Line2D coordinates, String impatto) {
		position = coordinates;
		impact = impatto;
	}
	
	public String getImpact() {
		return impact;
	}
	
	public Line2D getPosition() {
		return position;
	}
}
