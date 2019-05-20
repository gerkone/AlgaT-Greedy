package misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.Algorithms;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class codeAnimation implements Runnable { //DEPRECATED

	ObservableList<Node> segmentcontainer;
	private ArrayList<Label> code;
	private List<Segment> segments;
	private int animationSpeed;
	
	public codeAnimation(ObservableList<Node> segmentcontainer, ArrayList<Label> code, List<Segment> segments, int animationSpeed) {
		super();
		this.segmentcontainer = segmentcontainer;
		this.code = code;
		this.segments = segments;
		this.animationSpeed = animationSpeed;
	}
	
	private void nextHighlight(int p, int s) throws InterruptedException {

		this.code.get(p).setStyle("-fx-background-color: white;");

		this.code.get(s).setStyle("-fx-background-color: yellow;");
		
	}

	@Override
	public void run() {
		
		HashMap<Integer, Integer> queue = new HashMap<Integer, Integer>();
		int time = 0;
		code.get(0).setStyle("-fx-background-color: white;");
		
    	try {
    		for(int i = 1; i < segments.size(); i++) {
				nextHighlight(1, 2);
				Thread.sleep(animationSpeed);
				queue.put(segments.get(i).getDt(), i);
				nextHighlight(2, 3);
				Thread.sleep(animationSpeed);
				time = time + segments.get(i).getDt();
				nextHighlight(3, 4);
				Thread.sleep(animationSpeed);
				if (time >= segments.get(i).getFt()) {
					nextHighlight(4, 5);
					Thread.sleep(animationSpeed);
					int t = Algorithms.maxPriority(queue);
					nextHighlight(5, 6);
					Thread.sleep(animationSpeed);
					time = time - segments.get(t).getDt();
					nextHighlight(6, 7);
					Thread.sleep(animationSpeed);
					segments.get(t).select();
					nextHighlight(7, 8);
					Thread.sleep(animationSpeed);
					nextHighlight(8, 1);
					Thread.sleep(animationSpeed);
					
					Platform.runLater(new Runnable() {
			            @Override public void run() {
			            	segmentcontainer.clear();
							segments.forEach((s) -> {
								Pane line = s.getLine();
								segmentcontainer.add(line);

							});
			            }
			        });
				}
				else {
					nextHighlight(4, 1);
				}
    		}
    		
			nextHighlight(1, 9);
			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
		}

	
    	
}
