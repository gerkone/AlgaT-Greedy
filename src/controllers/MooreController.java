package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import misc.HeapItem;
import misc.HeapQueue;
import models.Segment;

public class MooreController {

	private static final int MAX_RAND = 30;
	private static final int MAX_RAND_D = 100;
	
	
	private static final Double  SHOWPANE_HEIGHT = 741.0;
	private static final int MAX_SPEED = 2000;
	private static final String LABLE_BOUNDS_ERROR = "Uno dei numeri eccede i limiti: durata<30, scadenza<100";
	private static final String LABLE_BADCHAR_ERROR = "sono presenti caratteri non consentiti";
	private static final String LABLE_DEFAULT = "Aggiungi segmento di durata (vuoto per random)";
	private static final String LABLE_FULL_ERROR = "Limite elementi raggiunto";

	private ArrayList<Segment> segments;
	private ArrayList<Segment> segmentsSorted;

	private ArrayList<Label> code;
	private ArrayList<String> lines;
	
	private Double spaceLeft = SHOWPANE_HEIGHT;
	private int time;	//moore time counter, used as global to be used in Labl showtime
	private boolean threadPause;
	
	private Thread executionThread;

	private Stage stage;
	private Parent root;

	@FXML
	private Button add, reset, start, back, next;
	@FXML
	private TextField numberfield_d, numberfield_f;
	@FXML
	private Label numberlable, showtime;
	@FXML
	private VBox codecontainer, before, result, valuesbox, prioritiesbox;
	@FXML
	private Slider animationspeed;
	@FXML
	private Pane info;
	@FXML
	private SplitPane showcontainer;
	@FXML
	private ImageView playpause;
	
	private Image play, pause;
	
	@FXML
	private QuestionController questionDialogueController;	//controller of the embedded questionnaire, named as rule "<fx:id>Controller" to allow code injection

	public MooreController() {
		super();
		
		this.segments = new ArrayList<Segment>();
		this.segmentsSorted = new ArrayList<Segment>();
		
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
		
		play = new Image("/play.png");
		pause = new Image("/pause.png");
		
		threadPause = false;
		
		time = 0;
	}
	
