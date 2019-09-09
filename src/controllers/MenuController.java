package controllers;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;



public class MenuController {

	private Stage stage;
    private Parent root;
    private Scene scene;
    
    @FXML
    Button toMoore,toPrim,toZaino;
    @FXML
    Accordion lessonmenu;
	
    public void initialize() {	
		misc.LessonManager.lessons.forEach((l) -> {
			Button b = new Button();
			b.setText("Enter");
			b.setLayoutX(534.0);
			b.setLayoutY(285.0);
			scene = null;
			
			b.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	stage = (Stage) lessonmenu.getScene().getWindow();
	            	try {
	            		root = FXMLLoader.load(getClass().getResource(l.getResource()));
	            	} catch (Exception e) {
	            		e.printStackTrace();
	            	}
	    			scene = new Scene(root, l.getWidth(), l.getHeight());
	    			
	    			if(scene != null) {
	    		        stage.setScene(scene);
	    		        stage.setResizable(false);
	    		        stage.show();
	    			}
	            }
	        });
			
			TextArea desc = new TextArea();
			desc.setText(l.getDescription());
			desc.setPrefSize(600, 286);
			desc.setEditable(false);
			desc.setFont(new Font(13));
			
			AnchorPane cont = new AnchorPane();
			cont.getChildren().add(desc);
			cont.getChildren().add(b);
			
			TitledPane tmp = new TitledPane();
			tmp.setText(l.getTitle());
			tmp.setContent(cont);
			lessonmenu.getPanes().add(tmp);
		});
        
	}
}
