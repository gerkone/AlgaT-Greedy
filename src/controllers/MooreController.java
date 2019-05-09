package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import misc.Segment;

public class MooreController {
	
	private ArrayList<Segment> segments = new ArrayList<Segment>();
	
	@FXML
	Button add, reset, start;
	@FXML
	TextField numberfield_d, numberfield_f;
	@FXML
	Label numberlable;
	@FXML
	VBox segmentcontainer;
	

	@FXML
	void handleButtons(ActionEvent event) throws IOException {
		if(event.getSource() == add) {
			if(segments.size() < 6) {
				manageAdd();
			} else {
				numberlable.setText("Limite elementi raggiunto");
			}
		} 
	}
	
	
	void manageAdd() throws IOException {
		String dt = numberfield_d.getText();
		String ft = numberfield_f.getText();
		int fti = 0;
		int dti = 0;
		if(!dt.isEmpty() && !dt.isEmpty()) {
			if(dt.matches("(0|[1-9]\\d*)") && ft.matches("(0|[1-9]\\d*)")) {
				fti = Integer.parseInt(ft);
				dti = Integer.parseInt(dt);
			}
		}
		if(fti <= 0 || fti > 20) 
			fti = (new Random()).nextInt(100);
		if(dti <= 0 || dti > 20) 
			dti = (new Random()).nextInt(100);
		Segment tmpsegment = new Segment(fti, dti);
		segments.add(tmpsegment);
		Pane line = tmpsegment.getLine();
		
		segmentcontainer.getChildren().add(line);
	}
	
}