	public void initialize() {
		
		code.forEach((line) -> {
			codecontainer.getChildren().add(line);
		});

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
	

	@SuppressWarnings("deprecation")
	@FXML
	void handleButtons(ActionEvent event) throws Exception {
		if (event.getSource() == add) {
			manageAdd();
			putSegments();
			putMarkers();
		} else if (event.getSource() == start) {
			if (segments.size() > 1) {
				manageStart();
			}
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

			Scene change = new Scene(root, application.Main.MENU_WIDTH_t, application.Main.MENU_HEIGHT_t);

			if (change != null) {
				stage.setScene(change);
				stage.show();
			}
		} else if(event.getSource() == next) {
			getNextQuestion();
		}
	}

	private void nextHighlight(int p, int s) {

		this.code.get(p).setStyle("-fx-background-color: EEEEEE;");

		this.code.get(s).setStyle("-fx-background-color: yellow;");
		
	}

	private void doMoore() {
		Task<Void> task = new Task<Void>() {
			
		    @Override public Void call() throws InterruptedException, IOException {
		    	HeapQueue<Integer> queue = new HeapQueue<Integer>(segments.size());
		    	
		    	code.get(0).setStyle("-fx-background-color: yellow;");
		    	
		    	Thread.sleep((int) animationspeed.getValue());
		    	
				step(0,1);
				
		    	for(int i = 0; i < segments.size(); i++) {
		    		
		    		segments.get(i).getBlock().setStyle("-fx-background-color: yellow;");
		    		
					step(1,2);
					
					queue.insert(segments.get(i).getID(), segments.get(i).getDt());
					
					step(2,3);
					
					time = time + segments.get(i).getDt();
					
					step(3,4);
					
					if (time >= segments.get(i).getFt()) {
						
						step(4,5);
						
						int mID = queue.deleteMax();
						
						step(5,6);
						
						Segment found = getSegByID(mID);
						time = time - found.getDt();
						
						step(6,7);
						
						found.setBadProgram(true);
						
						step(7,8);
						step(8,2);
						
					}
					Platform.runLater(new Runnable() {
			            @Override public void run() {
			            	showtime.setText("Current time:" + Integer.toString(time));
			            	updateQueue(queue);
			            }

			        });
					
					if(!segments.get(i).isBadProgram())	//i segments marcati con bad (quindi che finiscono dopo il tempo) sono lasciati evidenziati, così da poter essere visti
						segments.get(i).getBlock().setStyle("");
					
					step(4,1);
					
				}
		    	segments.forEach((s) -> {
		    		if(!s.isBadProgram()) {
						try {
							Segment tmp = new Segment(result.getHeight(), 	//se non è programma in ritardo lo aggiungiamo a quelli in orario
									s.getFt(), 
									s.getDt(), 
									s.getID(), 
									info.getHeight());
							segmentsSorted.add(tmp);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
		    		}
		    	});
		    	
		    	Platform.runLater(new Runnable() {
		            @Override public void run() {
		            	putSortedSegments();
		            	updateQueue(queue);
		            }
		        });
		    	
		    	code.forEach((l) -> {
		    		l.setStyle("");
		    	});
		    	code.get(9).setStyle("-fx-background-color: yellow;");
				return null;
		    }

			private void step(int i, int j) {
				try {
					nextHighlight(i, j);
					
					int speed = (int) animationspeed.getValue();
					executionThread.sleep(speed);
					while(threadPause) {
						executionThread.sleep(1000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		
		executionThread = new Thread(task);
		executionThread.start();
	}
	

	private void manageStart() throws Exception {
		softReset();
		Algorithms.sortMoore(segments);
		putSegments();
		putMarkers();
		doMoore();
	}
	
	
	private void manageAdd() throws IOException {
		softReset();
		String dt = numberfield_d.getText();
		String ft = numberfield_f.getText();
		numberfield_d.clear();
		numberfield_f.clear();
		
		int fti = 0;
		int dti = 0;
		if (!ft.isEmpty()) {
			if (ft.matches("(0|[1-9]\\d*)")) {
				fti = Integer.parseInt(ft);
				if (fti <= 0 || fti > 100) {
					numberlable.setText(LABLE_BOUNDS_ERROR);
					fti = 0;
				}
			} else {
				numberlable.setText(LABLE_BADCHAR_ERROR);
			}
		} else {
			fti = dti + (new Random()).nextInt(MAX_RAND_D - dti);
		}
		
		if (!dt.isEmpty()) {
			if (dt.matches("(0|[1-9]\\d*)")) {
				dti = Integer.parseInt(dt);
				if ((dti <= 0 || dti > 30)) {
					numberlable.setText(LABLE_BOUNDS_ERROR);
					dti = 0;
				}
			} else {
				numberlable.setText(LABLE_BADCHAR_ERROR);
			}
		} else {
			dti = 1 + (new Random()).nextInt(MAX_RAND - 1);
		}
		if(fti > 0 && dti > 0) {
			Segment tmpsegment = new Segment(before.getHeight(), fti, dti, getLastID() + 1, info.getHeight());
			if(spaceLeft - tmpsegment.getHeight() >  0) {
				spaceLeft -= tmpsegment.getHeight();
				segments.add(tmpsegment);
			} else {
				int maxDuration = (int) (spaceLeft/before.getHeight() * models.Segment.SCALE); 	//finds the max duration with the remaining free heigh
				segments.add(new Segment(before.getHeight(), fti, maxDuration, getLastID() + 1, info.getHeight()));
				spaceLeft = 0.0;
				numberlable.setText(LABLE_FULL_ERROR);
				numberfield_d.setDisable(true);
				numberfield_f.setDisable(true);
				add.setDisable(true);
			}
		}
	}
	
	
	@SuppressWarnings("deprecation")
	private void reset() {
		numberlable.setText(LABLE_DEFAULT);
		numberfield_d.setDisable(false);
		numberfield_f.setDisable(false);
		add.setDisable(false);
		spaceLeft = SHOWPANE_HEIGHT;
		if(executionThread != null)
			executionThread.stop();
		for (int i = 0; i < code.size(); i++) {
			code.get(i).setStyle("-fx-background-color: EEEEEE;");
		}
		segments.clear();
		segmentsSorted.clear();
		before.getChildren().clear();
		result.getChildren().clear();
		info.getChildren().clear();
		prioritiesbox.getChildren().clear();
		valuesbox.getChildren().clear();
		
		time = 0;
	}
	
	
	@SuppressWarnings("deprecation")
	private void softReset() {
		numberlable.setText(LABLE_DEFAULT);
		if(codecontainer.getChildren().size() == 0) {
			code.forEach((line) -> {
				codecontainer.getChildren().add(line);
			});
		}
		if(executionThread != null) {
			executionThread.stop();
			code.forEach((l) -> l.setStyle("-fx-background-color: EEEEEE;"));
			segments.forEach((x) -> x.getBlock().setStyle(""));
		}
		time = 0;
		showtime.setText("Current time:" + Integer.toString(time));
		segmentsSorted.clear();
		before.getChildren().clear();
		result.getChildren().clear();
		info.getChildren().clear();
		playpause.setImage(play);
		prioritiesbox.getChildren().clear();
		valuesbox.getChildren().clear();
	}
	
	
	private void updateQueue(HeapQueue<Integer> queue) {
		prioritiesbox.getChildren().clear();
		valuesbox.getChildren().clear();
		
		Label val;
		Label pri;
		
		for(HeapItem<Integer> e :queue.getQueue()) {
			val = new Label();
			pri = new Label();
			
			val.setText(Integer.toString(e.value));
			val.setPadding(new Insets(2,2,2,2));
			valuesbox.getChildren().add(val);
			
			pri.setText(Integer.toString(e.priority));
			pri.setPadding(new Insets(2,2,2,2));
			prioritiesbox.getChildren().add(pri);
		}
		
	}
	
	
	private int getLastID() {
		if(segments.size() > 0) {
			return (segments.stream().max(Comparator.comparingInt(Segment::getID)).get()).getID();
		} else {
			return -1;
		}
	}
	
	private Segment getSegByID(int id) {
		for(Segment s : segments) {
			if(s.getID() == id) {
				return s;
			}
		}
		return null;
	}
	

	private void putSortedSegments() {
		result.getChildren().clear();
		segmentsSorted.forEach((b) -> {
			result.getChildren().add(b.getBlock());
		});
	}
	
	
	private void putSegments() {
		before.getChildren().clear();
		segments.forEach((b) -> {
			before.getChildren().add(b.getBlock());
		});
	}
	
	
	private void putMarkers() {
		info.getChildren().clear();
		segments.forEach((s) -> {
			info.getChildren().add(s.getDecayMark());
			info.getChildren().add(s.getMarker());
		});
	}
	

	private void getNextQuestion() {
		if(misc.QuestionManager.isLastMooreAnswered()) {
			questionDialogueController.clear();
			questionDialogueController.enableButtons();
			questionDialogueController.setAll(misc.QuestionManager.getMooreQuestion());
		}
	}

}
