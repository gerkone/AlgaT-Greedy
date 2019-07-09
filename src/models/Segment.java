package models;

import java.io.IOException;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class Segment {
	
	public static final Double SCALE = 100.0;
	
	private BorderPane block;
	private Line decayMark;
	private Text marker;
	
	private int ft;	//decay time
	private int dt;	//duration
	private int ID;
	private double height;
	
	public Segment(double anchorHeight, int ft, int dt, int id, double infoHeight) throws IOException {
		this.ft = ft;
		this.dt = dt;
		this.ID = id;
		
		block = new BorderPane();
		block.setLeft(new Text("duration:" + Integer.toString(dt)));
		block.setRight(new Text("decay:" + Integer.toString(ft) + "   "));
		block.setCenter(new Text("ID:" + Integer.toString(this.ID)));
		
		block.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		height = anchorHeight * (dt/ SCALE);
		block.setPrefHeight(height);
		
		decayMark = new Line();
		decayMark.setStartX(0);
		decayMark.setEndX(1000);
		decayMark.setStartY(infoHeight * (ft / SCALE));
		decayMark.setEndY(infoHeight * (ft / SCALE));
		marker = new Text();
		marker.setText(Integer.toString(ID));
		marker.setY((decayMark.getEndY() + decayMark.getStartY()) * 0.5);
	}

	public Line getDecayMark() {
		return decayMark;
	}

	public Text getMarker() {
		return marker;
	}

	public int getID() {
		return ID;
	}

	public double getHeight() {
		return height;
	}

	public BorderPane getBlock() {
		return block;
	}

	public int getFt() {
		return ft;
	}

	public int getDt() {
		return dt;
	}
	
	
}
