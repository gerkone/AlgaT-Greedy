package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import misc.Edge;
import misc.Mfset;
import misc.Segment;


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
				segs.get(t).select();
			}
		}
	}

	public static Integer maxPriority(HashMap<Integer, Integer> queue) {
//		return queue.entrySet()
//				.stream()
//				.max(Map.Entry.comparingByValue())
//				.get()
//				.getKey();
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

        Collections.sort(A, new Comparator<Edge>() {
            @Override
            public int compare(Edge x, Edge y) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return x.getWeight() > y.getWeight() ? -1 : (x.getWeight() < y.getWeight()) ? 1 : 0;
            }
        });
        
        
        int c = 0;
        int i = 0;

        while (c < n-1 && i<=m) {
            if (mf.find(A.get(i).getuID()) != mf.find(A.get(i).getvID())) {
                mf.merge(A.get(i).getuID(), A.get(i).getvID());
                T.add(A.get(i));
                c++;
            }
            i++;
        }
    }
}


