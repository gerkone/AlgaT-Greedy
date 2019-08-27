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
	private List<Edge> edges;
	private List<GraphNode> adjacents;
	private int row;
	private int col;


	public GraphNode(Integer ID, int row, int col) {
		this.row = row;
		this.col = col;
		this.ID = ID;
		this.edges = new ArrayList<Edge>();
		this.adjacents = new ArrayList<GraphNode>();
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
	
	public void addEdge(GraphNode node) {
		
		if (!adjacents.contains(node))
			adjacents.add(node);
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
		return adjacents.size() > 0;
	}

	public boolean exists(GraphNode node) {
		return adjacents.contains(node);
	}
	
}
