package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import misc.Oggetto;

public class ZainoController {
	private static final int MAX_OGGETTI = 37; // objectare.width/oggetto.width (740/20)
	
	private static final String LABLE_BOUNDS_ERROR = "Valori fuori dai limiti";
	private static final String LABLE_BADCHAR_ERROR = "Caratteri non consentiti";
	private static final String LABLE_DEFAULT = "Aggiungi manualmente:";
	private static final String LABLE_FULL_ERROR = "Limite elementi raggiunto";

	private Stage stage;
	private Parent root;
	private Thread executionThread;
	private int totgain;
	private Double capleft;
	private boolean threadPause;

	@FXML
	private Slider animationspeed, bagsize, objectpoolsize;
	@FXML
	private Button start, reset, next, back, add, fill;
	@FXML
	private ToggleGroup sortoptions;
	@FXML
	private RadioButton sortg, sortw, sortgw;
	@FXML
	private TextField gainadd, weightadd;
	@FXML
	private VBox objectarea, bagarea;
	@FXML
	private Label totalgain, remainingcapacity;
	@FXML
	private Text addlabel;
	@FXML
	private ImageView playpause;
	
	private Image play, pause;

	@FXML
	private QuestionController questionDialogueController;

	private ArrayList<Oggetto> stuff; // oggetti dell'insieme complessivo
	private ArrayList<Oggetto> goodstuff; // oggetti scelti per essere inclusi nello zaino

	public ZainoController() {
		this.stuff = new ArrayList<Oggetto>();
		this.goodstuff = new ArrayList<Oggetto>();
		totgain = 0;
		capleft = 0.0;
		
		play = new Image("/play.png");
		pause = new Image("/pause.png");
		
		threadPause = false;
	}
	
	public void initialize() { //usato solo per pausare/riprendere l'esecuzione del thread

        animationspeed.valueProperty().addListener((observable, oldValue, newValue) -> {

            if(executionThread != null) {
            	if(newValue.intValue() >= 2000) {
					threadPause = true;
            		playpause.setImage(pause);
            	} else {
            		threadPause = false;
            		playpause.setImage(play);
            	}
            }


        });

    }

	public void handleButtons(ActionEvent event) {
		if (event.getSource() == start) {
			if(stuff.size() < (int) objectpoolsize.getValue()) {
				fillStuff();
			}
			manageSort();
			softReset();
//			totalgain.textProperty().bind(new SimpleIntegerProperty(totgain).asString());
//			remainingcapacity.textProperty().bind(new SimpleDoubleProperty(capleft).asString());
			doRealBag();
			
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
		} else if (event.getSource() == add) {
			manageAdd();
		} else if (event.getSource() == fill) {
			fillStuff();
		} else if (event.getSource() == next) {
			getNextQuestion();
		}
	}

	private void fillStuff() {
		softReset();
		int size = (int) objectpoolsize.getValue();
		if (size > 0) {
			reset();
			int gain;
			Double weight;
			for (int i = 0; i < size; i++) {
				weight =  Math.floor(((new Random()).nextDouble() * misc.Oggetto.MAX_WEIGHT * 100)) / 100 + 1;
				gain = (new Random()).nextInt(misc.Oggetto.MAX_GAIN) + 1;
				stuff.add(new Oggetto(gain, weight, i, false));
				goodstuff.add(new Oggetto(gain, weight, i, true));
			}
			updateObjectPool();
		}
	}
	
	private void doRealBag() {
		Task<Void> task = new Task<Void>() {

			@Override
			public Void call() throws InterruptedException, IOException {
				capleft = bagsize.getValue();
				int i = 0;
				Oggetto tmp;
				while (i < stuff.size() && capleft > 0) {
					stuff.get(i).getObjectSprite().setStyle("-fx-background-color: yellow");
					Thread.sleep((int) animationspeed.getValue());
					while(threadPause) {
						executionThread.sleep(1000);
					}
					tmp = getGoodStuffByID(stuff.get(i).getID());	//lista goodstuff non ordinata, preso relativo oggetto da ID e non da indice i
					if (stuff.get(i).getWeight() >= capleft) {
						int partialGain = (int) ((stuff.get(i).getGain())*(capleft/stuff.get(i).getWeight()));
						tmp.setGain(partialGain);
						tmp.setWeight(capleft);
						tmp.setSelected(true);
						tmp.updateText();
						totgain+=partialGain;
//						stuff.get(i).setWeight(stuff.get(i).getWeight() - capleft);		evitare altrimenti si modifica lista di oggetti
//						stuff.get(i).setGain(stuff.get(i).getGain() - partialGain);
						stuff.get(i).getObjectSprite().setStyle("-fx-background-color: red");
						tmp.getObjectSprite().setStyle("-fx-background-color: red");
						capleft = 0.0;
					} else {
						tmp.setSelected(true);
						capleft-=stuff.get(i).getWeight();
						totgain+=stuff.get(i).getGain();
					}
					i++;
					Platform.runLater(new Runnable() {
			            @Override public void run() {
			            	totalgain.setText(Integer.toString(totgain) + "€");
			            	remainingcapacity.setText(Double.toString(Math.floor(capleft * 100)/100) + "kg");
			            }
			        });
				}
				return null;
			}
		};

		if (executionThread != null) {
			executionThread.stop();
		}
		executionThread = new Thread(task);
		executionThread.start();
	}

