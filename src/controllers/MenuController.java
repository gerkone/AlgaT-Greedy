package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;



public class MenuController {
	
	
	private static final double MOORE_HEIGHT_t = 800;


	private static final double MOORE_WIDTH_t = 1000;
	

	private static final double KRUSKAL_HEIGHT_t = 800;


	private static final double KRUSKAL_WIDTH_t = 1000;
	
	
	private static final double ZAINO_HEIGHT_t = 800;


	private static final double ZAINO_WIDTH_t = 1000;
	
	
	private Stage stage;
    private Parent root;
    
    @FXML
    Button toMoore,toPrim,toZaino;
	
	@FXML
	public void go(ActionEvent event) throws IOException {
		stage = (Stage) toMoore.getScene().getWindow();
		Scene scene = null;
		if(event.getSource() == toMoore) {
			root = FXMLLoader.load(getClass().getResource("/tutorial/moore.fxml"));
			scene = new Scene(root, MOORE_WIDTH_t, MOORE_HEIGHT_t);
		} else if (event.getSource() == toPrim) {
			root = FXMLLoader.load(getClass().getResource("/tutorial/kruskal.fxml"));
			scene = new Scene(root, KRUSKAL_WIDTH_t, KRUSKAL_HEIGHT_t);
		} else if(event.getSource() == toZaino) {
			root = FXMLLoader.load(getClass().getResource("/tutorial/zaino.fxml"));
			scene = new Scene(root, ZAINO_WIDTH_t, ZAINO_HEIGHT_t);
		}
		
		if(scene != null) {
	        stage.setScene(scene);
	        stage.show();
		}
        
	}
}
