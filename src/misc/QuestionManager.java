package misc;

import java.util.ArrayList;

public class QuestionManager {
	
	private static final ArrayList<Question> mooreQuestions 
										= new ArrayList<Question>(){
																{
																	add(new Question("a2", "b", "c", "d", "e", 2));
																	add(new Question("truen3", "b", "c", "d", "e", 3));
																	add(new Question("dungu1", "b", "c", "d", "e", 1));
																	add(new Question("ungu4", "b", "c", "d", "e", 4));
																}
	};
	private static final ArrayList<Question> kruskalQuestions 
										= new ArrayList<Question>() {
																{
																	add(new Question("madamada2", "b", "c", "d", "e", 2));
																	add(new Question("machomann3", "b", "c", "d", "e", 3));
																}
			
	};
	
	private static int nextMoore = 0, nextKruskal = 0;

	public static Question getMooreQuestion() {
		if(mooreQuestions.size() > 0) {
			Question ret = mooreQuestions.get(nextMoore);
			nextMoore = (nextMoore + 1 ) % mooreQuestions.size();
			return ret;
		} else {
			return null;
		}
	}

	public static Question getKruskalQuestion() {
		if(kruskalQuestions.size() > 0) {
			Question ret = kruskalQuestions.get(nextKruskal);
			nextKruskal = (nextKruskal + 1) % kruskalQuestions.size();
			return ret;
		} else {
			return null;
		}
	}

	

}
