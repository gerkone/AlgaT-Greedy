package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import application.Algorithms;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
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
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import misc.Edge;
import misc.GraphNode;

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

	private Stage stage;
	private Parent root;

	private List<GraphNode> nodes;
	private List<Edge> edges;

	private ArrayList<Coord> randomGrid;

	private Thread executionThread;

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
			startup((new Random()).nextInt(GRID_COLUMNS * GRID_ROWS));
			draw();

		} else if (event.getSource() == start) {
			startup((int) nodesnumber.getValue());
			draw();
			doKruskal();
		} else if (event.getSource() == reset) {
			reset();
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

				return null;
			}
		};

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
		drawMatrix();
	}

	private void addEdges() {

		int edgesQty = (new Random()).nextInt(nodes.size());

		int uID;
		int vID;

		for (int i = 0; i < edgesQty; i++) {
			do {
				GraphNode nodeU = nodes.get((new Random()).nextInt(nodes.size()));
				GraphNode nodeV = nodes.get((new Random()).nextInt(nodes.size()));
				uID = nodeU.getID();
				vID = nodeV.getID();
			} while (vID == uID);

			edges.add(new Edge(uID, vID, (new Random()).nextInt(MAX_WEIGHT)));

		}
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

			text0.setStyle("-fx-background-color: yellow");
			text1.setStyle("-fx-background-color: yellow");

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

		line.startXProperty().bind(n1.layoutXProperty().add(SPACE_WIDTH / GRID_COLUMNS / 2));
		line.startYProperty().bind(n1.layoutYProperty().add(SPACE_HEIGHT / GRID_ROWS / 2));
		line.endXProperty().bind(n2.layoutXProperty().add(SPACE_WIDTH / GRID_COLUMNS / 2));
		line.endYProperty().bind(n2.layoutYProperty().add(SPACE_HEIGHT / GRID_ROWS / 2));
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
		for (GraphNode n : nodes) {
			if (n.getID() == ID) {
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