	private void manageAdd() {
		addlabel.setText(LABLE_DEFAULT);
		if (stuff.size() + 1 < MAX_OGGETTI) {
			String g = gainadd.getText();
			String w = weightadd.getText();
			int gain = 0;
			Double weight = 0.0;
			if(!w.isEmpty()) {
				if (w.matches("([,.0]|[1-9]\\d*)")) {
					if(Double.parseDouble(w) < misc.Oggetto.MAX_WEIGHT + 1) {
						weight = Double.parseDouble(g);
					} else {
						addlabel.setText(LABLE_BOUNDS_ERROR);
					}
				} else {
					addlabel.setText(LABLE_BADCHAR_ERROR);
				}
			} else {
				weight =  Math.floor(((new Random()).nextDouble() * misc.Oggetto.MAX_WEIGHT * 100)) / 100 + 1;
			}
			
			if(!g.isEmpty()) {
				if (g.matches("(0|[1-9]\\d*)")) {
					if(Integer.parseInt(g) < misc.Oggetto.MAX_GAIN + 1) {
						gain = Integer.parseInt(g);
					} else {
						addlabel.setText(LABLE_BOUNDS_ERROR);
					}
				} else {
					addlabel.setText(LABLE_BADCHAR_ERROR);
				}
			} else {
				gain = (new Random()).nextInt(misc.Oggetto.MAX_GAIN) + 1;
			}
			
			if (stuff.size() > 0) {
				stuff.add(new Oggetto(gain, weight, stuff.get(stuff.size() - 1).getID() + 1, false)); // assegna id in base ai precedenti
				goodstuff.add(new Oggetto(gain, weight, stuff.get(stuff.size() - 1).getID() + 1, true));
			} else {
				stuff.add(new Oggetto(gain, weight, 0, false));
				goodstuff.add(new Oggetto(gain, weight, 0, true));
			}
			
			objectpoolsize.setValue(objectpoolsize.getValue() + 1);

			gainadd.clear();
			weightadd.clear();

			updateObjectPool();
		} else {
			addlabel.setText(LABLE_FULL_ERROR);
		}
	}

	private void updateObjectPool() {	//sia bag che insieme completo hanno sempre tutti gli oggetti, ma visibili solo quelli non selezionati/selezionati
		objectarea.getChildren().clear();
		bagarea.getChildren().clear();
		stuff.forEach((o) -> {
			o.getObjectSprite().setStyle("-fx-background-color: lightblue");
			objectarea.getChildren().add(o.getObjectSprite());
		});

		goodstuff.forEach((o) -> {
			o.getObjectSprite().setStyle("-fx-background-color: lightgreen");
			bagarea.getChildren().add(o.getObjectSprite());
		});
	}

	private void manageSort() {
		try {
			if (sortoptions.getSelectedToggle().equals(sortg)) {
				sortByGain();
			} else if (sortoptions.getSelectedToggle().equals(sortw)) {
				sortByWeight();
			} else if (sortoptions.getSelectedToggle().equals(sortgw)) {
				sortByQuotient();
			}
		} catch (NullPointerException npe) {
			// do nothing?
		}
	}

	private void sortByGain() {
		Collections.sort(stuff, new Comparator<Oggetto>() {
			@Override
			public int compare(Oggetto o1, Oggetto o2) {
				return o1.getGain() > o2.getGain() ? -1 : (o1.getGain() < o2.getGain()) ? 1 : 0;
			}
		});
	}

	private void sortByWeight() {
		Collections.sort(stuff, new Comparator<Oggetto>() {
			@Override
			public int compare(Oggetto o1, Oggetto o2) {
				return o1.getWeight() > o2.getWeight() ? -1 : (o1.getWeight() < o2.getWeight()) ? 1 : 0;
			}
		});
	}

	private void sortByQuotient() {
		Collections.sort(stuff, new Comparator<Oggetto>() {
			@Override
			public int compare(Oggetto o1, Oggetto o2) {
				return (o1.getGain() / o1.getWeight()) > (o2.getGain() / o2.getWeight()) ? -1
						: ((o1.getGain() / o1.getWeight()) < (o2.getGain() / o2.getWeight())) ? 1 : 0;
			}
		});
	}

	private Oggetto getGoodStuffByID(int id) {
		for (Oggetto o : goodstuff) {
			if (o.getID() == id) {
				return o;
			}
		}
		return null;
	}
	
	private void softReset() {
		if(executionThread != null) {
			executionThread.stop();
		}
		updateObjectPool();
		goodstuff.forEach((o) -> {
			o.setSelected(false);
		});
		stuff.forEach((o) -> {
			o.setSelected(false);
		});
		capleft = 0.0;
		totgain = 0;
		remainingcapacity.setText("0");
		totalgain.setText("0");
		playpause.setImage(play);
	}

	private void reset() {
		stuff.clear();
		goodstuff.clear();
		objectarea.getChildren().clear();
		bagarea.getChildren().clear();
		capleft = 0.0;
		totgain = 0;
		remainingcapacity.setText("0");
		totalgain.setText("0");
	}

	private void getNextQuestion() {
		if(misc.QuestionManager.isLastZainoAnswered()) {
			questionDialogueController.clear();
			questionDialogueController.enableButtons();
			questionDialogueController.setAll(misc.QuestionManager.getZainoQuestion());
		}
	}
}
