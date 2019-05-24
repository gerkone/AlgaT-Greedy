package misc;

import javafx.scene.shape.Line;

public class Edge {
	private int uID;
	private int vID;
	
	private int weight;
	
	private Line edge;
	
	
	public Edge(int uID, int vID, int weight) {
		super();
		this.uID = uID;
		this.vID = vID;
		this.weight = weight;
		this.edge = new Line();
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

}
