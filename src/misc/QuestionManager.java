package misc;

import java.util.ArrayList;

import models.Question;

@SuppressWarnings("serial")
public class QuestionManager {
	
	private static final ArrayList<Question> mooreQuestions 
										= new ArrayList<Question>(){
																{
																	add(new Question("In quale anno veniva scritto l'algoritmo di Moore?", "1984", "1966", "1946", "1968", 4));
																	add(new Question("Dato un insieme S di programmi, ognuno con una durata ed una scadenza, l'algoritmo di Moore " + (char)232 +" usato per:", "trovare un sottoinsieme di S che minimizzi il ritardo", "calcolare il tempo di esecuzione complessivo di S", "trovare il pi" + (char)249 + " veloce programma di S", "trovare un sottoinsieme di S che massimizzi il ritardo", 1));
																	add(new Question("La \"scadenza\" in un programma in Moore rappresenta il momento in cui questo programma:", "viene eseguito", "non pu" + (char)224 + " pi" + (char)249 + " essere eseguito", "non serve pi" + (char)249 + " eseguirlo", "finisce l'esecuzione", 3));
																	add(new Question("La scelta greedy in Moore consiste nel scegliere per primo il programma:", "pi" + (char)249 + " breve", "con scadenza minore", "pi" + (char)249 + " veloce", "con scadenza maggiore", 2));
																	add(new Question("Ad ogni iterazione viene eliminato il processo", "pi" + (char)249 +" lungo", "con scadenza minore", "pi" + (char)249 + " breve", "con scadenza maggiore", 1));
																}
	};
	private static final ArrayList<Question> kruskalQuestions 
										= new ArrayList<Question>() {
																{
																	add(new Question("In quale anno veniva scritto l'algoritmo di Kruskal?", "1956", "1856", "1984", "1942", 1));
																	add(new Question("Per cosa sta MST?", "Maximal Spanning Tree", "Motorized Space Tyrant", "Minimum Spanning Tree", "Minimal Span Table", 3));
																	add(new Question("Che cos'" + (char)232 + " un arco sicuro?", "arco a peso minimo tra un nodo interno e uno esterno a un frammento", "unico arco tra due nodi", "arco tra due nodi in un frammento", "arco minimo tra due nodi esterni al frammento", 1));
																	add(new Question("L'algoritmo di Kruskal " + (char)232 + " usato per trovare:", "il minimo grafo di copertura in un albero", "l'arco con peso massimo in un grafo", "il minimo albero di copertura in un grafo", "il nodo pi" + (char)249 + " profondo in un albero", 3));
																	add(new Question("Completa : \nLa scelta greedy in Kruskal consiste nel scegliere per primo l'arco ________ tra gli archi che connettono due frammenti nella foresta", "di peso massimo", "di peso minimo", "tra nodi dispari", "verso un nodo non ancora visitato", 2));
																}
			
	};
	private static final ArrayList<Question> zainoQuestions 
										= new ArrayList<Question>() {
																{
																	add(new Question("Qual'" + (char)232 + " la differenza tra lo zaino \"reale\" e quello \"0/1\"?", "quello reale ha pi" + (char)249 + " oggetti", "quello reale " + (char)232 + " partizionabile, quello 0/1 " + (char)232 + " discreto", "quello 0/1 ha solo oggetti di profitto 0 o 1, quello reale ha un numero reale", "quello reale ha anche masse negative", 2));
																	add(new Question("Completa : Quale metodo di ordinamento per lo zaino " + (char)232 + " mancante? \npeso massimo, profitto massimo, _________", "peso minimo", "prima i pesi maggiori di k" ,"profitto specifico", "profitto minimo", 3));
																	add(new Question("Cosa significa \"partizionabile\"?", "gli oggetti solo formati da pi" + (char)249 + " parti", "gli oggetti possono essere divisi, partizionando massa e profitto", "gli oggetti hanno come massa un numero primo", "gli oggetti hanno tutti profitto nullo", 2));
																	add(new Question("Cosa si fa se la capacit" + (char)224 + "  residua nello zaino " + (char)232 + " minore di quella del prossimo oggetto nella lista?", "si partiziona l'oggetto in base alla capacit" + (char)224 + "  restante", "si termina l'esecuzione", "si allarga lo zaino e si include l'oggetto per intero", "non " + (char)232 + " possibile la capacit" + (char)224 + "  sia minore", 1));
																	add(new Question("La scelta greedy nello zaino reale consiste nel scegliere per primo l'oggetto:", "secondo l'ordinamento utilizzato", "di peso minore", "non " + (char)232 + " un algoritmo greedy", "di profitto massimo", 1));
																	add(new Question("Lo zaino \"0/1\" " + (char)232 + " realizzabile usando la programmazione greedy?", "si, " + (char)232 + " il modo migliore", "si, ma solo se i pesi sono primi", "no, non " + (char)232 + " realizzabile", "si, ma non restituisce sempre la soluzione ottima", 4));
																}

	};
	
	private static int nextMoore = 0, nextKruskal = 0, nextZaino = 0;

	public static Question getMooreQuestion() {
		if (mooreQuestions.size() > 0) {
			Question ret = mooreQuestions.get(nextMoore);
			if(ret.isAnswered()) {
				nextMoore = (nextMoore + 1) % mooreQuestions.size();
				ret = mooreQuestions.get(nextMoore);
			}
			return ret;
		} else {
			return null;
		}
	}

	public static Question getKruskalQuestion() {
		if (kruskalQuestions.size() > 0) {
			Question ret = kruskalQuestions.get(nextKruskal);
			if(ret.isAnswered()) {
				nextKruskal = (nextKruskal + 1) % kruskalQuestions.size();
				ret = kruskalQuestions.get(nextKruskal);
			}
			return ret;
		} else {
			return null;
		}
	}
	
	
	public static Question getZainoQuestion() {
		if (zainoQuestions.size() > 0) {
			Question ret = zainoQuestions.get(nextZaino);
			if(ret.isAnswered()) {
				nextZaino = (nextZaino + 1) % zainoQuestions.size();
				ret = zainoQuestions.get(nextZaino);
			}
			return ret;
		} else {
			return null;
		}
	}

	public static boolean isLastMooreAnswered() {
		if (nextMoore >= 1) {
			return mooreQuestions.get(nextMoore - 1).isAnswered();
		} else {
			return true;
		}
	}

	public static boolean isLastKruskalAnswered() {
		if (nextKruskal >= 1) {
			return kruskalQuestions.get(nextKruskal - 1).isAnswered();
		} else {
			return true;
		}
	}

	public static boolean isLastZainoAnswered() {
		if (nextZaino >= 1) {
			return zainoQuestions.get(nextZaino - 1).isAnswered();
		} else {
			return true;
		}
	}

}

