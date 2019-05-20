package misc;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class Segment {
	
	private static final String blackLine = "/tutorial/mooreLine.fxml";
	private static final String redLine = "/tutorial/mooreLineRed.fxml";
	
	private Pane line;
	private int ft;	//finish time
	private int dt;	//duration
	private boolean selected; //selected by moore
	private double anchorWidth;
	private double scale;
	
	public Segment(double anchowWidth, int ft, int dt, int scale) throws IOException {
		selected = false;
		this.line = new Pane();
		this.line = FXMLLoader.load(getClass().getResource(blackLine));
		//scaling and moving the arrow according to its proportions
		this.anchorWidth = anchowWidth;
		this.scale = scale;
		line.setTranslateX(anchowWidth * (double) ft/scale);	//TODO: AnchorPane width * ft/MAX_RAND
		line.setScaleX((double) 4*dt/scale);
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
	
	public void select() throws IOException {
		if(!selected) {
			selected = true;
			this.line = FXMLLoader.load(getClass().getResource(redLine));
			line.setTranslateX(anchorWidth * (double) ft/scale);	//TODO: AnchorPane width * ft/MAX_RAND
			line.setScaleX((double) 4 * dt/scale);
		}
	}
	
	public void deselect() throws IOException {
		if(selected) {
			selected = false;
			this.line = FXMLLoader.load(getClass().getResource(blackLine));
			line.setTranslateX(anchorWidth * (double) ft/scale);	//TODO: AnchorPane width * ft/MAX_RAND
			line.setScaleX((double) 4 * dt/scale);
		}
	}
	
	
}
