package models;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class GraphNode {
	private StackPane node;
	private Integer ID;
	private List<Edge> edges;	//TODO: aggingere anche lista di archi in ogni nodo maggiore precisione
	private int row;
	private int col;


	public GraphNode(Integer ID, int row, int col) {
		super();
		this.row = row;
		this.col = col;
		this.ID = ID;
		this.edges = new ArrayList<Edge>();
		this.node = new StackPane();
		Circle circle = new Circle();
		circle.setRadius(20);
		circle.setStrokeWidth(3);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.WHITE);
		node.getChildren().add(circle);
		
		Text text = new Text();
		text.setText(ID.toString());
		text.setBoundsType(TextBoundsType.VISUAL); 
		node.getChildren().add(text);
	}
	
	public void addEdge(Edge id) {
		if ((id.getuID() != this.ID) || (id.getvID() != this.ID)) {
			edges.add(id);
		}
	}
	
	
	public StackPane getNode() {
		return node;
	}

	public int getRow() {
		return row;
	}
	
	public int getID() {
		return ID;
	}


	public int getCol() {
		return col;
	}

	public boolean hasEdges() {
		return edges.size() != 0;
	}

	public boolean exists(Edge tmp) {
		return edges.contains(tmp);
	}
	
}
