package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import application.Algorithms;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import misc.Segment;

public class MooreController {

	private static final double MENU_HEIGHT_t = 400.0;

	private static final double MENU_WIDTH_t = 600.0;

	private static final int MAX_RAND = 100;

	private ArrayList<Segment> segments;

	private ArrayList<Label> code;
	private ArrayList<String> lines;
	
	private Thread executionThread;

	private Stage stage;
	private Parent root;

	@FXML
	Button add, reset, start, back;
	@FXML
	TextField numberfield_d, numberfield_f;
	@FXML
	Label numberlable;
	@FXML
	VBox segmentcontainer, info, codecontainer;
	@FXML
	Slider animationspeed;

	public MooreController() {
		super();
		
		this.segments = new ArrayList<Segment>();
		this.lines = new ArrayList<String>() {
			{
				add("//ordina segmenti per tempo di fine");
				add("for i = 1 to n do");
				add("  | queue.insert(i, t[i])");
				add("  | time = time + t[i]");
				add("  | if(time >= d[i] then)");
				add("  |  | j = queue.deleteMax()");
				add("  |  | time = time - t[j]");
				add("  |  | r[j] = true");
				add("  |  | k = k + 1");
				add("//fine");
			}
		};
		
		this.code = new ArrayList<Label>();
		for (int i = 0; i < lines.size(); i++) { // da 0 a numero linee di codice
			code.add(new Label());
			code.get(i).setText(lines.get(i));
		}
	}

	@SuppressWarnings("deprecation")
	@FXML
	void handleButtons(ActionEvent event) throws Exception {
		if (event.getSource() == add) {
			if (segments.size() < 25) { // TODO: rapporto corretto
				numberlable.setText("Aggiungi segmento di durata (vuoto per random)");
				manageAdd();
			} else {
				numberlable.setText("Limite elementi raggiunto");
			}
		} else if (event.getSource() == start) {
			if (segments.size() > 1) {
				manageStart();
			} else {

			}
		} else if (event.getSource() == reset) {
			executionThread.stop();
			for (int i = 0; i < code.size(); i++) {
				code.get(i).setStyle("-fx-background-color: CBCBCB;");
			}
			segments.clear();
			segmentcontainer.getChildren().clear();
			info.getChildren().clear();
			numberlable.setText("Aggiungi segmento di durata (vuoto per random)");
		} else if (event.getSource() == back) {
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

	private void clear() {
		segments.forEach((s) -> {
			try {
				s.deselect(); // resetta tutte le frecce a nere, e deselezionate
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private void nextHighlight(int p, int s) throws InterruptedException {

		this.code.get(p).setStyle("-fx-background-color: CBCBCB;");

		this.code.get(s).setStyle("-fx-background-color: yellow;");
		
	}

	private void doMoore() {
		Task task = new Task<Void>() {
			
		    @Override public Void call() throws InterruptedException, IOException {
		    	HashMap<Integer, Integer> queue = new HashMap<Integer, Integer>();
				int time = 0;
				code.get(0).setStyle("-fx-background-color: CBCBCB;");
		    	for(int i = 1; i < segments.size(); i++) {
					nextHighlight(1, 2);
					Thread.sleep((int) animationspeed.getValue());
					queue.put(segments.get(i).getDt(), i);
					nextHighlight(2, 3);
					Thread.sleep((int) animationspeed.getValue());
					time = time + segments.get(i).getDt();
					nextHighlight(3, 4);
					Thread.sleep((int) animationspeed.getValue());
					if (time >= segments.get(i).getFt()) {
						nextHighlight(4, 5);
						Thread.sleep((int) animationspeed.getValue());
						int t = Algorithms.maxPriority(queue);
						nextHighlight(5, 6);
						Thread.sleep((int) animationspeed.getValue());
						time = time - segments.get(t).getDt();
						nextHighlight(6, 7);
						Thread.sleep((int) animationspeed.getValue());
						segments.get(t).select();
//						segments.get(t).getLine().setStyle("-fx-background-color: red;");	//fix temporaneo, working
						nextHighlight(7, 8);
						Thread.sleep((int) animationspeed.getValue());
						nextHighlight(8, 2);
						Thread.sleep((int) animationspeed.getValue());
						Platform.runLater(new Runnable() {
				            @Override public void run() {
				            	putSegments();
				            }
				        });
					}
					else {
						nextHighlight(4, 1);
					}
				}
		    	code.forEach((l) -> {
		    		l.setStyle("-fx-background-color: CBCBCB;");
		    	});
		    	code.get(9).setStyle("-fx-background-color: yellow;");
				return null;
		    }
		};
		
		executionThread = new Thread(task);
		executionThread.start();
	}

	private void manageStart() throws Exception {
		putSegments();
		if(codecontainer.getChildren().size() == 0) {
			code.forEach((line) -> {
				codecontainer.getChildren().add(line);
			});
		}
		Algorithms.sortMoore(segments);
		putSegments();
		code.get(0).setStyle("-fx-background-color: yellow;");
		doMoore();

		putSegments();

		// TODO: launch the more algorithm and advance by steps
	}

	private void manageAdd() throws IOException {
		if(executionThread != null) {
			executionThread.stop();
			segments.forEach((s) -> {
				try {
					s.deselect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			putSegments();
		}
		
		String dt = numberfield_d.getText();
		String ft = numberfield_f.getText();
		numberfield_d.clear();
		numberfield_f.clear();
		
		int fti = 0;
		int dti = 0;
		if (!dt.isEmpty() && !dt.isEmpty()) {
			if (dt.matches("(0|[1-9]\\d*)") && ft.matches("(0|[1-9]\\d*)")) {
				fti = Integer.parseInt(ft);
				dti = Integer.parseInt(dt);
			}
		}
		if (dti <= 0 || dti > 20)
			dti = 1 + (new Random()).nextInt(MAX_RAND - 1);
		if (fti <= 0 || fti > 20)
			fti = dti + (new Random()).nextInt(MAX_RAND - dti);

		Segment tmpsegment = new Segment(segmentcontainer.getWidth() - 200, fti, dti, MAX_RAND);
		segments.add(tmpsegment);
		Pane line = tmpsegment.getLine();	//ne aggiunge uno singolo invece di aggiungerli ogni volta tutti
		segmentcontainer.getChildren().add(line);
		Label info_l = new Label("ft:" + fti + "dt:" + dti);
		info.getChildren().add(info_l);
		// da eseguire dopo che i node sono stati aggiunti al root
		double padding = (line.getBoundsInParent().getHeight() - info_l.getBoundsInParent().getHeight()) / 4; // altezza mooreLine importato,allineare labels con frecce
		info_l.setPadding(new Insets(padding, 0, padding, 0));
	}
	
	private void putSegments() {
		segmentcontainer.getChildren().clear(); // pulisce lo spazio dei segmenti
		info.getChildren().clear(); // pulisce lo spazio delle info
		segments.forEach((s) -> {
			Pane line = s.getLine();
			segmentcontainer.getChildren().add(line);
			Label info_l = new Label("ft:" + s.getFt() + "dt:" + s.getDt());
			info.getChildren().add(info_l);
			// da eseguire dopo che i node sono stati aggiunti al root
			// TODO: allineare info con segmenti
			double padding = (line.getBoundsInParent().getHeight() - (double) info_l.getBoundsInParent().getHeight())/ 4; // altezza mooreLine importato, allineare labels con frecce
			info_l.setPadding(new Insets(padding, 0, padding, 0));
		});
	}


}
