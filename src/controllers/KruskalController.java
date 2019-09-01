package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javafx.beans.binding.DoubleBinding;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Edge;
import models.GraphNode;

public class KruskalController {

	private class Coord {
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

	private static final int MAX_WEIGHT = 100;

	private static final int SPACE_WIDTH = 666;

	private static final int SPACE_HEIGHT = 406;

	protected static final int DEFAULT_SPEED = 500; // msec di step tra ogni ciclo del thread

	private static final int MAX_SPEED = 2000;

	private Stage stage;
	private Parent root;

	private List<GraphNode> nodes;
	private List<Edge> edges;
	private ArrayList<Coord> randomGrid;

	private Thread executionThread;
	private boolean threadPause;

	@FXML
	private Pane graphspace, edgespace, matrixcontainer;
	@FXML
	private Button randomize, reset, start, back, draw, next;
	@FXML
	private Label nodelabel;
	@FXML
	private GridPane nodegrid; 
	@FXML
	private Slider nodesnumber, animationspeed;
	@FXML
	private TextArea testo;
	@FXML
	private ImageView playpause;
	
	private Image play, pause;

	@FXML
	QuestionController questionDialogueController;

	public KruskalController() {
		nodes = new ArrayList<GraphNode>();
		edges = new ArrayList<Edge>();

		randomGrid = new ArrayList<Coord>();

		play = new Image("/play.png");
		pause = new Image("/pause.png");
		threadPause = false;
		newRandomGrid();

	}
	
	public void initialize() { //usato solo per pausare/riprendere l'esecuzione del thread, called from fxml

        animationspeed.valueProperty().addListener((observable, oldValue, newValue) -> {

            if(executionThread != null) {
            	if(newValue.intValue() >= MAX_SPEED) {
					threadPause = true;
            		playpause.setImage(pause);
            	} else {
            		threadPause = false;
            		playpause.setImage(play);
            	}
            }


        });

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
			manageStart((int) nodesnumber.getValue());
			draw();
		} else if (event.getSource() == randomize) {
			int rnd = (new Random()).nextInt(GRID_COLUMNS * GRID_ROWS - 4) + 4;
			nodesnumber.setValue(rnd);
			manageStart(rnd);
			draw();

		} else if (event.getSource() == start) {
			if ((nodes.size() == 0) || (nodes.size() != (int) nodesnumber.getValue())) {//if is the first start event or if the user changed the n of nodes since last time
				manageStart((int) nodesnumber.getValue());
				draw();
			}
			testo.setText("");
//			playpause.setImage(play);
			doKruskal();
		} else if (event.getSource() == reset) {
			reset();
		} else if (event.getSource() == next) {
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
				Set<Edge> set = new HashSet<Edge>();
				application.Algorithms.kruskal((ArrayList<Edge>) edges, nodes.size(), edges.size(), set);
				List<Edge> sorted = new ArrayList<Edge>(set);
				Collections.sort(sorted);
				testo.setFont(new Font(30));
				highlight(sorted);
				graphspace.getChildren().add(testo);
				testo.toFront();

				drawMatrix();
				return null;
			}

			private void highlight(List<Edge> list) throws InterruptedException {
				for (Edge el : list) {
					el.getEdge().setStroke(Color.RED);
					el.getEdge().setStrokeWidth(2);
					int uID = el.getuID();
					int vID = el.getvID();
					((Circle) getNodebyID(uID).getNode().getChildren().get(0)).setStroke(Color.RED);
					((Circle) getNodebyID(vID).getNode().getChildren().get(0)).setStroke(Color.RED);
					el.select();
					fragmentMatrix(uID, vID);
					testo.appendText(el.toString());
					Thread.sleep((long) animationspeed.getValue());
					while(threadPause) {
						executionThread.sleep(1000);
					}
				}
			}

		};

		if (executionThread != null) {//if the thread is already running
			executionThread.stop();
			draw();
		}
		
