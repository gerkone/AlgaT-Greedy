package application;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	// main section
	private Pane root;

	private Stage window;

	@Override
	public void start(Stage primaryStage) throws IOException {

		window = (primaryStage);

		root = (Pane) FXMLLoader.load(getClass().getResource("/splash.fxml"));

		Scene scene = new Scene(root, 600, 400);

		window.setTitle("AlgaT Greedy");

		window.setScene(scene);

		window.show();

		FadeTransition ft = new FadeTransition(Duration.millis(500), root);

		ft.setFromValue(1.0);

		ft.setToValue(0.0);
		
		ft.setDelay(Duration.millis(2000));

		ft.play();

		Timeline timeline = new Timeline(new KeyFrame(

				Duration.millis(2500), ae -> {
					try {
						root = (Pane) FXMLLoader.load(getClass().getResource("/menu.fxml"));
					} catch (IOException e) {
						e.printStackTrace();
					}

					Scene change = new Scene(root, 600, 400);

					window.setScene(change);

					window.show();

				}));

		timeline.play();

	}

	public static void main(String[] args) {

		launch(args);

	}

}
