package misc;

import java.util.ArrayList;

public class LessonManager {
	
	private static final String mooreText = "Algoritmo di Moore (1968)\r\n" + 
			"L'algoritmo di Moore è un algoritmo in scelta Greedy utilizzato per selezionare l'esecuzione \r\n" + 
			"più efficiente nel problema dei programmi in ritardo, ovvero dato l'insieme S di programmi, \r\n" + 
			"trovare il sottoinsieme S* tale che minimizzi il numero di programmi che non rispettano la\r\n" + 
			"propria scadenza.\r\n" + 
			"Gli step dell'algoritmo sono i seguenti:\r\n" + 
			"✦ Sequenza iniziale S: programmi ordinati per scadenze di crescenti\r\n" + 
			"✦ Si cerca il primo programma p in ritardo nella sequenza S\r\n" + 
			"✦ Si elimina il più lungo programma p’ tra quelli che precedono p, ottenendo la sequenza S’\r\n" + 
			"✦ Si itera il procedimento su S’, fino ad ottenere S* senza programmi in ritardo\r\n" + 
			"✦ Si eseguono quindi i programmi in S*, seguiti dai programmi in ritardo\r\n" + 
			"\r\n" + 
			"La visualizzazione della lezione è la seguente: La barra a sinistra presenta la possibilità\r\n" + 
			"di aggiungere \"programmi\" alla coda di esecuzione, oltre che visualizzare la linea di codice \r\n" + 
			"in corrente esecuzione, e regolare la velocità dell'animazione; ognuno di questi \r\n" + 
			"programmi sarà caratterizzato da un tempo di esecuzione e una scadenza. Quest'ultima è \r\n" + 
			"visibile in un pannello centrale, rappresentata da una linea che segna il tempo in cui il \r\n" + 
			"programma scadrà.\r\n" + 
			"Nei pannelli di destra sarà possibile vedere la coda di esecuzione ( insieme S,  nel riquadro \r\n" + 
			"più a sinistra) e i programmi riordinati (insieme S*, riquadro a destra), tutto ciò animato \r\n" + 
			"evidenziando il programma correntemente sotto esame.";
	
	private static final String kruskalText = "Algoritmo di Kruskal (1956)\r\n" + 
			"L'algoritmo di Kruskal è un algoritmo Greedy che trova il minimo albero di copertura (o MST) di \r\n" + 
			"un grafo pesato non orientato. l'algoritmo segue il seguente ragionamento\r\n" + 
			"✦ Ingrandire i frammenti di un albero di copertura minimo connettendoli fra di loro fino ad \r\n" + 
			"     avere l’albero complessivo\r\n" + 
			"✦ Si individua un arco sicuro scegliendo un arco [u,v] di peso minimo tra tutti gli archi che \r\n" + 
			"     connettono due distinti frammenti della foresta\r\n" + 
			"✦ L’algoritmo è greedy perché ad ogni passo si aggiunge alla foresta un arco con il peso minore\r\n" + 
			"✦ L’arco scelto è il miglior arco uscente comune ai due frammenti\r\n" + 
			"\r\n" + 
			"La visualizzazione della lezione è la seguente: nel pannello sinistro si trova uno slider per\r\n" + 
			"impostare il numero di nodi del grafo da generare, e la possibilità di modificare la velocità \r\n" + 
			"di animazione. Nella parte destra invece si hanno due Tab: il tab \"visualizer\" è diviso in due\r\n" + 
			"sezioni, una grafica dove verrà rappresentato il grafo con i suoi archi, e passo dopo passo\r\n" + 
			"evidenziato il MST trovato, e una testuale che descriverà i vari archi che fanno parte del MST.\r\n" + 
			"Mentre il tab Matrix mostrerà la matrice di adiacenza con i rispettivi pesi.\r\n";
	
	private static final String zainoText = "Zaino reale, scelta Greedy\r\n" + 
			"L'algoritmo a scelta greedy per risolvere il problema dello zaino con oggetti frazionabili; \r\n" + 
			"infatti solo lo zaino \"reale\" (ovvero con oggetti partizionabili) restituisce la soluzione\r\n" + 
			"migliore con la scelta Greedy, mentre lo zaino \"0/1\" (ovvero non partizionabile, con \r\n" + 
			"oggetti discreti) non gode della scelta greedy.\r\n" + 
			"Data una serie di oggetti, ognuno avente massa e rendita e uno zaino di capienza finita\r\n" + 
			"trovare la sottosequenza che massimizza il guadagno, riempiendo lo \"zaino\".\r\n" + 
			"Gli step dell'algoritmo sono i seguenti:\r\n" + 
			"✦ Pool iniziale di oggetti da \"rubare\" ordinati con varie strategie\r\n" + 
			"✦ Si sceglie il primo oggetto della sequenza ordinata se la capienza dello zaino lo permette\r\n" + 
			"✦ Se un oggetto ha capacità maggiore di quella residua lo si può partizionare\r\n" + 
			"\r\n" + 
			"La visualizzazione della lezione è la seguente: nel pannello a sinistra si trovano varie \r\n" + 
			"impostazioni sulle modalità di esecuzione (strategia di ordinamento), possibilità di \r\n" + 
			"aggiungere nuovi oggetti all'insieme principale, regolare la capienza dello zaino e la\r\n" + 
			"velocità di animazione. Il pannello a destra è invece diviso in pool di oggetti, e oggetti\r\n" + 
			"nello zaino: man mano che l'animazione avanza gli oggetti spariranno dal pool principale\r\n" + 
			"per apparire nello zaino.";
	
	
	public static final ArrayList<Lesson> lessons = new ArrayList<Lesson>() {
		{
			add(new Lesson("Moore", mooreText, "/lessons/moore.fxml", 1000.0, 800.0));
			add(new Lesson("Kruskal", kruskalText, "/lessons/kruskal.fxml", 1000.0, 800.0));
			add(new Lesson("Zaino", zainoText, "/lessons/zaino.fxml", 1000.0, 800.0));
		}
	};
}
