package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import misc.Edge;
import misc.GraphNode;



public class KruskalController {
	
	private static final double MENU_HEIGHT_t = 400.0;

	private static final double MENU_WIDTH_t = 600.0;
	
	private static final int GRID_COLUMNS = 7;

	private static final int GRID_ROWS = 8;

	private static final int MIN_EDGES = 1;

	private static final int MAX_WEIGHT = 100;

	private static final int SPACE_WIDTH = 666;

	private static final int SPACE_HEIGHT = 772;
	
	
	private Stage stage;
	private Parent root;
	
	private List<GraphNode> nodes;
	private List<Edge> edges;
	
	private ArrayList<ArrayList<Integer>> randomGrid;
	
	@FXML
	Pane graphspace, matrixcontainer;
	@FXML
	Button randomize, reset, start, back, draw;
	@FXML
	Label nodelabel;
	@FXML
	GridPane nodegrid;
	@FXML
	Slider nodesnumber;
	
	public KruskalController() {
		nodes = new ArrayList<GraphNode>();
		edges = new ArrayList<Edge>();
		
		randomGrid = new ArrayList<ArrayList<Integer>>();
		
		newRandomGrid();
		
	}
	
	private void newRandomGrid() {
		for(int i = 0; i < GRID_ROWS; i++) {
			randomGrid.add(new ArrayList<Integer>());
		}
		
		randomGrid.forEach((l) -> {
			for(int i = 0; i < GRID_COLUMNS; i++) {
				l.add(i);
			}	
			Collections.shuffle(l);
		});
	}
	

	@FXML
	public void handleButtons(ActionEvent event) {
		if(event.getSource() == draw) {
			
			startup((int) nodesnumber.getValue());
			draw();
		}
		else if(event.getSource() == randomize) {
			startup((new Random()).nextInt(GRID_COLUMNS*GRID_ROWS));
			draw();
			
		}
		else if (event.getSource() == start) {
			startup((int) nodesnumber.getValue());
			draw();
			doKruskal();
		} 
		else if (event.getSource() == reset) {
			reset();
		} 
		else if (event.getSource() == back) {
			reset();
			
			try {
				root = FXMLLoader.load(getClass().getResource("/menu.fxml"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			stage = (Stage) back.getScene().getWindow();

			Scene change = new Scene(root, MENU_WIDTH_t, MENU_HEIGHT_t);

			if (change != null) {
				stage.setScene(change);
				stage.show();
			}
		}
	}


	

	private void doKruskal() {
		// TODO Auto-generated method stub
		
	}


	private void startup(int d) {
		reset();
		int row;
		int col;
		for(int i = 0; i < d; i++) {
			do {
				row = (new Random()).nextInt(GRID_ROWS);
			} while (randomGrid.get(row).isEmpty());
			
			col = randomGrid.get(row).get(0);
			randomGrid.get(row).remove(0);
			
			nodes.add(new GraphNode(i, row, col));
		}
		addEdges();
	}


	private void addEdges() {
		int edgesQty = (new Random()).nextInt(nodes.size() - MIN_EDGES) + MIN_EDGES;
		
		int uID;
		int vID;
		
		for(int i = 0; i < edgesQty; i++) {
			do {
				uID= nodes.get((new Random()).nextInt(nodes.size())).getID();
				vID = nodes.get((new Random()).nextInt(nodes.size())).getID();
			} while(vID == uID);
			
			edges.add(new Edge(uID, vID, (new Random()).nextInt(MAX_WEIGHT)));
			
		}
	}
	
	
	private void draw() {
		nodegrid.getChildren().clear();
		nodes.forEach((n) -> {
			nodegrid.add(n.getNode(), n.getCol(), n.getRow());
		});
		edges.forEach((e) -> {
			connect(getNodebyID(e.getuID()).getNode(), getNodebyID(e.getvID()).getNode());
		});
	}
	
	private void connect(StackPane n1, StackPane n2) {
		
        Line line = new Line();
   
        line.startXProperty().bind(n1.layoutXProperty().add(SPACE_WIDTH/GRID_COLUMNS/2));
        line.startYProperty().bind(n1.layoutYProperty().add(SPACE_HEIGHT/GRID_ROWS/2));
        line.endXProperty().bind(n2.layoutXProperty().add(SPACE_WIDTH/GRID_COLUMNS/2));
        line.endYProperty().bind(n2.layoutYProperty().add(SPACE_HEIGHT/GRID_ROWS/2));
        graphspace.getChildren().add(line);
        
        
//        line.startXProperty().bind(Bindings.createDoubleBinding(() -> {
//          Bounds b = n1.getParent().getBoundsInParent();
//          return b.getMinX() + b.getWidth() / 2 ;
//      }, n1.getParent().boundsInParentProperty()));
//      line.startYProperty().bind(Bindings.createDoubleBinding(() -> {
//          Bounds b = n1.getParent().getBoundsInParent();
//          return b.getMinY() + b.getHeight() / 2 ;
//      }, n1.getParent().boundsInParentProperty()));
//      line.endXProperty().bind(Bindings.createDoubleBinding(() -> {
//          Bounds b = n2.getParent().getBoundsInParent();
//          return b.getMinX() + b.getWidth() / 2 ;
//      }, n2.getParent().boundsInParentProperty()));
//      line.endYProperty().bind(Bindings.createDoubleBinding(() -> {
//          Bounds b = n2.getParent().getBoundsInParent();
//          return b.getMinY() + b.getHeight() / 2 ;
//      }, n2.getParent().boundsInParentProperty()));
    }


	private GraphNode getNodebyID(int ID) {
		for(GraphNode n : nodes) {
			if(n.getID() == ID) {
				return n;
			}
		} 
		return null;
	}

	private void reset() {
		try {
			graphspace.getChildren().clear();
			nodegrid.getChildren().clear();
			graphspace.getChildren().add(nodegrid);
			newRandomGrid();
			nodes.clear();
			edges.clear();
		} catch (Exception e) {
			System.out.print("qualcosa e' andato storto");
		}
	}

	
}
