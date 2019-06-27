package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import misc.Edge;
import misc.GraphNode;
import misc.Mfset;

public class KruskalController {

	class Coord {
		int x;
		int y;

		Coord(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	private static final double MENU_HEIGHT_t = 400.0;

	private static final double MENU_WIDTH_t = 600.0;

	private static final int GRID_COLUMNS = 4;

	private static final int GRID_ROWS = 5;

	private static final int MIN_EDGES = 1;

	private static final int MAX_WEIGHT = 100;

	private static final int SPACE_WIDTH = 666;

	private static final int SPACE_HEIGHT = 772;

	protected static final int SPEED = 200;	//msec di step tra ogni ciclo del thread

	private Stage stage;
	private Parent root;

	private List<GraphNode> nodes;
	private List<Edge> edges;

	private ArrayList<Coord> randomGrid;

	private Thread executionThread;

	@FXML
	Pane graphspace, edgespace, matrixcontainer;
	@FXML
	Button randomize, reset, start, back, draw, next;
	@FXML
	Label nodelabel;
	@FXML
	GridPane nodegrid;
	@FXML
	Slider nodesnumber;

	@FXML
	QuestionController questionDialogueController;
	
	public KruskalController() {
		nodes = new ArrayList<GraphNode>();
		edges = new ArrayList<Edge>();

		randomGrid = new ArrayList<Coord>();
		
		newRandomGrid();

	}

	private void newRandomGrid() {
		randomGrid.clear();
		for (int i = 0; i < GRID_COLUMNS; i++) {
			for (int j = 0; j < GRID_ROWS; j++) {
				randomGrid.add(new Coord(i, j));
			}
		}
		Collections.shuffle(randomGrid);
	}

	@FXML
	public void handleButtons(ActionEvent event) {
		if (event.getSource() == draw) {
			startup((int) nodesnumber.getValue());
			draw();
		} else if (event.getSource() == randomize) {
			int rnd =  (new Random()).nextInt(GRID_COLUMNS * GRID_ROWS - 4) + 4;
			nodesnumber.setValue(rnd);
			startup(rnd);
			draw();

		} else if (event.getSource() == start) {
			if((nodes.size() == 0) || (nodes.size() != (int) nodesnumber.getValue())) {
				startup((int) nodesnumber.getValue());
				draw();
			}
			doKruskal();
		} else if (event.getSource() == reset) {
			reset();
		} else if(event.getSource() == next) {
			getNextQuestion();
		} else if (event.getSource() == back) {
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
		Task<Void> task = new Task<Void>() {

			@Override
			public Void call() throws InterruptedException, IOException {
				
				int n = nodes.size();
				
				int m = edges.size();
				
				ArrayList<Edge> T = new ArrayList<Edge>();
				
				Mfset mf = new Mfset(n);
				
				ArrayList<Edge> A = new ArrayList<Edge>(edges);
				
				Collections.sort(A, new Comparator<Edge>() {
					@Override
					public int compare(Edge x, Edge y) {
						return x.getWeight() > y.getWeight() ? -1 : (x.getWeight() < y.getWeight()) ? 1 : 0;
					}
				});

				int c = 0;
				int i = 0;

				while (c < n - 1 && i <= m) {
					if (mf.find(A.get(i).getuID()) != mf.find(A.get(i).getvID())) {
						mf.merge(A.get(i).getuID(), A.get(i).getvID());
//						T.add(A.get(i));	superfluo per la simulazione
						
						highlightFragment(i);
						
						Thread.sleep(SPEED);
						
						c++;
					}
					i++;
				}
				
				drawMatrix();	//eseguita solo quando il thread ha finito
				
				return null;
			}

			private void highlightFragment(int i) {
				edges.get(i).getEdge().setStroke(Color.RED);
				edges.get(i).getEdge().setStrokeWidth(2);
				
				((Circle) getNodebyID(edges.get(i).getuID()).getNode().getChildren().get(0)).setStroke(Color.RED);
				((Circle) getNodebyID(edges.get(i).getvID()).getNode().getChildren().get(0)).setStroke(Color.RED);
				
				edges.get(i).select();
			}
		};

		if(executionThread != null) {
			redraw();
			drawMatrix();
			executionThread.stop();
		}
		executionThread = new Thread(task);
		executionThread.start();
	}

	private void startup(int d) {
		reset();
		int row;
		int col;
		for (int i = 0; i < d; i++) {

			col = randomGrid.get(0).x;
			row = randomGrid.get(0).y;
			randomGrid.remove(0);

			GraphNode newNode = new GraphNode(i, row, col);
			if (nodes.size() > 0) {
				GraphNode select = nodes.get((new Random()).nextInt(nodes.size()));
				Edge tmp = new Edge(newNode.getID(), select.getID(), (new Random()).nextInt(MAX_WEIGHT));
				newNode.addEdge(tmp);
				select.addEdge(tmp);
				edges.add(tmp);
			}

			nodes.add(newNode);
		}
		//addEdges();
	}

	private void addEdges() {

		int edgesQty = (new Random()).nextInt(nodes.size());

		int uID;
		int vID;
		GraphNode nodeU;
		GraphNode nodeV;
		for (int i = 0; i < edgesQty; i++) {
			do {
				nodeU = nodes.get((new Random()).nextInt(nodes.size()));
				nodeV = nodes.get((new Random()).nextInt(nodes.size()));
				uID = nodeU.getID();
				vID = nodeV.getID();
			} while ((vID == uID) || (edgeExist(uID, vID)));
			
			edges.add(new Edge(uID, vID, (new Random()).nextInt(MAX_WEIGHT)));

		}
	}

	private boolean edgeExist(int uID, int vID) {
		for(Edge e : edges) {
			if((e.getuID() == uID) || (e.getvID() == uID)) {
				if((e.getuID() == vID) || (e.getvID() == vID)) {
					return true;
				}
			}
		}
		
		return false;
	}

	@SuppressWarnings("static-access")
	private void drawMatrix() {

		matrixcontainer.getChildren().clear();
		GridPane adjMatrix = new GridPane();

		int rows = SPACE_HEIGHT / nodes.size();
		int cols = SPACE_WIDTH / nodes.size();
		for (int i = 0; i < nodes.size(); i++) {
			RowConstraints row = new RowConstraints(rows);
			ColumnConstraints col = new ColumnConstraints(cols);
			adjMatrix.getRowConstraints().add(row);
			adjMatrix.getColumnConstraints().add(col);
		}

		edges.forEach((e) -> {
			Label text0 = new Label();
			Label text1 = new Label();
			text0.setText(String.valueOf(e.getWeight()));
			text1.setText(String.valueOf(e.getWeight()));
			
			if(e.isSelected()) {
				text0.setStyle("-fx-background-color: yellow");
				text1.setStyle("-fx-background-color: yellow");
			}

			text0.setFont(new Font("Arial", 20));
			text1.setFont(new Font("Arial", 20));

			adjMatrix.add(text0, e.getuID(), e.getvID());
			adjMatrix.setHalignment(text0, HPos.CENTER);
			adjMatrix.add(text1, e.getvID(), e.getuID());
			adjMatrix.setHalignment(text1, HPos.CENTER);
		});

		adjMatrix.setGridLinesVisible(true);
		matrixcontainer.getChildren().add(adjMatrix);
	}

	private void draw() {
		graphspace.getChildren().clear();
		nodegrid.getChildren().clear();
		edgespace.getChildren().clear();
		graphspace.getChildren().add(nodegrid);
		graphspace.getChildren().add(edgespace);
		nodegrid.toFront();
		nodes.forEach((n) -> {
			nodegrid.add(n.getNode(), n.getCol(), n.getRow());
		});
		edges.forEach((e) -> {
			connect(e);
		});
		drawMatrix();
	}
	
	private void redraw() {
		graphspace.getChildren().clear();
		nodegrid.getChildren().clear();
		edgespace.getChildren().clear();
		graphspace.getChildren().add(nodegrid);
		graphspace.getChildren().add(edgespace);
		nodegrid.toFront();
		nodes.forEach((n) -> {
			((Circle) n.getNode().getChildren().get(0)).setStroke(Color.BLACK);
			nodegrid.add(n.getNode(), n.getCol(), n.getRow());
		});
		edges.forEach((e) -> {
			e.getEdge().setStroke(Color.BLACK);
			e.getEdge().setStrokeWidth(1);
			e.deselect();
			connect(e);
		});
		drawMatrix();
	}

	private void connect(Edge e) {
		
		StackPane n1 = getNodebyID(e.getuID()).getNode();
		StackPane n2 = getNodebyID(e.getvID()).getNode();

		e.getEdge().startXProperty().bind(n1.layoutXProperty().add(SPACE_WIDTH / GRID_COLUMNS / 2));
		e.getEdge().startYProperty().bind(n1.layoutYProperty().add(SPACE_HEIGHT / GRID_ROWS / 2));
		e.getEdge().endXProperty().bind(n2.layoutXProperty().add(SPACE_WIDTH / GRID_COLUMNS / 2));
		e.getEdge().endYProperty().bind(n2.layoutYProperty().add(SPACE_HEIGHT / GRID_ROWS / 2));
		edgespace.getChildren().add(e.getEdge());

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
		for (GraphNode n : nodes) {
			if (n.getID() == ID) {
				return n;
			}
		}
		return null;
	}

	private void reset() {
		try {
			if(executionThread != null)
				executionThread.stop();
			graphspace.getChildren().clear();
			nodegrid.getChildren().clear();
			edgespace.getChildren().clear();
			graphspace.getChildren().add(nodegrid);
			graphspace.getChildren().add(edgespace);
			nodegrid.toFront();
			newRandomGrid();
			nodes.clear();
			edges.clear();
			
			matrixcontainer.getChildren().clear();
		} catch (Exception e) {
			System.out.print("qualcosa e' andato storto");
		}
	}
	
	private void getNextQuestion() {
		questionDialogueController.clear();
		questionDialogueController.enableButtons();
		questionDialogueController.setAll(misc.QuestionManager.getKruskalQuestion());
	}

}
