package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class ZainoController {
	
	private Stage stage;
	private Parent root;
	
	@FXML
	Slider animationspeed, bagsize, objectpoolsize;
	
	@FXML
	Button start, reset, next, back;
	
	@FXML
	ToggleGroup bagoptions, sortoptions;
	@FXML
	RadioButton sortp, sortv, sortpv;
	@FXML
	ToggleButton stepbag, realbag;
	
	
	@FXML
	private QuestionController questionDialogueController;
	
	
	
	public void handleButtons(ActionEvent event) {
		if (event.getSource() == start) {
			
			if(bagoptions.selectedToggleProperty().equals(stepbag)) {
				stepBagSetup();
			} else {
				realBagSetup();
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

	private void realBagSetup() {
		// TODO Auto-generated method stub
		
	}

	private void stepBagSetup() {
		// TODO Auto-generated method stub
		
	}

	private void reset() {
		// TODO Auto-generated method stub
		
	}
	
	private void getNextQuestion() {
		questionDialogueController.clear();
		questionDialogueController.enableButtons();
		questionDialogueController.setAll(misc.QuestionManager.getZainoQuestion());
	}
}
