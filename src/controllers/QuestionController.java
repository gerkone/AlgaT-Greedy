package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import models.Question;

public class QuestionController {
	
	private final String DEFAULT_TEXT = "answer";
	
	@FXML 
	private Button answer1,answer2,answer3,answer4;
	@FXML
	private TextArea questionArea;
	
	int right = -1;
	
	Question q;
	
	public void setAll(Question q) {
		this.q = q;
		questionArea.setText(q.getQuestion());
		answer1.setText(q.getAnswer1());
		answer2.setText(q.getAnswer2());
		answer3.setText(q.getAnswer3());
		answer4.setText(q.getAnswer4());
		if(!q.isAnswered()) {
			right = q.getRight();
		} else {
			disableButtons();
			
			answer1.setTextFill(Color.RED);
			answer2.setTextFill(Color.RED);
			answer3.setTextFill(Color.RED);
			answer4.setTextFill(Color.RED);
			
			if(q.getRight() == 1) {
				answer1.setTextFill(Color.GREEN);
			} else if(q.getRight() == 2) {
				answer2.setTextFill(Color.GREEN);
			} else if(q.getRight() == 3) {
				answer3.setTextFill(Color.GREEN);
			} else if(q.getRight() == 4) {
				answer4.setTextFill(Color.GREEN);
			}
		}
	}
	
	private void wrong(Button s) {
		s.setTextFill(Color.RED);
		s.setDisable(true);
		questionArea.appendText("\r\nWRONG!");
	}
	
	private void correct(Button s) {
		disableButtons();
		answer1.setTextFill(Color.RED);
		answer2.setTextFill(Color.RED);
		answer3.setTextFill(Color.RED);
		answer4.setTextFill(Color.RED);
		s.setTextFill(Color.GREEN);
		q.setAnswered(true);	
		questionArea.appendText("\r\nCORRECT!");
	}
	
	public void disableButtons() { //to be called immediately
		answer1.setDisable(true);
		answer2.setDisable(true);
		answer3.setDisable(true);
		answer4.setDisable(true);
	}
	
	public void enableButtons() { //to be called after set question/answer
		answer1.setDisable(false);
		answer2.setDisable(false);
		answer3.setDisable(false);
		answer4.setDisable(false);
	}

	@FXML
	public void handleAnswer(ActionEvent event) {
		if(q != null) {
			if(event.getSource() == answer1) {
				if(checkAnswer(1)) {
					correct(answer1);
				} else {
					wrong(answer1);
				}
			} else if(event.getSource() == answer2) {
				if(checkAnswer(2)) {
					correct(answer2);
				} else {
					wrong(answer2);
				}
			} else if(event.getSource() == answer3) {
				if(checkAnswer(3)) {
					correct(answer3);
				} else {
					wrong(answer3);
				}
			} else if(event.getSource() == answer4) {
				if(checkAnswer(4)) {
					correct(answer4);
				} else {
					wrong(answer4);
				}
			}
		}
	}
	
	private boolean checkAnswer(int a) {
		if((right == -1) || (right != a)) {
			return false;
			
		} else {
//			answer1.setDisable(true);
			return true;
		}
	}

	public void clear() {
		disableButtons();
		questionArea.clear();
		answer1.setText(DEFAULT_TEXT);
		answer1.setTextFill(Color.BLACK);
		answer2.setText(DEFAULT_TEXT);
		answer2.setTextFill(Color.BLACK);
		answer3.setText(DEFAULT_TEXT);
		answer3.setTextFill(Color.BLACK);
		answer4.setText(DEFAULT_TEXT);
		answer4.setTextFill(Color.BLACK);
	}
}
