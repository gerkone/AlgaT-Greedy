package models;

import java.util.Random;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Oggetto {
	public static final int MAX_WEIGHT = 15;
	public static final int MAX_GAIN = 100;
	
	private int ID;
	private int gain;
	private double weight;
	private boolean selected, bag;
	private BorderPane oggetto;
	
	
	public Oggetto(int profitto, Double peso, int id, boolean bag) {
		super();
		this.ID = id;
		this.gain = profitto;
		this.weight = peso;
		this.selected = false;
		this.bag = bag;
		setText();
	}
	public Oggetto(int id, boolean bag) {
		super();
		this.ID = id;
		this.gain = (new Random()).nextInt(MAX_GAIN) + 1;
		this.weight = Math.floor(((new Random()).nextDouble()*MAX_WEIGHT * 100)) / 100 + 1;
		this.selected = false;
		this.bag = bag;
		setText();
	}
	
	private void setText() {
		this.oggetto = new BorderPane();
		Text contentg = new Text();
		Text contentw = new Text();
		Text contentid = new Text();
		contentg.setText(Integer.toString(gain) + "€\t\t");
		contentw.setText(Double.toString(weight).substring(0, Double.toString(weight).indexOf('.') + 2) + "kg    ");	//soluzione pigra
		contentid.setText("ID:" + Integer.toString(ID));
		this.oggetto.setRight(contentw);
		this.oggetto.setLeft(contentg);
		this.oggetto.setCenter(contentid);
		this.oggetto.setPrefHeight(20);	//altezza container oggetti 740, si hanno 37 oggetti massimi
		this.oggetto.setPrefWidth(332);
		this.oggetto.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		if(bag && !selected) {
			oggetto.setVisible(false);
		}
	}
	
	public void updateText() {
		((Text) this.oggetto.getLeft()).setText(Integer.toString(gain) + "€\t\t");
		((Text) this.oggetto.getRight()).setText(Double.toString(weight).substring(0, Double.toString(weight).indexOf('.') + 2) + "kg    ");

	}
	
	public BorderPane getObjectSprite() {
		return oggetto;
	}

	public int getID() {
		return ID;
	}
	public int getGain() {
		return gain;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
		oggetto.setVisible((this.selected && this.bag) || (!this.selected && !this.bag)); //visibile sse è in bag e è selezionato o è in oggetti e non è selezionato
	}
	public void setGain(int gain) {
		this.gain = gain;
	}
	
	
}
