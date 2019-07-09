package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import misc.Mfset;
import models.Edge;
import models.Oggetto;
import models.Segment;


public class Algorithms {
	public static void sortMoore(ArrayList<Segment> segs) {
		segs.sort(Comparator.comparingInt(s -> s.getFt()));
	}
	
	public static void doMoore(ArrayList<Segment> segs) throws IOException {
        ArrayList<Segment> ret = new ArrayList<Segment>();
		HashMap<Integer, Integer> queue = new HashMap<Integer, Integer>();
		int time = 0;
		for(int i = 1; i < segs.size(); i++) {
			queue.put(segs.get(i).getDt(), i);
			time = time + segs.get(i).getDt();
			if(time >= segs.get(i).getFt()) {
				int t = maxPriority(queue);
				time = time - segs.get(t).getDt();
			}
		}
	}

	public static Integer maxPriority(HashMap<Integer, Integer> queue) {
		Integer max = 0;
		for(Integer k : queue.keySet()) {
			if(k > max) {
				max = k;
			}
		}
		Integer ret = queue.get(max);
		queue.remove(max);
		return ret;
	}
	
	
	public static void kruskal(ArrayList<Edge> A, int n, int m, Set<Edge> T) {
        Mfset mf = new Mfset(n);
        
        Collections.sort(A);
        System.out.println("number of edge : " + A.size());
        
        int c = 0;
        int i = 0;

        while (c < n-1 && i<=m) {
            if (mf.find(A.get(i).getuID()) != mf.find(A.get(i).getvID())) {
                mf.merge(A.get(i).getuID(), A.get(i).getvID());
                T.add(A.get(i));
                System.out.println("added edge : " + A.get(i));
                c++;
            }
            i++;
        }
    }

	public static void bagpd(ArrayList<Oggetto> stuff, int i, int value) {	//bag in programmazione dinamica
		
	}
}


