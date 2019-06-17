package misc;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Edge {
	private int uID;
	private int vID;
	
	private int weight;
	
	private Line edge;
	
	private boolean selected;
	
	
	public Edge(int uID, int vID, int weight) {
		super();
		this.uID = uID;
		this.vID = vID;
		this.weight = weight;
		this.edge = new Line();
		this.edge.setFill(Color.BLACK);
		this.edge.setStroke(Color.BLACK);
		this.edge.setStrokeWidth(1);
		this.selected = false;
	}
	
	public int getuID() {
		return uID;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public int getvID() {
		return vID;
	}

	public Line getEdge() {
		return edge;
	}
	
	public void select() {
		this.selected = true;
	}
	
	public void deselect() {
		this.selected = false;
	}
	

	public boolean isSelected() {
		return this.selected;
	}

}
