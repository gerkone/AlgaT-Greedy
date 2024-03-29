package application;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private static final boolean SHOW_SPLASH = true;


	public static final double MENU_HEIGHT_t = 400.0;
	
	
	public static final double MENU_WIDTH_t = 600.0;
	

	// main section
	private Pane root;

	private Stage window;

	@Override
	public void start(Stage primaryStage) throws IOException {

		window = (primaryStage);
		if(SHOW_SPLASH) {
			root = (Pane) FXMLLoader.load(getClass().getResource("/splash.fxml"));
	
			Scene scene = new Scene(root, MENU_WIDTH_t, MENU_HEIGHT_t);
	
			window.setTitle("AlgaT Greedy");
	
			try {
				window.getIcons().add(new Image("monopoly_running.png"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			window.setScene(scene);
	
			window.show();
	
			FadeTransition ft = new FadeTransition(Duration.millis(1000), root);
	
			ft.setFromValue(1.0);
	
			ft.setToValue(0.0);
			
			ft.setDelay(Duration.millis(1500));
	
			ft.play();
			
			Timeline timeline = new Timeline(
					new KeyFrame(
							Duration.millis(2500), ae -> {
								load();
							}));

			timeline.play();

		} else {
			load();
		}
		

	}
	
	private void load() {
		try {
			root = (Pane) FXMLLoader.load(getClass().getResource("/menu.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene change = new Scene(root, MENU_WIDTH_t, MENU_HEIGHT_t);

		window.setScene(change);
		
		window.setResizable(false);

		window.show();
	}

	public static void main(String[] args) {

		launch(args);

	}

}
