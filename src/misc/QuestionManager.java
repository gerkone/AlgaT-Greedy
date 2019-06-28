package misc;

import java.util.ArrayList;

@SuppressWarnings("serial")
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
	private static final ArrayList<Question> zainoQuestions 
										= new ArrayList<Question>() {
																{
																	add(new Question("mada2", "b", "c", "d", "e", 2));
																	add(new Question("man3", "b", "c", "d", "e", 3));
																}

};
	
	private static int nextMoore = 0, nextKruskal = 0, nextZaino = 0;

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
	
	public static Question getZainoQuestion() {
		if(zainoQuestions.size() > 0) {
			Question ret = zainoQuestions.get(nextZaino);
			nextZaino = (nextZaino + 1 ) % zainoQuestions.size();
			return ret;
		} else {
			return null;
		}
	}

	

}
