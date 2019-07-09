package models;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Edge  implements Comparable<Edge>{
	private int uID; //first node
	private int vID; //second node
	
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

	@Override
	public int compareTo(Edge e) {
		
		return this.weight - e.weight;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = new String();
		s += "first node : " + this.vID + ", second node : " + this.uID + ", weight :" + this.weight + "\n";
		return s;
	}
	
	

}