		executionThread = new Thread(task);
		executionThread.start();
	}

	private void manageStart(int nodesNumber) {//adds nodes and edges
		reset();//elimina il grafo disegnato e blocca il thread
		int row;
		int col;
		for (int i = 0; i < nodesNumber; i++) {

			col = randomGrid.get(0).x;
			row = randomGrid.get(0).y;
			randomGrid.remove(0);
			GraphNode newNode = new GraphNode(i, row, col);
			
			if (nodes.size() > 0) { //se un nodo e' stato aggiunto
				GraphNode adjacent = nodes.get((new Random()).nextInt(nodes.size()));
				Edge tmp = new Edge(newNode.getID(), adjacent.getID(), (new Random()).nextInt(MAX_WEIGHT));
				newNode.addEdge(adjacent);
				adjacent.addEdge(newNode);
				edges.add(tmp);
			}

			nodes.add(newNode);
		}
		addEdges();
		
	}

	private void addEdges() {	//adds more edges to the connected graph

		int edgesQty = new Random().nextInt(nodes.size());
		// con int edgesQty = new Random().nextInt(nodes.size()) + 1 bisogna controllare che edgesQty!=nodes.size()!=4, perche sarebbe impossibile aggiungere il 7th arco
		if(edgesQty == 0)
			edgesQty = 1;
			
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
				
			} while ( (vID == uID) || (nodeU.exists(nodeV)) );
			
			edges.add(new Edge(uID, vID, (new Random()).nextInt(MAX_WEIGHT)));
			nodeU.addEdge(nodeV);
			nodeV.addEdge(nodeU);

		}
	}
	

	@SuppressWarnings("static-access")
	private void drawMatrix() {
		matrixcontainer.getChildren().clear();
		GridPane adjMatrix = new GridPane();

		int rows = SPACE_HEIGHT / (nodes.size() + 1);
		int cols = SPACE_WIDTH / (nodes.size() + 1);
		for (int i = 0; i <= nodes.size(); i++) {
			RowConstraints row = new RowConstraints(rows);
			ColumnConstraints col = new ColumnConstraints(cols);
			adjMatrix.getRowConstraints().add(row);
			adjMatrix.getColumnConstraints().add(col);
		}
		setMatrix(adjMatrix);
		edges.forEach((e) -> {
			Label text0 = new Label();
			Label text1 = new Label();
			text0.setText(String.valueOf(e.getWeight()));
			text1.setText(String.valueOf(e.getWeight()));


			text0.setFont(new Font("Arial", 20));
			text1.setFont(new Font("Arial", 20));

			adjMatrix.add(text0, e.getuID() + 1, e.getvID() + 1); // node, uID = col, vID = row
			adjMatrix.setHalignment(text0, HPos.CENTER);
			adjMatrix.add(text1, e.getvID() + 1, e.getuID() + 1);//inverso di text0
			adjMatrix.setHalignment(text1, HPos.CENTER);
		});

		adjMatrix.setGridLinesVisible(true);
		matrixcontainer.getChildren().add(adjMatrix);
	}
	
	@SuppressWarnings("static-access")
	private void setMatrix(GridPane matrix) {
		for (int i = 0; i < nodes.size(); i++) {
			Label node = new Label(new String(String.valueOf(nodes.get(i).getID())));
			node.setFont(new Font("Arial", 20));
			Label node2 = new Label(new String(String.valueOf(nodes.get(i).getID())));
			node2.setFont(new Font("Arial", 20));
			matrix.add(node, i+1, 0);
			matrix.add(node2, 0, i+1);
			matrix.setHalignment(node, HPos.CENTER);
			matrix.setHalignment(node2, HPos.CENTER);
		}
	}

	private void fragmentMatrix(int uID, int vID) {//color the half the matrix. gridPane class doesn't let you access a specific cell using row, column index
		GridPane adjMatrix = (GridPane) matrixcontainer.getChildren().get(0);
		for (Node node : adjMatrix.getChildren()) {
			boolean found = ((adjMatrix.getRowIndex(node) == vID + 1) && (adjMatrix.getColumnIndex(node) == uID + 1))
					|| ((adjMatrix.getRowIndex(node) == uID + 1) && (adjMatrix.getColumnIndex(node) == vID + 1));
			if (found) {
				node.setStyle("-fx-background-color: red");
				break;
			}
		}
	}

	private void draw() {
		graphspace.getChildren().clear();
		nodegrid.getChildren().clear();
		edgespace.getChildren().clear();
		graphspace.getChildren().add(nodegrid);
		graphspace.getChildren().add(edgespace);
		graphspace.getChildren().add(testo);
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
			addWeight(e);
		});
		drawMatrix();
	}

	private void connect(Edge e) {

		StackPane n1 = getNodebyID(e.getuID()).getNode();
		StackPane n2 = getNodebyID(e.getvID()).getNode();
		DoubleBinding startx = n1.layoutXProperty().add(SPACE_WIDTH / GRID_COLUMNS / 2);
		DoubleBinding endx = n1.layoutYProperty().add(SPACE_HEIGHT / GRID_ROWS / 2);
		DoubleBinding starty = n2.layoutXProperty().add(SPACE_WIDTH / GRID_COLUMNS / 2);
		DoubleBinding endy = n2.layoutYProperty().add(SPACE_HEIGHT / GRID_ROWS / 2);
		e.getEdge().startXProperty().bind(startx);
		e.getEdge().startYProperty().bind(endx);
		e.getEdge().endXProperty().bind(starty);
		e.getEdge().endYProperty().bind(endy);

		edgespace.getChildren().add(e.getEdge());
	}

	private void addWeight(Edge e) {
		Text tWeight = new Text(Integer.toString(e.getWeight()));
		tWeight.setFont(new Font("Verdana", 14));
		tWeight.xProperty().bind(e.getEdge().startXProperty().add(e.getEdge().endXProperty()).multiply(0.5));
		tWeight.yProperty().bind(e.getEdge().startYProperty().add(e.getEdge().endYProperty()).multiply(0.5));
		edgespace.getChildren().add(tWeight);
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
			if (executionThread != null)
				executionThread.stop();
			
			graphspace.getChildren().clear();
			nodegrid.getChildren().clear();
			edgespace.getChildren().clear();
			
			
		    graphspace.getChildren().add(nodegrid);
		    graphspace.getChildren().add(edgespace);
			 
			testo.setText("");
			graphspace.getChildren().add(testo); 
			nodegrid.toFront();
			newRandomGrid();
			nodes.clear();
			edges.clear();

			matrixcontainer.getChildren().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getNextQuestion() {
		if(misc.QuestionManager.isLastKruskalAnswered()) {
			questionDialogueController.clear();
			questionDialogueController.enableButtons();
			questionDialogueController.setAll(misc.QuestionManager.getKruskalQuestion());
		}
	}

}
