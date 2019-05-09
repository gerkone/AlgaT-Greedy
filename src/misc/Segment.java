package misc;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class Segment {
	private Pane line;
	private int ft;	//finish time
	private int dt;	//duration
	
	public Segment(int ft, int dt) throws IOException {
		this.line = FXMLLoader.load(getClass().getResource("/tutorial/mooreLine.fxml"));
		
		//scaling and moving the arrow according to its proportions
		line.setTranslateX(50 * dt);
		line.setMaxWidth(line.getWidth() * dt/20);

		this.ft = ft;
		this.dt = dt;
	}

	public Pane getLine() {
		return line;
	}

	public int getFt() {
		return ft;
	}

	public int getDt() {
		return dt;
	}
	
	
}
