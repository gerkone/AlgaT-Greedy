package models;

public class Question {
	
	private String question, answer1, answer2, answer3, answer4;
	
	private int right;
	private boolean answered;
	
	public Question(String question, String answer1, String answer2, String anser3, String answer4, int right) {
		super();
		this.answered = false;
		this.question = question;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = anser3;
		this.answer4 = answer4;
		if ((right > 0) && (right < 5)) {
			this.right = right;
		} else {
			this.right = -1;
		}
	}

	public String getQuestion() {
		return question;
	}

	public String getAnswer1() {
		return answer1;
	}

	public String getAnswer2() {
		return answer2;
	}
	
	public String getAnswer3() {
		return answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public int getRight() {
		return right;
	}
	
	public boolean isAnswered() {
		return answered;
	}

	public void setAnswered(boolean answered) {
		this.answered = answered;
	}

	
	
	
}
